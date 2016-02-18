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
#include <iostream>
namespace BankFrontEnd {
  Account::Account() {
    withdrawal_limit_remaining = 500.0;
    transfer_limit_remaining = 1000.0;
    paybill_limit_remaining["EC"] = 2000.0;
    paybill_limit_remaining["CQ"] = 2000.0;
    paybill_limit_remaining["TV"] = 2000.0;
  }
}
