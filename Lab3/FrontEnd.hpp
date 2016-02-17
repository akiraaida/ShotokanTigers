#ifndef FRONTEND_HPP
#define FRONTEND_HPP
/******************************************************************************
* FrontEnd.hpp
* CSCI 3060u/SOFE 3980u: Course Project Front End
* Winter 2016
*
* Shotokan Tigers:
* -----
* Akira Aida          100526064
* Kathryn McKay       100524201
* Alexander Wheadon   100514985
*******************************************************************************/
#include <string>
#include <map>
#include <vector>
#include <deque>

/**
* Stores information about a customer's account's balance, etc.
**/
class Account {
 public:
  /**
  * 'Account number' uniquely identifying item in system
  **/
  int number;

  /**
  * Flag indicating whether or not an account is diabled i.e. A == true, D ==
  * false
  **/
  bool isActive;

  /**
  * Current value associated with this account in CAD
  **/
  double balance;

  /**
  * Flag corresponding to SP or NS charges.
  **/
  bool isStudentPlan;

  /**
  * Reset at each day; Number of dollars that may be withdrawn this day.
  **/
  double withdrawalLimitRemaining;

  /**
  * Reset at each day; Number of dollars that may be transferred this day.
  **/
  double transferLimitRemaining;

  /**
  * TODO: with above limits, keep track of paybill limits (Each limit may not)
  * belong in this class but that is later things).
  **/

};

/**
* Convert from file to accounts map.
**/
class AccountParser {
public:
  /**
  * Parses the info at fpath into a bank accounts directory.
  **/
  static std::map<std::string, std::vector<Account*> > parse(const char* fpath);
};

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


#endif //FRONTEND_HPP
