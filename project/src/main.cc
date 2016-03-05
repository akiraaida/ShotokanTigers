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

#include "commands.h"
#include "console_input.h"
#include "account.h"
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

    // take in arguments if possible
    if (argc >= 3) {
      commands.SetAccounts(BankFrontEnd::AccountParser::Parse(argv[1]));
      commands.SetTransactionPath(argv[2]);
    } else {
      std::cout << ERROR_MESSAGE_ACCOUNTS_LOAD_FAIL << std::endl;
      return 0;
    }
    while (std::cin) {
      std::cout << PROMPT_ENTER_COMMAND << std::endl;
      std::string user_cmd = BankFrontEnd::ConsoleInput::GetString();
      if(!std::cin){
        exit(0);
      }
      //std::cout << user_cmd << std::endl;
      if (user_cmd.compare("login") == 0) {
        commands.login();
      } else if (user_cmd.compare("withdrawal") == 0) {
        commands.withdrawal();
      } else if (user_cmd.compare("transfer") == 0) {
        commands.transfer();
      } else if (user_cmd.compare("paybill") == 0) {
        commands.paybill();
      } else if (user_cmd.compare("deposit") == 0) {
        commands.deposit();
      } else if (user_cmd.compare("create") == 0) {
        commands.create();
      } else if (user_cmd.compare("delete") == 0) {
        commands.delete_account();
      } else if (user_cmd.compare("disable") == 0) {
        commands.disable();
      } else if (user_cmd.compare("enable") == 0) {
        commands.enable();
      } else if (user_cmd.compare("changeplan") == 0) {
        commands.changeplan();
      } else if (user_cmd.compare("logout") == 0) {
        commands.logout();
      } else {
        std::cout << ERROR_MESSAGE_INVALID_COMMAND << std::endl;
      }
   }
}
