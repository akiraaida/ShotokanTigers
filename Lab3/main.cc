/******************************************************************************
* main.cc
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

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <fstream>
#include <iostream>
#include <string>

#include "account.h"
#include "commands.h"

#define ERROR_MESSAGE_ACCOUNTS_LOAD_FAIL "ERROR, ACCOUNTS FILE DID NOT LOAD."
#define ERROR_MESSAGE_INVALID_COMMAND "ERROR, INVALID COMMAND."
#define PROMPT_ENTER_COMMAND "Please enter a command: "

/**
* Main.
**/
int main(int argc, const char* argv[]) {
    BankFrontEnd::Commands commands;

    if(argc == 2) {
      commands.SetAccounts(BankFrontEnd::AccountParser::Parse(argv[1]));
    } else {
      std::cout << ERROR_MESSAGE_ACCOUNTS_LOAD_FAIL << std::endl;
      return 0;
    }

    char user_cmd[11] = { 0 };
    while(true) {
      std::cout << PROMPT_ENTER_COMMAND << std::endl;
      std::cin.getline(user_cmd, sizeof(user_cmd));
      if(strncmp(user_cmd, "login", 10) == 0) {                     // Login        - Done
        commands.login();
      } else if(strncmp(user_cmd, "withdrawal", 10) == 0) {         // Withdrawal   - Done
        commands.withdrawal();
      } else if(strncmp(user_cmd, "transfer", 10) == 0) {           // Transfer     - Done
        commands.transfer();
      } else if(strncmp(user_cmd, "paybill", 10) == 0) {            // Paybill      - Done
        commands.paybill();
      } else if(strncmp(user_cmd, "deposit", 10) == 0) {            // Deposit      - Done
        commands.deposit();
      } else if(strncmp(user_cmd, "create", 10) == 0) {             // Create       -

      } else if(strncmp(user_cmd, "delete", 10) == 0) {             // Delete       - Done
        commands.delete_account();
      } else if(strncmp(user_cmd, "disable", 10) == 0) {            // Disable      - Done
        commands.disable();
      } else if(strncmp(user_cmd, "enable", 10) == 0) {             // Enable       - Done
        commands.enable();
      } else if(strncmp(user_cmd, "changeplan", 10) == 0) {         // Change Plan  - Done
        commands.changeplan();
      } else if(strncmp(user_cmd, "logout", 10) == 0) {             // Logout       - Kinda Done
        commands.logout();
      } else {
        std::cout << ERROR_MESSAGE_INVALID_COMMAND << std::endl;
      }
    }
}
