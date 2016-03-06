/******************************************************************************
* format_check.cc
* CSCI 3060u/SOFE 3980u: Course Project Front End
* Winter 2016
*
* Shotokan Tigers:
* -----
* Akira Aida          100526064
* Kathryn McKay       100524201
* Alexander Wheadon   100514985
*******************************************************************************/
#include "format_check.h"
#include <vector>

namespace BankFrontEnd {
namespace FormatCheck {
  
int CheckCurrency(const std::string& number) {
  // check for invalid symbols
  std::vector<std::string> invalid_symbols = {"$", ","};
  for(std::string invalid_symbol : invalid_symbols) {
    if(number.find(invalid_symbol) != std::string::npos) {
      return CurrencyError::kInvalidSymbol;
    }
  }
  
  // check number of chars following decimal point
  std::string::size_type decimal_point_position = number.find(".");
  if(decimal_point_position != std::string::npos) {
    if(decimal_point_position == 0) {
      return CurrencyError::kBelowOne;
    } else {
      int nfractionals = (number.size() - 1) - decimal_point_position;
      if(nfractionals > 2) {
        return CurrencyError::kTooLongFractional;
      }
    }
  }
      
  // test size
  if(number.size() > 8) 
    return CurrencyError::kTooLong;
  
  
  
  // test for general invalidity
  try {
    std::stof(number);
  } catch(std::exception& e) {
    return CurrencyError::kInvalid;
  }
  
  // I guess it's fine
  return CurrencyError::kValid;
  
}
} //namespace FormatCheck
} //namespace BankFrontEnd