/******************************************************************************
* format_check.h
* CSCI 3060u/SOFE 3980u: Course Project Front End
* Winter 2016
*
* Enumeration of the format of a currency string.
* This helps to ensure that money values entered are valid.
*
* Shotokan Tigers:
* -----
* Akira Aida          100526064
* Kathryn McKay       100524201
* Alexander Wheadon   100514985
*******************************************************************************/
#ifndef BANK_FRONTEND_FORMAT_CHECK_H_
#define BANK_FRONTEND_FORMAT_CHECK_H_

#include <string>

namespace BankFrontEnd {

namespace FormatCheck {
/**
* Describes things that can be found wrong with a money input.
**/
namespace CurrencyError {
enum {
  /**
  * No problems found.
  **/
   kValid,
   
   /**
   * This thing wasn't parseable for some general reason.
   **/
   kInvalid,
   
   /**
   * Data value exceeded 99999.99
   **/
   kTooLarge,
   
   /**
   * Too many values following the decimal point.
   **/
   kTooLongFractional,
   
   /**
   * Input contained non-numerical, non point symbols.
   **/
   kInvalidSymbol,
   
   /**
   * Input was negative.
   **/
   kNegativeOrZero,
   
   /**
   * Input was fine in all ways, except it was not canadian.
   * Note that where canadian bill values are NOT a concern, this is
   * synonymous with kValid.
   **/
   kNonCanadian,
   
   /**
   * Uncaught somehow
   **/
   kUnset
};
} // namespace CurrencyError

/**
* Parses currency. If not set to nullptr, status is CurrencyError::kValid or
* CurrencyError::kNonCanadian when the string checks out and an error otherwise. 
**/
double CheckCurrency(const std::string& number, int* status);

/**
* Confirms whether the error indicates a correct value when conformance to
* canadian currency is not a constraint.
**/
bool NonBillValueIsValid(int error);

/**
* Checks if 'amount' is valid currency.
**/
bool CheckUnit(double value);

/**
* get string corresponding to currency error
**/
std::string GetCurrencyErrorMessage(int error);
} //namespace FormatCheck
} //namespace BankFrontEnd

#endif //BANK_FRONTEND_FORMAT_CHECK_H_