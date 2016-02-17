#ifndef FRONTEND_COMMANDS_HPP_
#define FRONTEND_COMMANDS_HPP_
/******************************************************************************
* Commands.hpp
* CSCI 3060u/SOFE 3980u: Course Project Front End
* Winter 2016
*
* Shotokan Tigers:
* -----
* Akira Aida          100526064
* Kathryn McKay       100524201
* Alexander Wheadon   100514985
*******************************************************************************/

#include <deque>
#include <map>
#include <string>
#include <vector>

#include "Account.hpp"

/**
* Bank System storing, tracking, and querying the accounts.
**/
class Commands {
 public:

   Commands();

  /**
  * Gives map
  **/
  void setAccounts(std::map<std::string, std::vector<Account*> >&& accounts);


  /**
  * Try to login.
  * \param name: name of customer (or "admin")
  * \return whether login was successful.
  **/
  bool login();

  /**
  * TODO
  **/
  std::string determineSession();

  /**
  * Withdraw a value from an account.
  * \param name Account holder's moniker.
  * \param account Account's index.
  * \param amount Amount to withdraw.
  * \return Success of transaction.
  **/
  bool withdrawal();

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
  bool deleteAccount();

  /**
  * TODO
  **/
  bool disable();

  /**
  * TODO
  **/
  bool changePlan();

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
    bool userExists(std::string name);

    /**
    * Check if account belongs to user
    **/
    Account* getAccount(std::string name, int account);

    /**
    * Find customer name corresponding to account number;
    * Returns empty string if account was not found
    **/
    std::string getAccountOwner(int account);

    /**
    * pushes transaction record with that info onto stack
    **/
    void pushTransactionRecord(int code, std::string name = "", int accountNumber = 0, double money = 0.0, std::string misc = "");

    /**
    * Field with customer names as keys and their associated bank accounts as
    * values.
    **/
    std::map<std::string, std::vector<Account*> > accounts;

    /**
    * Tracks whether a session is in progress
    **/
    bool isLoggedIn;

    /**
    * Name of the current user
    **/
    std::string loggedInName;

    /**
    * Tracks whether current session has admin priv or not
    **/
    bool isAdmin;

    /**
    * Stack of things to push to output on logout
    **/
    std::deque<std::string> transactionOutput;

};


#endif //FRONTEND_COMMANDS_HPP_
