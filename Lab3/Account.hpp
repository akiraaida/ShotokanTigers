#ifndef BANK_FRONTEND_ACCOUNT_HPP_
#define BANK_FRONTEND_ACCOUNT_HPP_
/******************************************************************************
* Account.hpp
* CSCI 3060u/SOFE 3980u: Course Project Front End
* Winter 2016
*
* Shotokan Tigers:
* -----
* Akira Aida          100526064
* Kathryn McKay       100524201
* Alexander Wheadon   100514985
*
* Contains Account POD & AccountParser methods
*
*******************************************************************************/

#include <deque>
#include <map>
#include <string>
#include <vector>

namespace BankFrontEnd {
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
  bool is_active;

  /**
  * Current value associated with this account in CAD
  **/
  double balance;

  /**
  * Flag corresponding to SP or NS charges.
  **/
  bool is_student_plan;

  /**
  * Reset at each day; Number of dollars that may be withdrawn this day.
  **/
  double withdrawal_limit_remaining;

  /**
  * Reset at each day; Number of dollars that may be transferred this day.
  **/
  double transfer_limit_remaining;

  /**
  * TODO: with above limits, keep track of paybill limits (Each limit may not)
  * belong in this class but that is later things).
  **/

};

/**
* Convert from file to accounts map.
**/
namespace AccountParser {
  std::map<std::string, std::vector<Account*> > parse(const char* fpath);
}
}


#endif //BANK_FRONTEND_ACCOUNT_HPP_
