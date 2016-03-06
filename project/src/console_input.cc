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

#include <cfloat>
#include <climits>

#include <iostream>

#include "format_check.h"

namespace BankFrontEnd {
namespace ConsoleInput {
std::string GetString() {
  std::string destination;
  std::getline(std::cin, destination);
  return destination;
}

int GetInteger() { // turn string from cin into an integer
  int destination;
  try {
    destination = std::stoi(GetString());
  } catch(std::exception& e) {
    return INT_MIN;
  }
  return destination;
}

double GetDouble(int* status) { // turn string from cin into a double
  std::string number = GetString();
  double value = FormatCheck::CheckCurrency(number, status);
  return FormatCheck::NonBillValueIsValid(*status) ? value : 0.0;
}
} // namespace ConsoleInput
} // namespace BankFrontEnd
