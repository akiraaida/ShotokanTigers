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

#include <cassert>
#include <cstring>
#include <ctime>
#include <math.h>

#include <iostream>
#include <string>

#include "account_status.h"
#include "console_input.h"
#include "transaction_io.h"

#define ERROR_MESSAGE_INVALID_SESSION "ERROR, SESSION TYPE IS NOT VALID."
#define ERROR_MESSAGE_ACCOUNTLESS_USER "ERROR, THAT USER DOES NOT HAVE AN ACCOUNT."
#define ERROR_MESSAGE_DOUBLE_LOGIN "ERROR, YOU'RE ALREADY LOGGED IN!"
#define ERROR_MESSAGE_NO_LOGIN "ERROR, YOU HAVE NOT LOGGED IN YET."
#define ERROR_MESSAGE_INVALID_ACCOUNT "ERROR, THAT ACCOUNT IS INVALID."
#define ERROR_MESSAGE_STOLEN_ACCOUNT "ERROR, THE ACCOUNT NUMBER DOESN'T MATCH THE ACCOUNT HOLDER'S NAME."
#define ERROR_INVALID_INPUT "ERROR, INVALID INPUT."
#define ERROR_BALANCE_INSUFFICIENT "ERROR, THE ACCOUNT DOES NOT HAVE SUFFICIENT FUNDS."
#define ERROR_ADMIN_PERMISSIONS "ERROR, YOU DO NOT HAVE THE CORRECT PRIVILEGES."
#define ERROR_DISABLED "ERROR, THAT ACCOUNT IS DISABLED."
#define ERROR_ENABLED "ERROR, THAT ACCOUNT IS ENABLED ALREADY."
#define ERROR_DELETED "ERROR, THAT ACCOUNT HAS BEEN DELETED."
#define ERROR_MESSAGE_HIT_TRANSFER_LIMIT "ERROR, THE VALUE ENTERED IS BEYOND THE TRANSFER LIMIT."
#define ERROR_MIN_INPUT "ERROR, INPUT VALUE IS TOO SMALL."
#define ERROR_MAX_INPUT "ERROR, INPUT VALUE IS TOO LARGE."
#define ERROR_INVALID_COMPANY "ERROR, THAT COMPANY IS NOT A VALID RECIPIENT."
#define ERROR_MESSAGE_HIT_PAYBILL_LIMIT "ERROR, THE VALUE ENTERED IS BEYOND THE PAYBILL LIMIT."
#define ERROR_ABOVE_MAX_INIT "ERROR, MAX INITIAL BALANCE EXCEEDED."

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

void Commands::SetTransactionPath(const std::string& path) {
  transactions_file_ = path;
}

std::string Commands::DetermineSession() {
  std::cout << PROMPT_ENTER_SESSION_TYPE  << std::endl;
  std::string session = ConsoleInput::GetString();

  if (session.compare("admin") == 0) {
    return "admin";
  } else if (session.compare("standard") == 0) {
    std::cout << PROMPT_ENTER_LOGIN_NAME << std::endl;
    std::string login_name = ConsoleInput::GetString();
    return login_name;
  } else {
    std::cout << ERROR_MESSAGE_INVALID_SESSION << std::endl;
    return "";
  }
}

