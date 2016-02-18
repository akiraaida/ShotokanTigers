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
* Defines the assessment of an account's validity e.g. is it available for use.
* Also provides error messages corresponding to those states.
* Example Usage:
*   int status = AccountStatus::QueryAccountStatus(account);
*   if (status != AccountStatus::kActiveAccount) {
*     std::cout << AccountStatus::GetErrorMessage(status) << std::endl;
*     return;
*   }
*
*******************************************************************************/
#include <string>

#include "account.h"

namespace BankFrontEnd {
namespace AccountStatus {
/**
* Representation of various ways an account can be invalid; or valid.
**/
enum {
  kActiveAccount = 0,
  kDisabledAccount,
  kDeletedAccount,
  kAccountNoExist,
  kInvalidAccount
};

/**
* Returns one of AccountStatus: Active, disabled, new, etc. Where a new or
* deleted account state takes precedence.
**/
int QueryAccountStatus(Account* account);

/**
* Returns the message corresponding to an account status. That means error
* messages except for when status == kActiveAccount, in which case it's just a
* statement that the account is active.
**/
std::string GetErrorMessage(int status);
} //namespace AccountStatus
} //namespace BankFrontEnd

#endif //BANK_FRONTEND_ACCOUNT_STATUS_H_
