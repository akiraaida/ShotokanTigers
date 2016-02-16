
#include <string>
#include <map>
#include <vector>

/**
* Stores data for 1 bank account
**/
class Account {
 public:
  /**
  * ctor
  **/
  /*Account() {
    // Initialize limits for this day
    withdrawalLimitRemaining = 500.0;
    transferLimitRemaining = 1000.0;
  }*/

  /**
  * Class Fields
  **/
  int number;
  bool isActive;
  double balance;
  bool isStudentPlan;
  double withdrawalLimitRemaining;
  double transferLimitRemaining;
  // won't put in  paybill limits yet cause ugghh..??
};

/**
* Convert from file to accounts map
**/
class AccountParser {

  static std::map<std::string, std::vector<Account*> > parse(const char* fpath);
};

class Commands {
  std::map<std::string, std::vector<Account*> > accounts;
  bool login(std::string name);
  bool withdrawal(std::string name, int account, double amount);
  bool transfer(std::string name, int account1, int account2, double amount);
  bool paybill(std::string name, int account, std::string company, double amount);
  bool deposit(std::string name, int account, double amount);
  bool create(std::string name, double amount);
  bool deleteAccount(std::string name, int account);
  bool disable(std::string name, int account);
  bool changePlan(std::string name, int account);
  bool logout();
};
;
