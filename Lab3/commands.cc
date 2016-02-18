/******************************************************************************
* commands.cc
* CSCI 3060u/SOFE 3980u: Course Project Front End
* Winter 2016
*
* Shotokan Tigers:
* -----
* Akira Aida          100526064
* Kathryn McKay       100524201
* Alexander Wheadon   100514985
*******************************************************************************/
#include "commands.h"

#include <cstring>
#include <math.h>
#include <iostream>
#include <string>

#define ERROR_MESSAGE_INVALID_SESSION "ERROR, SESSION TYPE IS NOT VALID."
#define ERROR_MESSAGE_ACCOUNTLESS_USER "ERROR, THAT USER DOES NOT HAVE AN ACCOUNT."
#define ERROR_MESSAGE_DOUBLE_LOGIN "ERROR, YOU'RE ALREADY LOGGED IN!"
#define ERROR_MESSAGE_NO_LOGIN "ERROR, YOU HAVE NOT LOGGED IN YET."
#define ERROR_MESSAGE_STOLEN_ACCOUNT "ERROR, THE ACCOUNT NUMBER DOESN'T MATCH THE ACCOUNT HOLDER'S NAME."
#define ERROR_BALANCE_INSUFFICIENT "ERROR, THE ACCOUNT DOES NOT HAVE SUFFICIENT FUNDS."

#define PROMPT_ENTER_SESSION_TYPE "Please enter your session type: "
#define PROMPT_ENTER_LOGIN_NAME "Please enter a login name: "
#define PROMPT_ENTER_CUSTOMER_NAME "Please enter the account holder's name: "
#define PROMPT_ENTER_ACCOUNT_NUMBER "Please enter the user's account number: "
#define PROMPT_WITHDRAWAL_VALUE "Please enter an amount to withdraw: "

#define SUCCESS_WITHDRAWAL "Your withdrawal transaction has completed."

