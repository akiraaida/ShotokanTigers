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

#include <iostream>
#include <fstream>
#include <string>

#include "account.h"
#include "commands.h"
#include "account_parser.h"

#define ERROR_MESSAGE_ACCOUNTS_LOAD_FAIL "Usage: banksys [accounts file] [transaction file]"
#define ERROR_MESSAGE_INVALID_COMMAND "ERROR, INVALID COMMAND."
#define PROMPT_ENTER_COMMAND "Please enter a command: "

/**
* Main.
* The program allows for a user to access their bank account(s) and make simple, everyday transactions.
* It will also allow the admins to modify, create and perform more advanced transactions that a normal user cannot.
* Input: accounts.txt
* Output: transactions.txt
* Usage: make
* Usage: .\frontend.exe accounts.txt
**/
int main(int argc, const char* argv[]) {
    // commands
    BankFrontEnd::Commands commands;
    const char* transactions_fpath;

    if (argc >= 3) {
      commands.SetAccounts(BankFrontEnd::AccountParser::Parse(argv[1]));
      transactions_fpath = argv[2];
    } else {
      std::cout << ERROR_MESSAGE_ACCOUNTS_LOAD_FAIL << std::endl;
      return 0;
    }

    char user_cmd[11] = { 0 };
    while (std::cin.good()) {
      std::cout << PROMPT_ENTER_COMMAND << std::endl;
      std::cin.getline(user_cmd, sizeof(user_cmd));
      if (strncmp(user_cmd, "login", 10) == 0) {
        commands.login();
      } else if (strncmp(user_cmd, "withdrawal", 10) == 0) {
        commands.withdrawal();
      } else if (strncmp(user_cmd, "transfer", 10) == 0) {
        commands.transfer();
      } else if (strncmp(user_cmd, "paybill", 10) == 0) {
        commands.paybill();
      } else if (strncmp(user_cmd, "deposit", 10) == 0) {
        commands.deposit();
      } else if (strncmp(user_cmd, "create", 10) == 0) {
        commands.create();
      } else if (strncmp(user_cmd, "delete", 10) == 0) {
        commands.delete_account();
      } else if (strncmp(user_cmd, "disable", 10) == 0) {
        commands.disable();
      } else if (strncmp(user_cmd, "enable", 10) == 0) {
        commands.enable();
      } else if (strncmp(user_cmd, "changeplan", 10) == 0) {
        commands.changeplan();
      } else if (strncmp(user_cmd, "logout", 10) == 0) {
        commands.logout();
      } else {
        std::cout << ERROR_MESSAGE_INVALID_COMMAND << std::endl;
      }
    }
}
