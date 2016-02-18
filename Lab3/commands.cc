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
#include <ctime>

#define ERROR_MESSAGE_INVALID_SESSION "ERROR, SESSION TYPE IS NOT VALID."
#define ERROR_MESSAGE_ACCOUNTLESS_USER "ERROR, THAT USER DOES NOT HAVE AN ACCOUNT."
#define ERROR_MESSAGE_DOUBLE_LOGIN "ERROR, YOU'RE ALREADY LOGGED IN!"
#define ERROR_MESSAGE_NO_LOGIN "ERROR, YOU HAVE NOT LOGGED IN YET."
#define ERROR_MESSAGE_INVALID_ACCOUNT "ERROR, THAT ACCOUNT IS INVALID."
#define ERROR_MESSAGE_STOLEN_ACCOUNT "ERROR, THE ACCOUNT NUMBER DOESN'T MATCH THE ACCOUNT HOLDER'S NAME."
#define ERROR_BALANCE_INSUFFICIENT "ERROR, THE ACCOUNT DOES NOT HAVE SUFFICIENT FUNDS."
#define ERROR_ADMIN_PERMISSIONS "ERROR, YOU DO NOT HAVE THE CORRECT PRIVILEGES."
#define ERROR_DISABLED "ERROR, THAT ACCOUNT IS DISABLED."
#define ERROR_ENABLED "ERROR, THAT ACCOUNT IS ENABLED ALREADY."
#define ERROR_DELETED "ERROR, THAT ACCOUNT HAS BEEN DELETED."
#define ERROR_MESSAGE_HIT_TRANSFER_LIMIT "ERROR, THE VALUE ENTERED IS BEYOND THE TRANSFER LIMIT."
#define ERROR_INVALID_COMPANY "ERROR, THAT COMPANY IS NOT A VALID RECIPIENT."
#define ERROR_MESSAGE_HIT_PAYBILL_LIMIT "ERROR, THE VALUE ENTERED IS BEYOND THE PAYBILL LIMIT."
#define ERROR_ABOVE_MAX_INIT "ERROR, MAX INITIAL BALANCE EXCEEDED."
#define ERROR_NEW "ERROR, THAT ACCOUNT HAS BEEN NEWLY CREATED."

#define PROMPT_ENTER_SESSION_TYPE "Please enter your session type: "
#define PROMPT_ENTER_LOGIN_NAME "Please enter a login name: "
#define PROMPT_ENTER_CUSTOMER_NAME "Please enter the account holder's name: "
#define PROMPT_ENTER_ACCOUNT_NUMBER "Please enter the user's account number: "
#define PROMPT_TRANSFER_SOURCE "Please enter the transferring account's number: "
#define PROMPT_TRANSFER_TARGET "Please enter the recipient account's number: "
#define PROMPT_TRANSFER_VALUE "Please enter an amount to transfer: "
#define PROMPT_WITHDRAWAL_VALUE "Please enter an amount to withdraw: "
#define PROMPT_DEPOSIT_VALUE "Please enter an amount to deposit: "
#define PROMPT_PAYBILL_VALUE "Please enter an amount to pay: "
#define PROMPT_PAYBILL_RECIPIENT "Please enter a recipient to be paid: "
#define PROMPT_INIT_BALANCE "Please enter an initial balance: "

#define SUCCESS_WITHDRAWAL "The withdrawal transaction has completed."
#define SUCCESS_DEPOSIT "The deposit transaction has completed."
#define SUCCESS_DISABLE "The disable transaction has completed."
#define SUCCESS_ENABLE "The enable transaction has completed."
#define SUCCESS_TO_STUDENT "Specified account is now a student account."
#define SUCCESS_TO_NONSTUDENT "Specified account is now a non-student account."
#define SUCCESS_DELETE "Specified account has been deleted."
#define SUCCESS_TRANSFER "Amount has been transfered successfully."

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

std::string Commands::FitStringToSpace(std::string string, size_t size,
                             char fluff, bool align_right) {
  int offset = string.size() - size;
  if(offset > 0) {
    // truncate
    int start_point = align_right ? offset : 0;
    return string.substr(start_point, size);
  } else {
    // pad out
    int start_point = align_right ? 0 : string.size();
    return string.insert(start_point, -offset, fluff);
  }
}


