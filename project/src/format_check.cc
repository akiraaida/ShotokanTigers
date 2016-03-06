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

#include <cmath>
#include <cassert>

#include <vector>

#define ERROR_NEGATIVE_INPUT "ERROR, ONLY POSITIVE VALUES ARE ACCEPTED."
#define ERROR_LONG_INPUT "ERROR, VALUE HAS MORE THAN 5 DIGITS PRECEDING THE DECIMAL."
#define ERROR_INPUT_BELOW_ONE "ERROR, VALUE MUST HAVE AT LEAST 1 DIGIT PRECEDING THE DECIMAL."
#define ERROR_INPUT_LOTS_FRACTIONALS "ERROR, VALUE CAN HAVE AT MOST 2 DIGITS FOLLOWING THE DECIMAL."
#define ERROR_INVALID_SYMBOL "ERROR, INPUT CONTAINS ONE OR MORE OF FORBIDDEN SYMBOLS [$ ,]"
#define ERROR_INVALID_INPUT "ERROR, INVALID INPUT."
#define ERROR_MIN_INPUT "ERROR, INPUT VALUE IS TOO SMALL."
#define ERROR_MAX_INPUT "ERROR, INPUT VALUE IS TOO LARGE."
#define ERROR_NON_CANADIAN "ERROR, ONLY CANADIAN BILL VALUES ARE ALLOWED."

namespace BankFrontEnd {
namespace FormatCheck {
  
double CheckCurrency(const std::string& number, int* status) {
  // Initialize values
  double value = 0.0;
  *status = CurrencyError::kUnset;
  
  // check for invalid symbols
  std::vector<std::string> invalid_symbols = {"$", ","};
  for(std::string invalid_symbol : invalid_symbols) {
    if(number.find(invalid_symbol) != std::string::npos) {
      *status = CurrencyError::kInvalidSymbol;
      return value;
    }
  }
  
  // test formatting
  std::string::size_type decimal_point_position = number.find(".");
  if(decimal_point_position != std::string::npos) {
    int nfractionals = (number.size() - 1) - decimal_point_position;
    if(nfractionals > 2) {
      *status = CurrencyError::kTooLongFractional;
      return value;
    }
  }
  
  // test for general invalidity
  try {
    value = std::stof(number);
  } catch(std::exception& e) {
    *status =  CurrencyError::kInvalid;
    return value;
  }
    
  // test value yielded
  if(value <= 0.0) {
    *status =  CurrencyError::kNegativeOrZero;
  } else if (value > 99999.99) {
    *status =  CurrencyError::kTooLarge;
  } else if (!CheckUnit(value)) {
    *status = CurrencyError::kNonCanadian;
  } else {
    *status = CurrencyError::kValid;
  }
  
  // status has been found
  return value;
}

bool NonBillValueIsValid(int error) {
  return error == CurrencyError::kValid || error == CurrencyError::kNonCanadian;
}

bool CheckUnit(double amount) {
  if (fmod(amount, 5) == 0 || fmod(amount, 10) == 0 || fmod(amount, 20) == 0
      || fmod(amount, 100) == 0) {
    return true;
  }
  return false;
}


std::string GetCurrencyErrorMessage(int error) {
  switch(error) {
   case CurrencyError::kInvalidSymbol: {
      return ERROR_INVALID_SYMBOL;
   }
    
   case CurrencyError::kInvalid: {
      return ERROR_INVALID_INPUT;
   }
   
   case CurrencyError::kTooLarge: {
     return ERROR_MAX_INPUT;
   }
    
   case CurrencyError::kTooLongFractional: {
      return ERROR_INPUT_LOTS_FRACTIONALS;
   }
    
   case CurrencyError::kValid: {
      return std::string();
   }
   
   case CurrencyError::kNonCanadian: {
     return ERROR_NON_CANADIAN;
   }
   
   case CurrencyError::kNegativeOrZero: {
     return ERROR_MIN_INPUT;
   }
      
    default:
      assert(false);
   }
}

} //namespace FormatCheck
} //namespace BankFrontEnd