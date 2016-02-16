
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
      std::cout << line << std::endl;
    }
    file.close();
  } else {
    std::cout << "ERROR" << std::endl;
  }


  return 0;
}