namespace BankFrontEnd {
Commands::Commands() {
  is_logged_in_ = false;
  is_admin_ = false;
}

void Commands::SetAccounts(std::map<std::string,
                           std::vector<Account*> >&& accounts) {
  accounts_ = accounts;
}

std::string Commands::DetermineSession() {
  char session[21];
  std::cout << PROMPT_ENTER_SESSION_TYPE  << std::endl;
  std::cin.getline(session, sizeof(session));

  if(strncmp(session, "admin", 20) == 0) {
    return "admin";
  } else if(strncmp(session, "standard", 20) == 0) {
    char login_name[21];
    std::cout << PROMPT_ENTER_LOGIN_NAME << std::endl;
    std::cin.getline(login_name, sizeof(login_name));
    return login_name;
  } else {
    std::cout << ERROR_MESSAGE_INVALID_SESSION << std::endl;
    return "";
  }
}


void Commands::PushTransactionRecord(int code, std::string name,
                                     int account_number, double money,
                                     std::string misc) {

  // it all goes here
  std::string transaction = "";

  // get the transaction type
  std::string code_string;
  {
    code_string = std::to_string(code);
    int zeroes_count = 2 - code_string.size();
    code_string.insert(0, zeroes_count, '0');
  }

  // get the money
  std::string money_string;
  {
    char money_string_buff[16] = { 0 };
    sprintf(money_string_buff, "%.2f", money);
    std::string money_string_buff_2(money_string_buff);
    int zeroes_count = 8 - money_string_buff_2.length();
    zeroes_count = zeroes_count < 0 ? 0 : zeroes_count;
    money_string.insert(0, zeroes_count, '0');
    money_string = money_string + money_string_buff_2; // fix later
  }

  // get the account number
  std::string account_string;
  {
    int zeroes_count = 5 - std::to_string(account_number).size();
    account_string.insert(0, zeroes_count, '0');
    account_string = account_string + std::to_string(account_number);
    // std::cout << account_string << std::endl;
  }

  // shove it in
  transaction.insert(0, 41, ' ');
  transaction.replace(0, 2, code_string);
  transaction.replace(3, name.length(), name);
  transaction.replace(3 + 21, 5, account_string);
  transaction.replace(24 + 6, 8, money_string);
  transaction.replace(39, 2, misc);
  std::cout << "\"" << transaction << "\" " << transaction.size() <<  std::endl;

  // push
  transaction_output_.push_front(transaction);
}


bool Commands::login() {
  if(is_logged_in_ == false) {
    std::string session = DetermineSession();

    if(session != "" && session != "admin") {
      std::vector<Account*> temp = accounts_[session];
      if(temp.empty()) {
        std::cout << ERROR_MESSAGE_ACCOUNTLESS_USER << std::endl;
        return false;
      } else {
        is_logged_in_ = true;
        logged_in_name_ = session;
        PushTransactionRecord(10, session, 00000, 00000.00, "S ");
        return true;
      }
    } else if(session == "admin") {
      is_logged_in_ = true;
      is_admin_ = true;
      PushTransactionRecord(10, "admin", 00000, 00000.00, "A ");
      return true;
    }
    return false;
  } else {
    std::cout << ERROR_MESSAGE_DOUBLE_LOGIN << std::endl;
    return false;
  }
}

bool Commands::withdrawal() {
  if(is_logged_in_ == true) {
    if(is_admin_ == true) {
      std::cout << PROMPT_ENTER_CUSTOMER_NAME << std::endl;
      char name[21] = { 0 };
      std::cin.getline(name, sizeof(name));
      std::cout << PROMPT_ENTER_ACCOUNT_NUMBER << std::endl;
      char num[6] = { 0 };
      std::cin.getline(num, sizeof(num));
      std::cout << PROMPT_WITHDRAWAL_VALUE << std::endl;
      char amount[9] = { 0 };
      std::cin.getline(amount, sizeof(amount));
      std::vector<Account*> temp = accounts_[name];
      if(temp.empty()) {
        std::cout << ERROR_MESSAGE_ACCOUNTLESS_USER << std::endl;
        return false;
      } else {
        bool owned_account = UserExists(name);
        Account* temp_account = GetAccount(name, atoi(num));
        if(owned_account == false || temp_account == nullptr) {
          std::cout << ERROR_MESSAGE_STOLEN_ACCOUNT << std::endl;
        } else {
          if(temp_account->balance > atof(amount) && CheckUnit(atof(amount)) == true) {
            float newBal = temp_account->balance - atof(amount);
            std::string trans = "";
            PushTransactionRecord(01, name, atoi(num), atof(amount));
            std::cout << SUCCESS_WITHDRAWAL << std::endl;
            /*TODO

              Implement update remaining withdrawal

            */
            return true;
          } else {
            std::cout << ERROR_BALANCE_INSUFFICIENT << std::endl; // Very generalized error message atm, may want to break the error cases down?
          }                                                       // Errors for not mod 5/10/20/100 and not enough money
        }
      }

    } else {
      std::cout << PROMPT_ENTER_ACCOUNT_NUMBER << std::endl;
      char num[6] = { 0 };
      std::cin.getline(num, sizeof(num));
      std::cout << PROMPT_WITHDRAWAL_VALUE << std::endl;
      char amount[9] = { 0 };
      std::cin.getline(amount, sizeof(amount));
      std::vector<Account*> temp = accounts_[logged_in_name_];
      if(temp.empty()) {
        std::cout << ERROR_MESSAGE_ACCOUNTLESS_USER << std::endl;
        return false;
      } else {
        bool owned_account = UserExists(logged_in_name_);
        Account* temp_account = GetAccount(logged_in_name_, atoi(num));
        if(owned_account == false || temp_account == nullptr) {
          std::cout << ERROR_MESSAGE_STOLEN_ACCOUNT << std::endl;
        } else {
          if(temp_account->balance > atof(amount) && CheckUnit(atof(amount)) == true) {
            float newBal = temp_account->balance - atof(amount);
            std::string trans = "";
            PushTransactionRecord(01, logged_in_name_, atoi(num), atof(amount));
            std::cout << SUCCESS_WITHDRAWAL << std::endl;
            /*TODO

              Implement account charge for withdraw
              Implement update remaining withdrawal

            */
            return true;
          } else {
            std::cout << ERROR_BALANCE_INSUFFICIENT << std::endl;// Very generalized error message atm, may want to break the error cases down?
          }                                                       // Errors for not mod 5/10/20/100 and not enough money
        }
      }
    }


  } else {
    std::cout << ERROR_MESSAGE_NO_LOGIN << std::endl;
    return false;
  }
  return false;
}

bool Commands::CheckUnit(double amount){
  if(fmod(amount,5) == 0 || fmod(amount, 10) == 0 || fmod(amount, 20) == 0 || fmod(amount, 100) == 0){
    return true;
  }
  return false;
}

bool Commands::UserExists(std::string name) {
  std::vector<Account*> record = accounts_[name];
  return !record.empty();
}

std::string Commands::GetAccountOwner(int number) {
  for(std::pair<const std::string, std::vector<Account*> > pair : accounts_) {
    for(Account* account : pair.second) {
      if(account->number == number) {
        return pair.first;
      }
    }
  }
  return "";
}

Account* Commands::GetAccount(std::string name, int number) {
  // lookup user in directory
  std::vector<Account*> record = accounts_[name];
  if(!record.empty()) {
    for(Account* account : record) {
      if(account->number == number) {
        return account;
      }
    }
  }

  // failed to lookup account
  return nullptr;
}

bool Commands::transfer() {
  if(is_logged_in_ == true) {
    // get name
    std::string name;
    if(is_admin_) {
      // retrieve name
      std::cout << "Please enter the account holder's name: " << std::endl;
      char buffer[20];
      std::cin.getline(buffer, sizeof(buffer));
      name = buffer;

      // check name
      if(!UserExists(name)) {
        std::cout << "Error: user " << name << " does not exist" << std::endl;
        return false;
      }

    } else {
      // use stored name
      name = logged_in_name_;
    }

    // get number
    int number;
    {
      std::cout << "Please enter account to transfer from: " << std::endl;
      char num[6];
      std::cin.getline(num, sizeof(num));
      number = std::stoi(num);

      // check name against account number
      Account* account = GetAccount(name, number);
      if(account == nullptr) {
        std::cout << "Error, account " << name << ":" << number
                  << " was not found" << std::endl;
        return false;
      }
    }



    // Ask for next name
    std::string recipient_name;
    int recipient_number;
    Account* recipient_account;
    {
      std::cout << "Please enter account to transfer to: " << std::endl;
      char num[6];
      std::cin.getline(num, sizeof(num));
      recipient_number = std::stoi(num);

      // find name corresponding
      recipient_name = GetAccountOwner(recipient_number);
      if(recipient_name.empty() || recipient_name == name) {
        std::cout << "Error, " << recipient_number
                  << " is not a valid recipient." << std::endl;
        return false;
      }

      // get account
      recipient_account = GetAccount(recipient_name, recipient_number);
    }

    // TODO: after withdrawal is done



  } else {
    std::cout << ERROR_MESSAGE_NO_LOGIN << std::endl;
    return false;
  }
  return false;
}

bool Commands::paybill() {
  if(is_logged_in_ == true) {

  } else {
    std::cout << ERROR_MESSAGE_NO_LOGIN << std::endl;
    return false;
  }
  return false;
}

bool Commands::deposit() {
  if(is_logged_in_ == true) {

  } else {
    std::cout << ERROR_MESSAGE_NO_LOGIN << std::endl;
    return false;
  }
  return false;
}

bool Commands::create() {
  if(is_logged_in_ == true) {

  } else {
    std::cout << ERROR_MESSAGE_NO_LOGIN << std::endl;
    return false;
  }
  return false;
}

bool Commands::delete_account() {
  if(is_logged_in_ == true) {

  } else {
    std::cout << ERROR_MESSAGE_NO_LOGIN << std::endl;
    return false;
  }
  return false;
}

bool Commands::disable() {
  if(is_logged_in_ == true) {

  } else {
    std::cout << ERROR_MESSAGE_NO_LOGIN << std::endl;
    return false;
  }
  return false;
}

bool Commands::changeplan() {
  if(is_logged_in_ == true) {

  } else {
    std::cout << ERROR_MESSAGE_NO_LOGIN << std::endl;
    return false;
  }
  return false;
}

bool Commands::enable() {
  if(is_logged_in_ == true) {

  } else {
    std::cout << ERROR_MESSAGE_NO_LOGIN << std::endl;
    return false;
  }
  return false;
}

bool Commands::logout() {
  if(is_logged_in_ == true) {
    PushTransactionRecord(00);
    is_logged_in_ = false;
    logged_in_name_ = "";
    is_admin_ = false;

    /*TODO

      Create transactions file w/ all of the dequeu info

    */

  } else {
    std::cout << ERROR_MESSAGE_NO_LOGIN << std::endl;
    return false;
  }
  return false;
  }
}
