
#include <string>
#include <map>
#include <vector>

class Account {
 public:

  int number;
  bool isActive;
  double balance;
  bool isStudentPlan;
  double withdrawalLimitRemaining;
  double transferLimitRemaining;

};

/**
* Convert from file to accounts map
**/
class AccountParser {
public:
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
