#include "FrontEnd.hpp"
#include <iostream>
#include <string>
#include <string.h>

Commands::Commands(){
  isLoggedIn = false;
  isAdmin = false;
}

void Commands::setAccounts(std::map<std::string, std::vector<Account*> >&& accounts) {
  this->accounts = accounts;
}

std::string Commands::determineSession(){
  char session[21];
  std::cout << "Please enter your session type: " << std::endl;
  std::cin.getline(session, sizeof(session));

  if(strncmp(session, "admin", 20) == 0) {
    return "admin";
  } else if(strncmp(session, "standard", 20) == 0) {
    char logName[21];
    std::cout << "Please enter a login name: " << std::endl;
    std::cin.getline(logName, sizeof(logName));
    return logName;
  } else{
    std::cout << "ERROR, SESSION TYPE IS NOT VALID." << std::endl;
    return "";
  }
}

bool Commands::login() {

  if(isLoggedIn == false){
    std::string session;
    session = determineSession();

    if(session != "" && session != "admin") {
      std::vector<Account*> temp;
      temp = accounts[session];
      if(temp.empty()){
        std::cout << "ERROR, THAT USER DOES NOT HAVE AN ACCOUNT." << std::endl;
        return false;
      } else {
        isLoggedIn = true;

        std::string trans = "";
        for(int i = 0; i < 41; i++){
          trans = trans + " ";
        }
        loggedInName = session;
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
      isLoggedIn = true;
      isAdmin = true;
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

  if(isLoggedIn == true){

    if(isAdmin == true){
      std::cout << "Please enter the account holder's name: " << std::endl;
      char name[21];
      std::cin.getline(name, sizeof(name));
      std::cout << "Please enter the user's account number: " << std::endl;
      char num[6];
      std::cin.getline(num, sizeof(num));
      std::cout << "Please enter an amount to withdraw: " << std::endl;
      char amount[9];
      std::cin.getline(amount, sizeof(amount));
      std::cout << amount << std::endl;

    } else{

    }


  } else {
    std::cout << "ERROR, YOU HAVE NOT LOGGED IN YET." << std::endl;
    return false;
  }

  return false;
}

bool Commands::userExists(std::string name) {
  std::vector<Account*> record = accounts[name];
  return !record.empty();
}

Account* Commands::getAccount(std::string name, int number) {
  // lookup user in directory
  std::vector<Account*> record = accounts[name];
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
  if(isLoggedIn == true){
    // get name
    std::string name;
    if(isAdmin) {
      // retrieve name
      std::cout << "Please enter the account holder's name: " << std::endl;
      char buffer[20];
      std::cin.getline(buffer, sizeof(buffer));
      name = buffer;
    } else {
      // use stored name
      name = loggedInName;
    }
    std::cout << name << " exists? " << userExists(name) << std::endl;

    // get number
    int account;
    {
      std::cout << "Please enter the user's account number: " << std::endl;
      char num[6];
      std::cin.getline(num, sizeof(num));
      account = std::stoi(num);
    }

    // check name against account number
    std::cout << "account " << account << "found? " << (getAccount(name, account) != nullptr) << std::endl;

  } else {
    std::cout << "ERROR, YOU HAVE NOT LOGGED IN YET." << std::endl;
    return false;
  }
  return false;
}

bool Commands::paybill() {
  if(isLoggedIn == true){

  } else {
    std::cout << "ERROR, YOU HAVE NOT LOGGED IN YET." << std::endl;
    return false;
  }
  return false;
}

bool Commands::deposit() {
  if(isLoggedIn == true){

  } else {
    std::cout << "ERROR, YOU HAVE NOT LOGGED IN YET." << std::endl;
    return false;
  }
  return false;
}

bool Commands::create() {
  if(isLoggedIn == true){

  } else {
    std::cout << "ERROR, YOU HAVE NOT LOGGED IN YET." << std::endl;
    return false;
  }
  return false;
}

bool Commands::deleteAccount(){
  if(isLoggedIn == true){

  } else {
    std::cout << "ERROR, YOU HAVE NOT LOGGED IN YET." << std::endl;
    return false;
  }
  return false;
}

bool Commands::disable(){
  if(isLoggedIn == true){

  } else {
    std::cout << "ERROR, YOU HAVE NOT LOGGED IN YET." << std::endl;
    return false;
  }
  return false;
}

bool Commands::changePlan(){
  if(isLoggedIn == true){

  } else {
    std::cout << "ERROR, YOU HAVE NOT LOGGED IN YET." << std::endl;
    return false;
  }
  return false;
}

bool Commands::enable(){
  if(isLoggedIn == true){

  } else {
    std::cout << "ERROR, YOU HAVE NOT LOGGED IN YET." << std::endl;
    return false;
  }
  return false;
}

bool Commands::logout(){
  if(isLoggedIn == true){

  } else {
    std::cout << "ERROR, YOU HAVE NOT LOGGED IN YET." << std::endl;
    return false;
  }
  return false;
}
