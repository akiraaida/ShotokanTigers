
#include <iostream>
#include <fstream>
#include <stdlib.h>
#include <stdio.h>
#include <string>
#include "FrontEnd.hpp"




// ./program accounts.txt
int main(int argc, const char* argv[]) {

    std::map<std::string, std::vector<Account*> > accounts;

    if(argc == 2) {
      accounts = AccountParser::parse(argv[1]);
    } else {
      std::cout << "ERROR" << std::endl;
    }


  // for(Account* account : accounts["Ashe Ace            "]) {
  //   std::cout << account->number << std::endl;
  // }


  return 0;
}
