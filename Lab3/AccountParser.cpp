#include "Account.hpp"

#include <stdio.h>
#include <stdlib.h>

#include <fstream>
#include <iostream>
#include <string>

namespace BankFrontEnd {
namespace AccountParser {
std::map<std::string, std::vector<Account*> > Parse(const char* fpath) {

  std::map<std::string, std::vector<Account*> > accounts;
  std::ifstream file;
  file.open(fpath);
  std::string line;

  while(getline(file, line)) {
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
      // std::cout << "read \"" << name << "\"" << std::endl;
    }

    Account* new_account = new Account();
    new_account->number = atoi(num.c_str());
    if (stat == "A") {
      new_account->is_active = 1;
    } else {
      new_account->is_active = 0;
    }

    new_account->balance = atof(bal.c_str());
    if (plan == "S") {
      new_account->is_student_plan = 1;
    } else {
      new_account->is_student_plan = 0;
    }

    accounts[name].push_back(new_account);

  }
  file.close();
  return accounts;
}
}
}
