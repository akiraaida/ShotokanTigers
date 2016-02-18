#ifndef BANK_FRONTEND_ACCOUNT_STATUS_H_
#define BANK_FRONTEND_ACCOUNT_STATUS_H_
/******************************************************************************
* account_status.h
* CSCI 3060u/SOFE 3980u: Course Project Front End
* Winter 2016
*
* Shotokan Tigers:
* -----
* Akira Aida          100526064
* Kathryn McKay       100524201
* Alexander Wheadon   100514985
*
*******************************************************************************/

#include <string>

#include "account.h"

namespace BankFrontEnd {
namespace AccountStatus {
enum {
  kActiveAccount = 0,
  kDisabledAccount,
  kDeletedAccount,
  kAccountNoExist
};

/**
* Returns one of AccountStatus: Active, disabled, new, etc. Where a new or
* deleted account state takes precedence.
**/
int QueryAccountStatus(Account* account);

/**
* Returns the message corresponding to an account status
* Error messages except for active account, which just states that the account
* is active.
**/
std::string GetErrorMessage(int status);


}
}

#endif //BANK_FRONTEND_ACCOUNT_STATUS_H_
