#include "Commands.hpp"
#include <iostream>
#include <string>
#include <cstring>

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


void Commands::pushTransactionRecord(int code, std::string name, int accountNumber, double money, std::string misc) {
  std::string trans;
  misc = "";
  std::string codeStr;
  {
    codeStr = std::to_string(code);
    int nzeroes = 2 - codeStr.size();
    codeStr.insert(0, nzeroes, '0');
  }
  std::string moneyStr;
  {
    char moneyStrBuff[16] = { 0 };
    sprintf(moneyStrBuff, "%5.2f", money);
    std::string moneyStrBuff2(moneyStrBuff);
    int nzeroes = 8 - moneyStrBuff2.length();
    nzeroes = nzeroes < 0 ? 0 : nzeroes;
    moneyStr.insert(0, nzeroes, '0');
    moneyStr = moneyStr + moneyStrBuff2; // fix later
  }
  std::string accountStr;
  {
    int nzeroes = 5 - std::to_string(accountNumber).size();
    accountStr.insert(0, nzeroes, '0');
    accountStr = accountStr + std::to_string(accountNumber);
    std::cout << accountStr << std::endl;
  }

  trans.insert(0, 41, ' ');
  trans.replace(0, 2, codeStr);
  trans.replace(3, name.length(), name);
  trans.replace(3 + 21, 5, accountStr);
  trans.replace(24 + 6, 8, moneyStr);
  trans.replace(39, 2, misc);
  std::cout << "\"" << trans << "\" " << trans.size() <<  std::endl;

  // push
  transactionOutput.push_front(trans);
}


bool Commands::login() {

  if(isLoggedIn == false){
    std::string session;
    session = determineSession();

    pushTransactionRecord(10);
    if(session != "" && session != "admin") {
      std::vector<Account*> temp;
      temp = accounts[session];
      if(temp.empty()){
        std::cout << "ERROR, THAT USER DOES NOT HAVE AN ACCOUNT." << std::endl;
        return false;
      } else {
        isLoggedIn = true;

        // test the function that I made and stuff


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
      std::vector<Account*> temp;
      temp = accounts[name];
      if(temp.empty()){
        std::cout << "ERROR, THAT USER DOES NOT HAVE AN ACCOUNT." << std::endl;
        return false;
      } else{

        bool ownedAcnt = userExists(name);
        Account* tempAcnt = getAccount(name, atoi(num));

        if(ownedAcnt == false || tempAcnt == nullptr){
          std::cout << "ERROR, THE ACCOUNT NUMBER DOESN'T MATCH THE ACCOUNT HOLDER'S NAME." << std::endl;
        } else {
          if(tempAcnt->balance > atof(amount)){

            float newBal = tempAcnt->balance - atof(amount);
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

bool Commands::userExists(std::string name) {
  std::vector<Account*> record = accounts[name];
  return !record.empty();
}

std::string Commands::getAccountOwner(int number) {
  for(std::pair<const std::string, std::vector<Account*> > pair : accounts) {
    for(Account* account : pair.second) {
      if(account->number == number) {
        return pair.first;
      }
    }
  }
  return "";
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

      // check name
      if(!userExists(name)) {
        std::cout << "Error: user " << name << " does not exist" << std::endl;
        return false;
      }

    } else {
      // use stored name
      name = loggedInName;
    }

    // get number
    int number;
    {
      std::cout << "Please enter account to transfer from: " << std::endl;
      char num[6];
      std::cin.getline(num, sizeof(num));
      number = std::stoi(num);

      // check name against account number
      Account* account = getAccount(name, number);
      if(account == nullptr) {
        std::cout << "Error, account " << name << ":" << number
            << " was not found" << std::endl;
        return false;
      }
    }



    // Ask for next name
    std::string recipientName;
    int recipientNumber;
    Account* recipientAccount;
    {
      std::cout << "Please enter account to transfer to: " << std::endl;
      char num[6];
      std::cin.getline(num, sizeof(num));
      recipientNumber = std::stoi(num);

      // find name corresponding
      recipientName = getAccountOwner(recipientNumber);
      if(recipientName.empty() || recipientName == name) {
        std::cout << "Error, " << recipientNumber << " is not a valid recipient." << std::endl;
        return false;
      }

      // get account
      recipientAccount = getAccount(recipientName, recipientNumber);
    }

    // TODO: after withdrawal is done



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