void Commands::PushTransactionRecord(int code, std::string name,
                                     int account_number, double money,
                                     std::string misc) {
  // figure out the money string
  char money_string_buff[16] = { 0 };
  sprintf(money_string_buff, "%.2f", money);
  std::string money_string(money_string_buff);
  int chop_point = money_string.size() - 8;
  if(chop_point > 0)
    money_string = money_string.substr(chop_point, 8);

  // throw the transaction together
  std::string transaction = FitStringToSpace(std::to_string(code), 2, '0') + " "
                            + FitStringToSpace(name, 20, ' ', false) + " "
                            + FitStringToSpace(std::to_string(account_number),
                                5, '0') + " "
                            + FitStringToSpace(money_string, 8, '0') + " "
                            + FitStringToSpace(misc, 2, ' ', false);

  // test output
  std::cout << "pushed \"" << transaction << "\" size: " << transaction.size() <<  std::endl;

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
  if(!CheckLogin()) {
    return false;
  }
  std::string name = PromptForAccountHolderIfUnknown();
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
  }
  bool owned_account = UserExists(name);
  Account* temp_account = GetAccount(name, atoi(num));
  if(owned_account == false || temp_account == nullptr) {
    std::cout << ERROR_MESSAGE_STOLEN_ACCOUNT << std::endl;
    return false;
  }
  if(temp_account->is_deleted) {
    std::cout << ERROR_DELETED << std::endl;
    return false;
  } else if (!temp_account->is_active) {
    std::cout << ERROR_DISABLED << std::endl;
    return false;
  } else if (temp_account->is_new){
    std::cout << ERROR_NEW << std::endl;
    return false;
  }
  float transaction_charge = is_admin_ ? 0.0 : GetTransactionCharge(name, atoi(num));
  float debit = atof(amount) + transaction_charge;
  if(temp_account->balance < debit || CheckUnit(atof(amount)) == false) {
    std::cout << ERROR_BALANCE_INSUFFICIENT << std::endl; // Very generalized error message atm, may want to break the error cases down?
    return false;                                         // Errors for not mod 5/10/20/100 and not enough money
  }
  PushTransactionRecord(1, name, atoi(num), debit);
  std::cout << SUCCESS_WITHDRAWAL << std::endl;
  temp_account->withdrawal_limit_remaining = temp_account->withdrawal_limit_remaining - atof(amount);
  return true;
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
  if(CheckLogin()) {
    // get name
    std::string name = PromptForAccountHolderIfUnknown();

    // check name
    if(!UserExists(name)) {
      std::cout << ERROR_MESSAGE_ACCOUNTLESS_USER << std::endl;
      return false;
    }

    // get number
    int number;
    Account* account;
    {
      std::cout << PROMPT_TRANSFER_SOURCE << std::endl;
      char num[6];
      std::cin.getline(num, sizeof(num));
      number = std::stoi(num);

      // check name against account number
      account = GetAccount(name, number);
      if(account == nullptr) {
        std::cout << ERROR_MESSAGE_STOLEN_ACCOUNT << std::endl;
        return false;
      }
    }

    // Ask for next name
    std::string recipient_name;
    int recipient_number;
    Account* recipient_account;
    {
      std::cout << PROMPT_TRANSFER_TARGET << std::endl;
      char num[6];
      std::cin.getline(num, sizeof(num));
      recipient_number = std::stoi(num);

      // find name corresponding
      recipient_name = GetAccountOwner(recipient_number);
      if(recipient_name.empty() || recipient_name == name) {
        std::cout << ERROR_MESSAGE_INVALID_ACCOUNT << std::endl;
        return false;
      }

      // get account
      recipient_account = GetAccount(recipient_name, recipient_number);
    }

    // get amount to transfer
    double amount;
    {
      std::cout << PROMPT_TRANSFER_VALUE << std::endl;
      char num[9];
      std::cin.getline(num, sizeof(num));
      amount = std::stod(num);
    }

    // check transfer limit
    double charge = is_admin_ ? 0.0 : GetTransactionCharge(name, number);
    if(account->transfer_limit_remaining < amount) {
      std::cout << ERROR_MESSAGE_HIT_TRANSFER_LIMIT << std::endl;
      std::cout << account->transfer_limit_remaining << "<" << amount << std::endl;
      return false;
    }

    // check transfer amount
    if(account->balance < (amount + charge)) {
      std::cout << ERROR_BALANCE_INSUFFICIENT << std::endl;
      return false;
    }

    // perform deduction
    account->balance -= amount + charge;
    recipient_account->balance += amount;
    account->transfer_limit_remaining -= amount;

    // create transaction records
    PushTransactionRecord(2, name, number, amount);
    PushTransactionRecord(2, recipient_name, recipient_number, amount);
    PushTransactionRecord(1, name, number, charge);

    // did it
    std::cout << SUCCESS_TRANSFER << std::endl;

    // done
    return true;
  } else {
    return false;
  }
}

