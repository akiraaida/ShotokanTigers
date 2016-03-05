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
  printf("Current Balance[%d]: %.2f\n", number, balance);
}

/**
* Output remaining deposit limit to console.
**/
void Account::PrintWithdrawalLimit() const {
  printf("Daily Withdrawals Remaining[%d]: %.2f\n", number,
         withdrawal_limit_remaining);
}

/**
* Output remaining transfer limit to console.
**/
void Account::PrintTransferLimit() const {
  printf("Daily Transfers Remaining[%d]: %.2f\n", number,
         transfer_limit_remaining);
}

/**
* Output remaining paybill limit to indicated recieving company.
**/
void Account::PrintPaybillLimit(const std::string& recipient) const {
  assert(TransferRecipientExists(recipient));
  printf("Daily Transfers to '%s' Remaining[%d]: %.2f\n", recipient.c_str(),
         number,
         paybill_limit_remaining.at(recipient));
}


bool Account::TransferRecipientExists(const std::string& recipient) const {
  return paybill_limit_remaining.find(recipient)
         == paybill_limit_remaining.end();
}


} //namespace BankFrontEnd
