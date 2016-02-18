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
* This is the heart of the bank system.
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

   Commands();

  /**
  * Gives map
  **/
  void SetAccounts(std::map<std::string, std::vector<Account*> >&& accounts);


  /**
  * Try to login.
  * \param name: name of customer (or "admin")
  * \return whether login was successful.
  **/
  bool login();

  /**
  * TODO
  **/
  std::string DetermineSession();

  /**
  * Withdraw a value from an account.
  * \param name Account holder's moniker.
  * \param account Account's index.
  * \param amount Amount to withdraw.
  * \return Success of transaction.
  **/
  bool withdrawal();

  bool CheckUnit(double amount);

  /**
  * TODO
  **/
  bool transfer();

  /**
  * TODO
  **/
  bool paybill();

  /**
  * TODO
  **/
  bool deposit();

  /**
  * TODO
  **/
  bool create();

  /**
  * TODO
  **/
  bool delete_account();

  /**
  * TODO
  **/
  bool disable();

  /**
  * TODO
  **/
  bool changeplan();

  /**
  * TODO
  **/
  bool logout();

  /**
  * TODO
  **/
  bool enable();

 private:
    /**
    * Check if user exists
    **/
    bool UserExists(std::string name);

    /**
    * Check if account belongs to user
    **/
    Account* GetAccount(std::string name, int account);

    /**
    * Find customer name corresponding to account number;
    * Returns empty string if account was not found
    **/
    std::string GetAccountOwner(int account);

    /**
    * pushes transaction record with that info onto stack
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
    * Retrieves the account holder's name based on the session.
    **/
    std::string PromptForAccountHolderIfUnknown();

    /**
    * Prints an error if the user is not logged in.
    * Prints an error if the user is not admin.
    **/
    bool CheckLogin(bool admins_only = false);

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

};
}

#endif //BANK_FRONTEND_COMMANDS_H_