std::string Commands::FitStringToSpace(std::string string, size_t size,
                             char fluff, bool align_right) {
  int offset = string.size() - size;
  if (offset > 0) {
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
  if (chop_point > 0)
    money_string = money_string.substr(chop_point, 8);

  // throw the transaction together
  std::string transaction = FitStringToSpace(std::to_string(code), 2, '0') + " "
                            + FitStringToSpace(name, 20, ' ', false) + " "
                            + FitStringToSpace(std::to_string(account_number),
                                5, '0') + " "
                            + FitStringToSpace(money_string, 8, '0') + " "
                            + FitStringToSpace(misc, 2, ' ', false);

  // push
  transaction_output_.push_back(transaction);
}


void Commands::login() {
  if (is_logged_in_ == false) {
    std::string session = DetermineSession();

    if (session != "" && session != "admin") {
      std::vector<Account*> temp = accounts_[session];
      if (temp.empty()) {
        std::cout << ERROR_MESSAGE_ACCOUNTLESS_USER << std::endl;
        return;
      } else {
        is_logged_in_ = true;
        logged_in_name_ = session;
        PushTransactionRecord(10, session, 00000, 00000.00, "S ");
        return;
      }
    } else if (session == "admin") {
      is_logged_in_ = true;
      is_admin_ = true;
      PushTransactionRecord(10, "admin", 00000, 00000.00, "A ");
      return;
    }
    return;
  } else {
    std::cout << ERROR_MESSAGE_DOUBLE_LOGIN << std::endl;
    return;
  }
}

void Commands::withdrawal() {
  if (!CheckLogin()) {
    return;
  }
  std::string name = PromptForAccountHolderIfUnknown();
  std::cout << PROMPT_ENTER_ACCOUNT_NUMBER << std::endl;
  int number = ConsoleInput::GetInteger();
  std::cout << PROMPT_WITHDRAWAL_VALUE << std::endl;
  double amount = ConsoleInput::GetDouble();
  std::vector<Account*> temp = accounts_[name];
  if (temp.empty()) {
    std::cout << ERROR_MESSAGE_ACCOUNTLESS_USER << std::endl;
    return;
  }
  bool owned_account = UserExists(name);
  Account* temp_account = GetAccount(name, number);
  if (owned_account == false || temp_account == nullptr) {
    std::cout << ERROR_MESSAGE_STOLEN_ACCOUNT << std::endl;
    return;
  }
  if (temp_account->is_deleted) {
    std::cout << ERROR_DELETED << std::endl;
    return;
  } else if (!temp_account->is_active) {
    std::cout << ERROR_DISABLED << std::endl;
    return;
  }
  float transaction_charge =
      is_admin_ ? 0.0 : GetTransactionCharge(name, number);
  float debit = amount + transaction_charge;
  if (temp_account->balance < debit || CheckUnit(amount) == false) {
    std::cout << ERROR_BALANCE_INSUFFICIENT << std::endl; // Very generalized error message atm, may want to break the error cases down?
    return;                                         // Errors for not mod 5/10/20/100 and not enough money
  }
  PushTransactionRecord(1, name, number, debit);
  std::cout << SUCCESS_WITHDRAWAL << std::endl;
  temp_account->withdrawal_limit_remaining
      = temp_account->withdrawal_limit_remaining - amount;
  return;
}

bool Commands::CheckUnit(double amount) {
  if (fmod(amount, 5) == 0 || fmod(amount, 10) == 0 || fmod(amount, 20) == 0
      || fmod(amount, 100) == 0) {
    return true;
  }
  return false;
}

bool Commands::UserExists(std::string name) {
  std::vector<Account*> record = accounts_[name];
  return !record.empty();
}

std::string Commands::GetAccountOwner(int number) {
  for (std::pair<const std::string, std::vector<Account*> > pair : accounts_) {
    for (Account* account : pair.second) {
      if (account->number == number) {
        return pair.first;
      }
    }
  }
  return "";
}

Account* Commands::GetAccount(std::string name, int number) {
  // lookup user in directory
  std::vector<Account*> record = accounts_[name];
  if (!record.empty()) {
    for (Account* account : record) {
      if (account->number == number) {
        return account;
      }
    }
  }
  // failed to lookup account
  return nullptr;
}

void Commands::transfer() {
  if (CheckLogin()) {
    // get name
    std::string name = PromptForAccountHolderIfUnknown();

    // check name
    if (!UserExists(name)) {
      std::cout << ERROR_MESSAGE_ACCOUNTLESS_USER << std::endl;
      return;
    }

    // get number
    std::cout << PROMPT_TRANSFER_SOURCE << std::endl;
    int number = ConsoleInput::GetInteger();

    // check name against account number
    Account* account = GetAccount(name, number);
    {
      int status = AccountStatus::QueryAccountStatus(account);
      if (status != AccountStatus::kActiveAccount) {
        std::cout << AccountStatus::GetErrorMessage(status) << std::endl;
        return;
      }
    }

    // Ask for next name
    std::string recipient_name;
    int recipient_number;
    Account* recipient_account;
    {
      std::cout << PROMPT_TRANSFER_TARGET << std::endl;
      recipient_number = ConsoleInput::GetInteger();

      // find name corresponding
      recipient_name = GetAccountOwner(recipient_number);
      if (recipient_name.empty() || recipient_name == name) {
        std::cout <<
                  AccountStatus::GetErrorMessage(AccountStatus::kAccountNoExist)
                  << std::endl;
        return;
      }

      // get account
      recipient_account = GetAccount(recipient_name, recipient_number);

      // check recieving account status
      int status = AccountStatus::QueryAccountStatus(recipient_account);
      if (status != AccountStatus::kActiveAccount) {
        std::cout << AccountStatus::GetErrorMessage(status) << std::endl;
        return;
      }

    }

    // get amount to transfer
    std::cout << PROMPT_TRANSFER_VALUE << std::endl;
    double amount = ConsoleInput::GetDouble();

    // check transfer limit
    double charge = is_admin_ ? 0.0 : GetTransactionCharge(name, number);
    if (account->transfer_limit_remaining < amount) {
      std::cout << ERROR_MESSAGE_HIT_TRANSFER_LIMIT << std::endl;
      std::cout << account->transfer_limit_remaining << "<" << amount
                << std::endl;
      return;
    }

    // check transfer amount
    if (account->balance < (amount + charge)) {
      std::cout << ERROR_BALANCE_INSUFFICIENT << std::endl;
      return;
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
    return;
  } else {
    return;
  }
}

void Commands::paybill() {
  // check if logged in
  if (!CheckLogin()) {
    return;
  }

  // get name
  std::string name = PromptForAccountHolderIfUnknown();
  if (is_admin_ && !UserExists(name)) {
    std::cout << ERROR_MESSAGE_ACCOUNTLESS_USER << std::endl;
    return;
  }

  // get account number
  std::cout << PROMPT_ENTER_ACCOUNT_NUMBER << std::endl;
  int account_number = ConsoleInput::GetInteger();

  // get account
  Account* account = GetAccount(name, account_number);
  int account_status = AccountStatus::QueryAccountStatus(account);
  if (account_status != AccountStatus::kActiveAccount) {
    std::cout << AccountStatus::GetErrorMessage(account_status) << std::endl;
    return;
  }

  // get company name
  std::cout << PROMPT_PAYBILL_RECIPIENT << std::endl;
  std::string company = ConsoleInput::GetString();

  // check company name
  if (account->paybill_limit_remaining.find(company)
      == account->paybill_limit_remaining.end()) {
    std::cout << ERROR_INVALID_COMPANY << std::endl;
    return;
  }

  // get amount to transfer
  std::cout << PROMPT_PAYBILL_VALUE << std::endl;
  double amount = ConsoleInput::GetDouble();

  // check transfer limit
  if (account->paybill_limit_remaining[company] < amount) {
    std::cout << ERROR_MESSAGE_HIT_PAYBILL_LIMIT << std::endl;
    return;
  }

  // check balance
  double charge = is_admin_ ? 0.0 : GetTransactionCharge(name, account_number);
  if (account->balance < (amount + charge)) {
    std::cout << ERROR_BALANCE_INSUFFICIENT << std::endl;
    return;
  }

  // do it
  account->balance -= amount + charge;
  account->paybill_limit_remaining[company] -= amount;

  // print out transaction
  PushTransactionRecord(3, name, account_number, amount, company);
  PushTransactionRecord(1, name, account_number, charge);

  // good
  return;
}

void Commands::deposit() {
  if (!CheckLogin()) {
    return;
  }
  std::string name = PromptForAccountHolderIfUnknown();
  std::cout << PROMPT_ENTER_ACCOUNT_NUMBER << std::endl;
  int number = ConsoleInput::GetInteger();
  std::cout << PROMPT_DEPOSIT_VALUE << std::endl;
  double amount = ConsoleInput::GetDouble();
  try {
    if (amount <= 0) {
      std::cout << ERROR_MIN_INPUT << std::endl;
      return;
    }
    if (amount > 99999.99){
      std::cout << ERROR_MAX_INPUT << std::endl;
      return;
    }
    std::vector<Account*> temp = accounts_[name];
    if (temp.empty()) {
      std::cout << ERROR_MESSAGE_ACCOUNTLESS_USER << std::endl;
      return;
    }
    bool owned_account = UserExists(name);
    Account* temp_account = GetAccount(name, number);
    if (owned_account == false || temp_account == nullptr) {
    std::cout << ERROR_MESSAGE_STOLEN_ACCOUNT << std::endl;
      return;
    }
    if (!temp_account->is_active) {
      std::cout << ERROR_DISABLED << std::endl;
      return;
    } else if (temp_account->is_deleted) {
      std::cout << ERROR_DELETED << std::endl;
      return;
    }
    float transaction_charge =
      is_admin_ ? 0.0 : GetTransactionCharge(name, number);
    if (temp_account->balance + amount < transaction_charge) {
      std::cout << ERROR_BALANCE_INSUFFICIENT << std::endl;
      return;
    }
    PushTransactionRecord(4, name, number, amount);
    if (!is_admin_) {
      PushTransactionRecord(1, name, number, transaction_charge);
    }
    std::cout << SUCCESS_DEPOSIT << std::endl;
    return;
  } catch(std::exception& e) {
      std::cout << ERROR_INVALID_INPUT << std::endl;
      return;
  }
}

void Commands::create() {
  if (!CheckLogin(1)) {
    return;
  }
  std::string name = PromptForAccountHolderIfUnknown();
  std::cout << PROMPT_INIT_BALANCE << std::endl;
  double init = ConsoleInput::GetDouble();
  if (init > 99999.99) {
    std::cout << ERROR_ABOVE_MAX_INIT << std::endl;
    return;
  }
  PushTransactionRecord(5, name, 00000, init);
  return;
}

void Commands::delete_account() {
  if (!CheckLogin(1)) {
    return;
  }
  std::string name = PromptForAccountHolderIfUnknown();
  std::cout << PROMPT_ENTER_ACCOUNT_NUMBER << std::endl;
  int number = ConsoleInput::GetInteger();
  std::vector<Account*> temp = accounts_[name];
  if (temp.empty()) {
    std::cout << ERROR_MESSAGE_ACCOUNTLESS_USER << std::endl;
    return;
  }
  bool owned_account = UserExists(name);
  Account* temp_account = GetAccount(name, number);
  if (owned_account == false || temp_account == nullptr) {
    std::cout << ERROR_MESSAGE_STOLEN_ACCOUNT << std::endl;
    return;
  }
  if (temp_account->is_deleted) {
    std::cout << ERROR_DELETED << std::endl;
    return;
  }
  PushTransactionRecord(6, name, number);
  temp_account->is_deleted = true;
  std::cout << SUCCESS_DELETE << std::endl;
  return;
}

void Commands::disable() {
  if (!CheckLogin(1)) {
    return;
  }
  std::string name = PromptForAccountHolderIfUnknown();
  std::cout << PROMPT_ENTER_ACCOUNT_NUMBER << std::endl;
  int number = ConsoleInput::GetInteger();
  std::vector<Account*> temp = accounts_[name];
  if (temp.empty()) {
    std::cout << ERROR_MESSAGE_ACCOUNTLESS_USER << std::endl;
    return;
  }
  bool owned_account = UserExists(name);
  Account* temp_account = GetAccount(name, number);
  if (owned_account == false || temp_account == nullptr) {
    std::cout << ERROR_MESSAGE_STOLEN_ACCOUNT << std::endl;
    return;
  }
  if (temp_account->is_deleted) {
    std::cout << ERROR_DELETED << std::endl;
    return;
  } else if (!temp_account->is_active) {
    std::cout << ERROR_DISABLED << std::endl;
    return;
  }
  PushTransactionRecord(7, name, number);
  temp_account->is_active = false;
  std::cout << SUCCESS_DISABLE << std::endl;
  return;
}

void Commands::changeplan() {
  if (!CheckLogin(1)) {
    return;
  }
  std::string name = PromptForAccountHolderIfUnknown();
  std::cout << PROMPT_ENTER_ACCOUNT_NUMBER << std::endl;
  int number = ConsoleInput::GetInteger();
  std::vector<Account*> temp = accounts_[name];
  if (temp.empty()) {
    std::cout << ERROR_MESSAGE_ACCOUNTLESS_USER << std::endl;
    return;
  }
  bool owned_account = UserExists(name);
  Account* temp_account = GetAccount(name, number);
  if (owned_account == false || temp_account == nullptr) {
    std::cout << ERROR_MESSAGE_STOLEN_ACCOUNT << std::endl;
    return;
  }
  if (temp_account->is_deleted) {
    std::cout << ERROR_DELETED << std::endl;
    return;
  } else if (!temp_account->is_active) {
    std::cout << ERROR_DISABLED << std::endl;
    return;
  }
  if (temp_account->is_student_plan) {
    PushTransactionRecord(8, name, number);
    std::cout << SUCCESS_TO_NONSTUDENT << std::endl;
  } else {
    PushTransactionRecord(8, name, number);
    std::cout << SUCCESS_TO_STUDENT << std::endl;
  }
  return;
}

void Commands::enable() {
  if (!CheckLogin(1)) {
    return;
  }
  std::string name = PromptForAccountHolderIfUnknown();
  std::cout << PROMPT_ENTER_ACCOUNT_NUMBER << std::endl;
  int number = ConsoleInput::GetInteger();
  std::vector<Account*> temp = accounts_[name];
  if (temp.empty()) {
    std::cout << ERROR_MESSAGE_ACCOUNTLESS_USER << std::endl;
    return;
  }
  bool owned_account = UserExists(name);
  Account* temp_account = GetAccount(name, number);
  if (owned_account == false || temp_account == nullptr) {
    std::cout << ERROR_MESSAGE_STOLEN_ACCOUNT << std::endl;
    return;
  }
  if (temp_account->is_deleted) {
    std::cout << ERROR_DELETED << std::endl;
    return;
  } else if (temp_account->is_active) {
    std::cout << ERROR_ENABLED << std::endl;
    return;
  }
  PushTransactionRecord(9, name, number);
  temp_account->is_active = true;
  std::cout << SUCCESS_ENABLE << std::endl;
  return;
}

void Commands::logout() {
  if (is_logged_in_ == true) {
    PushTransactionRecord(00);
    is_logged_in_ = false;
    logged_in_name_ = "";
    is_admin_ = false;

    assert(!transactions_file_.empty());
    TransactionIO::PrintToTransactionFile(&transaction_output_,
                                          transactions_file_);
    assert(transaction_output_.size() == 0);

  } else {
    std::cout << ERROR_MESSAGE_NO_LOGIN << std::endl;
    return;
  }
  return;
}

std::string Commands::PromptForAccountHolder() {
  std::cout << PROMPT_ENTER_CUSTOMER_NAME << std::endl;
  std::string name = ConsoleInput::GetString();
  if(name.length() > 20){
    name = name.substr(0,20);
  }
  return name;
}

std::string Commands::PromptForAccountHolderIfUnknown() {
  return is_admin_ ? PromptForAccountHolder() : logged_in_name_;
}


bool Commands::CheckLogin(bool admins_only) {
  if (!is_logged_in_) {
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
  if (account->is_student_plan) {
    return 0.05;
  } else {
    return 0.1;
  }
}
} //namespace BankFrontEnd
