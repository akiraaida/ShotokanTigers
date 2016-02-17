#include "Commands.hpp"

#include <cstring>

#include <iostream>
#include <string>

namespace BankFrontEnd {
Commands::Commands(){
  is_logged_in_ = false;
  is_admin_ = false;
}

void Commands::SetAccounts(std::map<std::string, std::vector<Account*> >&& accounts) {
  accounts_ = accounts;
}

std::string Commands::DetermineSession(){
  char session[21];
  std::cout << "Please enter your session type: " << std::endl;
  std::cin.getline(session, sizeof(session));

  if(strncmp(session, "admin", 20) == 0) {
    return "admin";
  } else if(strncmp(session, "standard", 20) == 0) {
    char login_name[21];
    std::cout << "Please enter a login name: " << std::endl;
    std::cin.getline(login_name, sizeof(login_name));
    return login_name;
  } else{
    std::cout << "ERROR, SESSION TYPE IS NOT VALID." << std::endl;
    return "";
  }
}


void Commands::PushTransactionRecord(int code, std::string name, int account_number, double money, std::string misc) {
  std::string transaction = "";
  std::string code_string;
  {
    code_string = std::to_string(code);
    int zeroes_count = 2 - code_string.size();
    code_string.insert(0, zeroes_count, '0');
  }
  std::string money_string;
  {
    char money_string_buff[16] = { 0 };
    sprintf(money_string_buff, "%5.2f", money);
    std::string money_string_buff_2(money_string_buff);
    int zeroes_count = 8 - money_string_buff_2.length();
    zeroes_count = zeroes_count < 0 ? 0 : zeroes_count;
    money_string.insert(0, zeroes_count, '0');
    money_string = money_string + money_string_buff_2; // fix later
  }
  std::string account_string;
  {
    int zeroes_count = 5 - std::to_string(account_number).size();
    account_string.insert(0, zeroes_count, '0');
    account_string = account_string + std::to_string(account_number);
    std::cout << account_string << std::endl;
  }

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

  if(is_logged_in_ == false){
    std::string session = DetermineSession();

    // testing
    PushTransactionRecord(10);

    if(session != "" && session != "admin") {
      std::vector<Account*> temp = accounts_[session];
      if(temp.empty()){
        std::cout << "ERROR, THAT USER DOES NOT HAVE AN ACCOUNT." << std::endl;
        return false;
      } else {
        is_logged_in_ = true;

        // test the function that I made and stuff


        std::string trans = "";
        for(int i = 0; i < 41; i++){
          trans = trans + " ";
        }
        logged_in_name_ = session;
        trans.replace(0, 2, "10");
        trans.replace(3, session.length(), session);
        trans.replace(3 + 21, 5, "00000");
        trans.replace(24 + 6, 8, "00000.00");
        trans.replace(30 + 9, 1, "S");
        // Send this to a file
        std::cout << trans << std::endl;

        return true;
      }
    } else if(session == "admin"){
      is_logged_in_ = true;
      is_admin_ = true;
      std::string trans = "";
      for(int i = 0; i < 41; i++){
        trans = trans + " ";
      }


      trans.replace(0, 2, "10");
      trans.replace(3, 5, "admin");
      trans.replace(3 + 21, 5, "00000");
      trans.replace(24 + 6, 8, "00000.00");
      trans.replace(30 + 9, 1, "A");
      // Send this to a file
      std::cout << trans << std::endl;

      return true;
    }
    return false;
  } else {
    std::cout << "ERROR, YOU'RE ALREADY LOGGED IN!" << std::endl;
    return false;
  }
}

bool Commands::withdrawal() {

  if(is_logged_in_ == true){

    if(is_admin_ == true){
      std::cout << "Please enter the account holder's name: " << std::endl;
      char name[21] = { 0 };
      std::cin.getline(name, sizeof(name));
      std::cout << "Please enter the user's account number: " << std::endl;
      char num[6] = { 0 };
      std::cin.getline(num, sizeof(num));
      std::cout << "Please enter an amount to withdraw: " << std::endl;
      char amount[9] = { 0 };
      std::cin.getline(amount, sizeof(amount));
      std::vector<Account*> temp = accounts_[name];
      if(temp.empty()){
        std::cout << "ERROR, THAT USER DOES NOT HAVE AN ACCOUNT." << std::endl;
        return false;
      } else{

        bool owned_account = UserExists(name);
        Account* temp_account = GetAccount(name, atoi(num));

        if(owned_account == false || temp_account == nullptr){
          std::cout << "ERROR, THE ACCOUNT NUMBER DOESN'T MATCH THE ACCOUNT HOLDER'S NAME." << std::endl;
        } else {
          if(temp_account->balance > atof(amount)){

            float newBal = temp_account->balance - atof(amount);
            std::string trans = "";
            for(int i = 0; i < 41; i++){
              trans = trans + " ";
            }
            trans.replace(0, 2, "01");
            trans.replace(3, 20, name); // Why does this not work?
            trans.replace(3 + 21, 5, num);
            //trans.replace(24 + 6, 8, amount);
            //std::cout << amount < std::endl;
            std::cout << "\"" << trans << "\"" << std::endl;

          }
        }
      }

    } else{

    }


  } else {
    std::cout << "ERROR, YOU HAVE NOT LOGGED IN YET." << std::endl;
    return false;
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
  if(is_logged_in_ == true){
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
        std::cout << "Error, " << recipient_number << " is not a valid recipient." << std::endl;
        return false;
      }

      // get account
      recipient_account = GetAccount(recipient_name, recipient_number);
    }

    // TODO: after withdrawal is done



  } else {
    std::cout << "ERROR, YOU HAVE NOT LOGGED IN YET." << std::endl;
    return false;
  }
  return false;
}

bool Commands::paybill() {
  if(is_logged_in_ == true){

  } else {
    std::cout << "ERROR, YOU HAVE NOT LOGGED IN YET." << std::endl;
    return false;
  }
  return false;
}

bool Commands::deposit() {
  if(is_logged_in_ == true){

  } else {
    std::cout << "ERROR, YOU HAVE NOT LOGGED IN YET." << std::endl;
    return false;
  }
  return false;
}

bool Commands::create() {
  if(is_logged_in_ == true){

  } else {
    std::cout << "ERROR, YOU HAVE NOT LOGGED IN YET." << std::endl;
    return false;
  }
  return false;
}

bool Commands::delete_account(){
  if(is_logged_in_ == true){

  } else {
    std::cout << "ERROR, YOU HAVE NOT LOGGED IN YET." << std::endl;
    return false;
  }
  return false;
}

bool Commands::disable(){
  if(is_logged_in_ == true){

  } else {
    std::cout << "ERROR, YOU HAVE NOT LOGGED IN YET." << std::endl;
    return false;
  }
  return false;
}

bool Commands::changeplan(){
  if(is_logged_in_ == true){

  } else {
    std::cout << "ERROR, YOU HAVE NOT LOGGED IN YET." << std::endl;
    return false;
  }
  return false;
}

bool Commands::enable(){
  if(is_logged_in_ == true){

  } else {
    std::cout << "ERROR, YOU HAVE NOT LOGGED IN YET." << std::endl;
    return false;
  }
  return false;
}

bool Commands::logout(){
  if(is_logged_in_ == true){

  } else {
    std::cout << "ERROR, YOU HAVE NOT LOGGED IN YET." << std::endl;
    return false;
  }
  return false;
}
}
