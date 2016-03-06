/******************************************************************************
* account.cc
* CSCI 3060u/SOFE 3980u: Course Project Front End
* Winter 2016
*
* Shotokan Tigers:
* -----
* Akira Aida          100526064
* Kathryn McKay       100524201
* Alexander Wheadon   100514985
*******************************************************************************/
#include "account.h"

#include <cassert>
#include <cmath>

#include <iostream>

namespace BankFrontEnd {
Account::Account() {
  withdrawal_limit_remaining = 500.0;
  transfer_limit_remaining = 1000.0;
  paybill_limit_remaining["EC"] = 2000.0;
  paybill_limit_remaining["CQ"] = 2000.0;
  paybill_limit_remaining["TV"] = 2000.0;
}

void Account::PrintBalance() const {
  assert(balance >= 0.0);
  printf("Available Balance (%d):\n", number);
  PrintMoney(std::abs(balance));
}

/**
* Output remaining deposit limit to console.
**/
void Account::PrintWithdrawalLimit() const {
  printf("Daily Withdrawals Remaining (%d):\n", number);
  PrintMoney(withdrawal_limit_remaining);
}

/**
* Output remaining transfer limit to console.
**/
void Account::PrintTransferLimit() const {
  printf("Daily Transfer Limit Remaining (%d):\n", number);
  PrintMoney(transfer_limit_remaining);
}

/**
* Output remaining paybill limit to indicated recieving company.
**/
void Account::PrintPaybillLimit(const std::string& recipient) const {
  assert(CompanyExists(recipient));
  printf("Daily Payment Limit to %s Remaining (%d):\n", recipient.c_str(),
         number);
  PrintMoney(paybill_limit_remaining.at(recipient));
}


bool Account::CompanyExists(const std::string& recipient) const {
  return paybill_limit_remaining.find(recipient)
         != paybill_limit_remaining.end();
}


void Account::PrintMoney(double amount) const {
  printf("%.2f\n", amount);
}


} //namespace BankFrontEnd
