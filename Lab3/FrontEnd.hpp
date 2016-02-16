
#include <string>
#include <map>
#include <vector>

/**
*
**/
class Account {
public:
  int number;
  bool isActive;
  float balance;
  bool isStudentPlan;
};
//12345 John Doe             A 00110.00 S

// check account validity
class Accounts {
public:
  // returns true on success, false on failure
  bool isAccountValid(std::string name) {
    //todo
    return false;
  }

  // parse file
  void loadFile(const char* file) {

  }
private:
  void parseLine(std::string& line) {
    // do stuff
    // diff ret??
  }

  std::map<std::string, std::vector<Account> > accounts; //THIS THING
};

// check input (i.e.) money validity
