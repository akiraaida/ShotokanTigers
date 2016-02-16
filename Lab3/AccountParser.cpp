#include "FrontEnd.hpp"
#include <iostream>
#include <fstream>
#include <stdlib.h>
#include <stdio.h>
#include <string>

std::map<std::string, std::vector<Account*> > AccountParser::parse(const char* fpath) {

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

    Account* newAcc = new Account();
    newAcc->number = atoi(num.c_str());
    if (stat == "A") {
      newAcc->isActive = 1;
    } else {
      newAcc->isActive = 0;
    }

    newAcc->balance = atof(bal.c_str());
    if (plan == "S") {
      newAcc->isStudentPlan = 1;
    } else {
      newAcc->isStudentPlan = 0;
    }

    accounts[name].push_back(newAcc);

  }
  file.close();
  return accounts;
}