bool Commands::paybill() {
  // check if logged in
  if(!CheckLogin()) {
    return false;
  }

  // get name
  std::string name = PromptForAccountHolderIfUnknown();
  if(is_admin_ && !UserExists(name)) {
    std::cout << ERROR_MESSAGE_ACCOUNTLESS_USER << std::endl;
    return false;
  }

  // get account number
  int account_number;
  {
    std::cout << PROMPT_ENTER_ACCOUNT_NUMBER << std::endl;
    char num[6];
    std::cin.getline(num, sizeof(num));
    account_number = std::stoi(num);
  }

  // get account
  Account* account = GetAccount(name, account_number);
  if(account == nullptr) {
    std::cout << ERROR_MESSAGE_STOLEN_ACCOUNT << std::endl;
    return false;
  }

  // get company name
  std::string company;
  {
    std::cout << PROMPT_PAYBILL_RECIPIENT << std::endl;
    char buffer[256];
    std::cin.getline(buffer, sizeof(buffer));
    company = std::string(buffer);

  }

  // check company name
  if(account->paybill_limit_remaining.find(company)
      == account->paybill_limit_remaining.end()) {
    std::cout << ERROR_INVALID_COMPANY << std::endl;
    return false;
  }

  // get amount to transfer
  double amount;
  {
    std::cout << PROMPT_PAYBILL_VALUE << std::endl;
    char num[9];
    std::cin.getline(num, sizeof(num));
    amount = std::stod(num);
  }

  // check transfer limit
  if(account->paybill_limit_remaining[company] < amount) {
    std::cout << ERROR_MESSAGE_HIT_PAYBILL_LIMIT << std::endl;
    return false;
  }

  // check balance
  double charge = is_admin_ ? 0.0 : GetTransactionCharge(name, account_number);
  if(account->balance < (amount + charge)) {
    std::cout << ERROR_BALANCE_INSUFFICIENT << std::endl;
    return false;
  }

  // do it
  account->balance -= amount + charge;
  account->paybill_limit_remaining[company] -= amount;

  // print out transaction
  PushTransactionRecord(3, name, account_number, amount, company);
  PushTransactionRecord(1, name, account_number, charge);

  // good
  return true;
}

bool Commands::deposit() {
  if(!CheckLogin()) {
    return false;
  }
  std::string name = PromptForAccountHolderIfUnknown();
  std::cout << PROMPT_ENTER_ACCOUNT_NUMBER << std::endl;
  char num[6] = { 0 };
  std::cin.getline(num, sizeof(num));
  std::cout << PROMPT_DEPOSIT_VALUE << std::endl;
  char amount[9] = { 0 };
  std::cin.getline(amount, sizeof(amount));
  std::vector<Account*> temp = accounts_[name];
  if(temp.empty()) {
    std::cout << ERROR_MESSAGE_ACCOUNTLESS_USER << std::endl;
    return false;
  }
  bool owned_account = UserExists(name);
  Account* temp_account = GetAccount(name, atoi(num));
  if(owned_account == false || temp_account == nullptr) {
    std::cout << ERROR_MESSAGE_STOLEN_ACCOUNT << std::endl;
    return false;
  }
  if(!temp_account->is_active) {
    std::cout << ERROR_DISABLED << std::endl;
    return false;
  } else if (temp_account->is_deleted) {
    std::cout << ERROR_DELETED << std::endl;
    return false;
  } else if (temp_account->is_new){
    std::cout << ERROR_NEW << std::endl;
    return false;
  }
  float transaction_charge = is_admin_ ? 0.0 : GetTransactionCharge(name, atoi(num));
  if(temp_account->balance + atof(amount) < transaction_charge) {
    std::cout << ERROR_BALANCE_INSUFFICIENT << std::endl;
    return false;
  }
  PushTransactionRecord(4, name, atoi(num), atof(amount));
  if(!is_admin_){
    PushTransactionRecord(1, name, atoi(num), transaction_charge);
  }
  std::cout << SUCCESS_DEPOSIT << std::endl;
  return true;
}

