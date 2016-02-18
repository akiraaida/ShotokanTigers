/******************************************************************************
* account_parser.cc
* CSCI 3060u/SOFE 3980u: Course Project Front End
* Winter 2016
*
* Shotokan Tigers:
* -----
* Akira Aida          100526064
* Kathryn McKay       100524201
* Alexander Wheadon   100514985
*******************************************************************************/
#include "account_parser.h"

#include <stdio.h>
#include <stdlib.h>

#include <fstream>
#include <iostream>
#include <string>

#define ADMIN_MODE_FLAG "A"
#define STANDARD_MODE_FLAG "S"

namespace BankFrontEnd {
namespace AccountParser {
// Parse
std::map<std::string, std::vector<Account*> > Parse(const char* fpath) {
  std::map<std::string, std::vector<Account*> > accounts;
  std::ifstream file;
  file.open(fpath);
  std::string line;

  while (getline(file, line)) {
    std::string num, name, stat, bal, plan;
    num = line.substr(0,5);
    name = line.substr(6, 20);
    stat = line.substr(27, 1);
    bal = line.substr(29, 8);
    plan = line.substr(38, 1);

    // trim the name
    {
      size_t endpos = name.find_last_not_of(" ");
      name = name.substr(0, endpos+1);
    }

    Account* new_account = new Account();
    new_account->number = atoi(num.c_str());
    if (stat == ADMIN_MODE_FLAG) {
      new_account->is_active = 1;
    } else {
      new_account->is_active = 0;
    }

    new_account->balance = atof(bal.c_str());
    if (plan == STANDARD_MODE_FLAG) {
      new_account->is_student_plan = 1;
    } else {
      new_account->is_student_plan = 0;
    }

    new_account->is_deleted = 0;

    accounts[name].push_back(new_account);

  }
  file.close();
  return accounts;
}
}
}
