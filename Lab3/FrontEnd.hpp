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
  /**
  * Try to login.
  * \param name: name of customer (or "admin")
  * \return whether login was successful.
  **/
  bool login(std::string name);

  /**
  * Withdraw a value from an account.
  * \param name Account holder's moniker.
  * \param account Account's index.
  * \param amount Amount to withdraw.
  * \return Success of transaction.
  **/
  bool withdrawal(std::string name, int account, double amount);

  /**
  * TODO
  **/
  bool transfer(std::string name, int account1, int account2, double amount);

  /**
  * TODO
  **/
  bool paybill(std::string name, int account, std::string company, double amount);

  /**
  * TODO
  **/
  bool deposit(std::string name, int account, double amount);

  /**
  * TODO
  **/
  bool create(std::string name, double amount);

  /**
  * TODO
  **/
  bool deleteAccount(std::string name, int account);

  /**
  * TODO
  **/
  bool disable(std::string name, int account);

  /**
  * TODO
  **/
  bool changePlan(std::string name, int account);

  /**
  * TODO
  **/
  bool logout();

 private:
    /**
    * Field with customer names as keys and their associated bank accounts as
    * values.
    **/
    std::map<std::string, std::vector<Account*> > accounts;
};
