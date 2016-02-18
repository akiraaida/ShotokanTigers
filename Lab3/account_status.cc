/******************************************************************************
* account_status.cc
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
#include "account_status.h"

namespace BankFrontEnd {
namespace AccountStatus {
// Query Account Status
int QueryAccountStatus(Account* account) {
  if (account == nullptr) {
    return AccountStatus::kAccountNoExist;
  } else if (account->is_deleted) {
    return AccountStatus::kDeletedAccount;
  } else if (!account->is_active) {
    return AccountStatus::kDisabledAccount;
  } else {
    return AccountStatus::kActiveAccount;
  }
}
}
}
