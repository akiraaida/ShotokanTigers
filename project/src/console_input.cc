/******************************************************************************
* console_input.cc
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
#include "console_input.h"

#include <iostream>

namespace BankFrontEnd {
namespace ConsoleInput {
static std::string GetString() {
  std::string destination;
  std::getline(std::cin, destination);
  return destination;
}

static int GetInteger() { // turn string from cin into an integer
  return std::atoi(GetString().c_str());
}

static double GetDouble() { // turn string from cin into a double
  return std::atof(GetString().c_str());
}
} // namespace ConsoleInput
} // namespace BankFrontEnd
