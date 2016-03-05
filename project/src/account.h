#ifndef BANK_FRONTEND_ACCOUNT_H_
#define BANK_FRONTEND_ACCOUNT_H_
/******************************************************************************
* account.h
* CSCI 3060u/SOFE 3980u: Course Project Front End
* Winter 2016
*
* Shotokan Tigers:
* -----
* Akira Aida          100526064
* Kathryn McKay       100524201
* Alexander Wheadon   100514985
*
* Contains account POD
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
  * Constructor
  **/
  Account();

  /**
  * Output current dollars in account to console.
  **/
  void PrintBalance() const;

  /**
  * Output remaining withdrawal limit to console.
  **/
  void PrintWithdrawalLimit() const;

  /**
  * Output remaining transfer limit to console.
  **/
  void PrintTransferLimit() const;

  /**
  * Output remaining paybill limit to indicated recieving company.
  **/
  void PrintPaybillLimit(const std::string& recipient) const;

  /**
  * Returns true when a company limit is filed.
  **/
  bool TransferRecipientExists(const std::string& recipient) const;

  /**
  * 'Account number' uniquely identifying item in system
  **/
  int number;

  /**
  * Flag indicating whether or not an account is disabled i.e.
  * A == true, D == false
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
  * Flag for a deleted account.
  **/
  bool is_deleted;

  /**
  * Reset at each day; Number of dollars that may be withdrawn this day.
  **/
  double withdrawal_limit_remaining;

  /**
  * Reset at each day; Number of dollars that may be transferred this day.
  **/
  double transfer_limit_remaining;

  /**
  * Reset at each day; Number of dollars that may be paid to each company this
  * day.
  **/
  std::map<std::string, double> paybill_limit_remaining;
};
} //namespace BankFrontEnd


#endif //BANK_FRONTEND_ACCOUNT_H_
