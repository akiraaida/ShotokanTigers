#include "FrontEnd.hpp"
#include <iostream>
#include <string>
#include <string.h>

Commands::Commands(){
  isLoggedIn = false;
}

void Commands::setAccounts(std::map<std::string, std::vector<Account*> >&& accounts) {
  this->accounts = accounts;
}

std::string Commands::determineSession(){
  char session[20];
  std::cout << "Please enter your session type: " << std::endl;
  std::cin.getline(session, sizeof(session));

  if(strncmp(session, "admin", 20) == 0) {
    return "admin";
  } else if(strncmp(session, "standard", 20) == 0) {
    char logName[20];
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
  } else{
    std::cout << "ERROR, YOU'RE ALREADY LOGGED IN!" << std::endl;
    return false;
  }
}

bool Commands::withdrawal(std::string name, int account, double amount) {
  //todo
  return false;
}

bool Commands::transfer(std::string name, int account1, int account2, double amount) {
  //todo
  return false;
}

bool Commands::paybill(std::string name, int account, std::string company, double amount) {
  //todo
  return false;
}

bool Commands::deposit(std::string name, int account, double amount) {
  //todo
  return false;
}

bool Commands::create(std::string name, double amount) {
  //todo
  return false;
}
