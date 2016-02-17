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

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include <iostream>
#include <fstream>
#include <string>

#include "Account.hpp"
#include "Commands.hpp"

int main(int argc, const char* argv[]) {
    Commands commands;

    if(argc == 2) {
      commands.setAccounts(AccountParser::parse(argv[1]));
    } else {
      std::cout << "ERROR, ACCOUNTS FILE DID NOT LOAD." << std::endl;
      return 0;
    }

    char userCmd[11];
    while(true){
      std::cout << "Please enter a command: " << std::endl;
      std::cin.getline(userCmd, sizeof(userCmd));
      if(strncmp(userCmd, "login", 10) == 0){
        commands.login();
      } else if(strncmp(userCmd, "withdrawal", 10) == 0){
        commands.withdrawal();
      } else if(strncmp(userCmd, "transfer", 10) == 0){
        commands.transfer();
      } else if(strncmp(userCmd, "paybill", 10) == 0){

      } else if(strncmp(userCmd, "deposit", 10) == 0){

      } else if(strncmp(userCmd, "create", 10) == 0){

      } else if(strncmp(userCmd, "delete", 10) == 0){

      } else if(strncmp(userCmd, "disable", 10) == 0){

      } else if(strncmp(userCmd, "enable", 10) == 0){

      } else if(strncmp(userCmd, "changeplan", 10) == 0){

      } else if(strncmp(userCmd, "logout", 10) == 0){

      } else{
        std::cout << "ERROR, INVALID COMMAND." << std::endl;
      }
    }
}
