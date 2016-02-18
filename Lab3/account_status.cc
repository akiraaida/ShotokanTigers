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

#include <cassert>

#define ACCOUNT_STATUS_INVALID_ACCOUNT "ERROR, THAT ACCOUNT IS INVALID."
#define ACCOUNT_STATUS_DISABLED "ERROR, THAT ACCOUNT IS DISABLED."
#define ACCOUNT_STATUS_ENABLED "ERROR, THAT ACCOUNT IS ENABLED ALREADY."
#define ACCOUNT_STATUS_DELETED "ERROR, THAT ACCOUNT HAS BEEN DELETED."
#define ACCOUNT_STATUS_ACTIVE "This account is active."
namespace BankFrontEnd {
namespace AccountStatus {
// Query Account Status
int QueryAccountStatus(Account* account) {
  if (account == nullptr) {
    return kAccountNoExist;
  } else if (account->is_deleted) {
    return kDeletedAccount;
  } else if (!account->is_active) {
    return kDisabledAccount;
  } else {
    return kActiveAccount;
  }
}

std::string GetErrorMessage(int status) {
  switch (status) {
    case kActiveAccount: {
      return ACCOUNT_STATUS_ACTIVE;
      break;
    }

    case kInvalidAccount:
    case kAccountNoExist: {
      return ACCOUNT_STATUS_INVALID_ACCOUNT;
      break;
    }

    case kDeletedAccount: {
      return ACCOUNT_STATUS_DELETED;
      break;
    }

    case kDisabledAccount: {
      return ACCOUNT_STATUS_DISABLED;
      break;
    }

    default:
      assert(false);
  }
}

}
}