bool Commands::create() {
  if(!CheckLogin(1)) {
    return false;
  }
  std::string name = PromptForAccountHolderIfUnknown();
  char init[9] = { 0 };
  std::cout << PROMPT_INIT_BALANCE << std::endl;
  std::cin.getline(init, sizeof(init));
  if(atof(init) > 99999.99){
    std::cout << ERROR_ABOVE_MAX_INIT << std::endl;
    return false;
  }
  Account* new_account = new Account();
  new_account->number = GenerateNum();
  new_account->is_active = 1;
  new_account->balance = atof(init);
  new_account->is_student_plan = 0;
  new_account->is_deleted = 0;
  new_account->is_new = 1;
  accounts_[name].push_back(new_account);
  PushTransactionRecord(5, name, new_account->number, atof(init));
  return false;
}

int Commands::GenerateNum(){
  srand(time(0));
  int rnd;
  bool duplicate = true;
  while(duplicate == true){
    duplicate = false;
    rnd = rand() % 99999 + 1;
    std::map<std::string, std::vector<Account*> >::iterator it;
    for(it=accounts_.begin(); it != accounts_.end(); it++) {
      for(int i = 0; i < it->second.size(); i++){
        if(rnd == it->second[i]->number){
          duplicate = true;
        }
      }
    }
  }
  return rnd;
}

bool Commands::delete_account() {
  if(!CheckLogin(1)) {
    return false;
  }
  std::string name = PromptForAccountHolderIfUnknown();
  std::cout << PROMPT_ENTER_ACCOUNT_NUMBER << std::endl;
  char num[6] = { 0 };
  std::cin.getline(num, sizeof(num));
  std::vector<Account*> temp = accounts_[name];
  if(temp.empty()) {
    std::cout << ERROR_MESSAGE_ACCOUNTLESS_USER << std::endl;
    return false;
  }
  bool owned_account = UserExists(name);
  Account* temp_account = GetAccount(name, atoi(num));
  if(owned_account == false || temp_account == nullptr) {
    std::cout << ERROR_MESSAGE_STOLEN_ACCOUNT << std::endl;
    return false;
  }
  if(temp_account->is_deleted) {
    std::cout << ERROR_DELETED << std::endl;
    return false;
  } else if (temp_account->is_new){
    std::cout << ERROR_NEW << std::endl;
    return false;
  }
  PushTransactionRecord(6, name, atoi(num));
  temp_account->is_deleted = true;
  std::cout << SUCCESS_DELETE << std::endl;
  return true;
}

bool Commands::disable() {
  if(!CheckLogin(1)) {
    return false;
  }
  std::string name = PromptForAccountHolderIfUnknown();
  std::cout << PROMPT_ENTER_ACCOUNT_NUMBER << std::endl;
  char num[6] = { 0 };
  std::cin.getline(num, sizeof(num));
  std::vector<Account*> temp = accounts_[name];
  if(temp.empty()) {
    std::cout << ERROR_MESSAGE_ACCOUNTLESS_USER << std::endl;
    return false;
  }
  bool owned_account = UserExists(name);
  Account* temp_account = GetAccount(name, atoi(num));
  if(owned_account == false || temp_account == nullptr) {
    std::cout << ERROR_MESSAGE_STOLEN_ACCOUNT << std::endl;
    return false;
  }
  if(temp_account->is_deleted) {
    std::cout << ERROR_DELETED << std::endl;
    return false;
  } else if (!temp_account->is_active){
    std::cout << ERROR_DISABLED << std::endl;
    return false;
  } else if (temp_account->is_new){
    std::cout << ERROR_NEW << std::endl;
    return false;
  }
  PushTransactionRecord(7, name, atoi(num));
  temp_account->is_active = false;
  std::cout << SUCCESS_DISABLE << std::endl;
  return true;
}

