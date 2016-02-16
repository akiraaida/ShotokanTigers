
#include <iostream>
#include <fstream>
#include "FrontEnd.hpp"
//login

//withdrawal

//transfer

//paybill

//deposit

//create

//delete

//disable

//changeplan

//logout

// ./program accounts.txt
int main(int argc, const char* argv[]) {


  if(argc == 2) {
    std::ifstream file;
    file.open(argv[1]);
    std::string line;
    while(getline(file, line)) {

      std::map<std::string, std::vector<Account> > accounts;
      std::string num, name, stat, bal, plan;

      for(int i = 0; i < line.length(); i++){
        if(i < 5) {
          num = num + line[i];
        } else if(i > 5 && i < 26) {
          name = name + line[i];
        } else if(i > 26 && i < 28) {
          stat = stat + line[i];
        } else if(i > 28 && i < 37) {
          bal = bal + line[i];
        } else if(i > 37 && i < 39) {
          plan = plan + line[i];
        }
      }

    Account* newAcc = new Account();
    newAcc.


    }
    file.close();
  } else {
    std::cout << "ERROR" << std::endl;
  }


  return 0;
}
