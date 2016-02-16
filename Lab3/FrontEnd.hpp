
#include <string>
#include <map>
#include <vector>

class Account {
  int number;
  bool isStudentPlan;
  bool isActive;
}

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
  void parseLine

  std::map<std::string, std::vector<int>> accounts;
}

// check input (i.e.) money validity