bool Commands::changeplan() {
  if(!CheckLogin(1)) {
    return false;
  }
  std::string name = PromptForAccountHolderIfUnknown();
  std::cout << PROMPT_ENTER_ACCOUNT_NUMBER << std::endl;
  char num[6] = { 0 };
  std::cin.getline(num, sizeof(num));
  std::vector<Account*> temp = accounts_[name];
  if(temp.empty()) {
    std::cout << ERROR_MESSAGE_ACCOUNTLESS_USER << std::endl;
    return false;
  }
  bool owned_account = UserExists(name);
  Account* temp_account = GetAccount(name, atoi(num));
  if(owned_account == false || temp_account == nullptr) {
    std::cout << ERROR_MESSAGE_STOLEN_ACCOUNT << std::endl;
    return false;
  }
  if(temp_account->is_deleted) {
    std::cout << ERROR_DELETED << std::endl;
    return false;
  } else if (temp_account->is_active){
    std::cout << ERROR_DISABLED << std::endl;
    return false;
  } else if (temp_account->is_new){
    std::cout << ERROR_NEW << std::endl;
    return false;
  }
  if(temp_account->is_student_plan){
    PushTransactionRecord(8, name, atoi(num));
    std::cout << SUCCESS_TO_NONSTUDENT << std::endl;
  } else {
    PushTransactionRecord(8, name, atoi(num));
    std::cout << SUCCESS_TO_STUDENT << std::endl;
  }
  return true;
}

bool Commands::enable() {
  if(!CheckLogin(1)) {
    return false;
  }
  std::string name = PromptForAccountHolderIfUnknown();
  std::cout << PROMPT_ENTER_ACCOUNT_NUMBER << std::endl;
  char num[6] = { 0 };
  std::cin.getline(num, sizeof(num));
  std::vector<Account*> temp = accounts_[name];
  if(temp.empty()) {
    std::cout << ERROR_MESSAGE_ACCOUNTLESS_USER << std::endl;
    return false;
  }
  bool owned_account = UserExists(name);
  Account* temp_account = GetAccount(name, atoi(num));
  if(owned_account == false || temp_account == nullptr) {
    std::cout << ERROR_MESSAGE_STOLEN_ACCOUNT << std::endl;
    return false;
  }
  if(temp_account->is_deleted) {
    std::cout << ERROR_DELETED << std::endl;
    return false;
  } else if (temp_account->is_active){
    std::cout << ERROR_ENABLED << std::endl;
    return false;
  } else if (temp_account->is_new){
    std::cout << ERROR_NEW << std::endl;
    return false;
  }
  PushTransactionRecord(9, name, atoi(num));
  temp_account->is_active = true;
  std::cout << SUCCESS_ENABLE << std::endl;
  return true;
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

std::string Commands::PromptForAccountHolder() {
  char name[21] = { 0 };
  std::cout << PROMPT_ENTER_CUSTOMER_NAME << std::endl;
  std::cin.getline(name, sizeof(name));
  return std::string(name);
}

std::string Commands::PromptForAccountHolderIfUnknown() {
  return is_admin_ ? PromptForAccountHolder() : logged_in_name_;
}


bool Commands::CheckLogin(bool admins_only) {
  if(!is_logged_in_) {
    std::cout << ERROR_MESSAGE_NO_LOGIN << std::endl;
    return false;
  } else if (admins_only && !is_admin_) {
    std::cout << ERROR_ADMIN_PERMISSIONS << std::endl;
    return false;
  } else {
    return true;
  }
}


double Commands::GetTransactionCharge(std::string name, int account_number) {
  Account* account = GetAccount(name, account_number);
  if(account->is_student_plan){
    return 0.05;
  } else {
    return 0.1;
  }
}
}
