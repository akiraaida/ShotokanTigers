/******************************************************************************
* main.cpp
* CSCI 3060u/SOFE 3980u: Course Project Front End
* Winter 2016
*
* Shotokan Tigers:
* -----
* Akira Aida          100526064
* Kathryn McKay       100524201
* Alexander Wheadon   100514985
*
* Program Intention:
* ----
*
* Input & Output Files:
* ----
*
*
* How the Program Is Intended to Be Run:
* ----
*
*******************************************************************************/
#include <iostream>
#include <fstream>
#include <stdlib.h>
#include <stdio.h>
#include <string>
#include "FrontEnd.hpp"

int main(int argc, const char* argv[]) {

    std::map<std::string, std::vector<Account*> > accounts;

    if(argc == 2) {
      accounts = AccountParser::parse(argv[1]);
    } else {
      std::cout << "ERROR" << std::endl;
    }
}
