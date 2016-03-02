#ifndef BANK_FRONTEND_COMMANDS_H_
#define BANK_FRONTEND_COMMANDS_H_
/******************************************************************************
* commands.h
* CSCI 3060u/SOFE 3980u: Course Project Front End
* Winter 2016
*
* Shotokan Tigers:
* -----
* Akira Aida          100526064
* Kathryn McKay       100524201
* Alexander Wheadon   100514985
*
* This is the heart of the bank system. Each command in the system corresponds
* to a method here e.g. login, withdrawal, etc.
*
* Example Usage:
*   Commands bank;
*   bank.SetAccounts(accounts);
*   bank.login();
*   bank.logout();
*
* Google style note: chose to keep commands 'login' etc in the cheaper lowercase
* style, for consistency.
*
*******************************************************************************/

#include <deque>
#include <map>
#include <string>
#include <vector>

#include "account.h"

namespace BankFrontEnd {

/**
* Bank System storing, tracking, and querying the accounts.
**/
class Commands {
 public:

  /*
  * Constructor
  */
   Commands();

  /**
  * Gives map created from the Parse method to this class.
  **/
  void SetAccounts(std::map<std::string, std::vector<Account*> >&& accounts);

  /**
  * Gives filepath to print to.
  **/
  void SetTransactionPath(const std::string& path);

  /**
  * Corresponds to 'login' command.
  **/
  void login();


  /**
  * Corresponds to 'withdrawal' command.
  **/
  void withdrawal();

  /**
  * Corresponds to 'transfer' command.
  **/
  void transfer();

  /**
  * Corresponds to 'paybill' command.
  **/
  void paybill();

  /**
  * Corresponds to 'deposit' command.
  **/
  void deposit();

  /**
  * Corresponds to 'create' command. Admin only.
  **/
  void create();

  /**
  * Corresponds to 'delete' command. Admin only.
  **/
  void delete_account();

  /**
  * Corresponds to 'disable' command. Admin only.
  **/
  void disable();

  /**
  * Corresponds to 'changeplan' command. Admin only.
  **/
  void changeplan();

  /**
  * Corresponds to 'logout' command.
  * Causes output to the transaction file.
  **/
  void logout();

  /**
  * Corresponds to 'enable' command. Admin only.
  **/
  void enable();

 private:
    /**
    * Check if user exists
    **/
    bool UserExists(std::string name);

    /**
    * Checks if 'amount' is valid currency.
    **/
    bool CheckUnit(double amount);

    /*
    * Determines if it's a standard or admin session
    */
    std::string DetermineSession();

    /**
    * Returns nullptr if the name/account pair is not found in system.
    **/
    Account* GetAccount(std::string name, int account);

    /**
    * Find customer name corresponding to account number
    * Returns empty string if account was not found
    **/
    std::string GetAccountOwner(int account);

    /**
    * Pushes transaction record with that info onto stack.
    **/
    void PushTransactionRecord(int code, std::string name = "",
                               int account_number = 0, double money = 0.0,
                               std::string misc = "");

    /**
    * Formats string to match certain number of characters, using fluff to fill
    * space.
    **/
    std::string FitStringToSpace(std::string string, size_t size,
                                 char fluff, bool align_right = true);


    /**
    * Retrieves the account holder's name.
    **/
    std::string PromptForAccountHolder();


    /**
    * Retrieves the account holder's name based on the session.
    **/
    std::string PromptForAccountHolderIfUnknown();

    /**
    * Prints an error if the user is not logged in.
    * Prints an error if the user is not admin, if admins_only is set to true.
    **/
    bool CheckLogin(bool admins_only = false);

    /**
    * Retrieves Transaction Charge for a particular account.
    * Behaviour is undefined if the account doesn't exist.
    **/
    double GetTransactionCharge(std::string name, int account_number);

    /**
    * Field with customer names as keys and their associated bank accounts as
    * values.
    **/
    std::map<std::string, std::vector<Account*> > accounts_;

    /**
    * Tracks whether a session is in progress
    **/
    bool is_logged_in_;

    /**
    * Name of the current user
    **/
    std::string logged_in_name_;

    /**
    * Tracks whether current session has admin priv or not
    **/
    bool is_admin_;

    /**
    * Stack of things to push to output on logout
    **/
    std::deque<std::string> transaction_output_;

    /**
    * filepath to the transactions file
    **/
    std::string transactions_file_;

};
} //namespace BankFrontEnd

#endif //BANK_FRONTEND_COMMANDS_H_
