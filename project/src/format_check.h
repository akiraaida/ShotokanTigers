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
   * data was longer than 8 characters.
   **/
   kTooLong,
   
   /**
   * Too many values following the decimal point.
   **/
   kTooLongFractional,
   
   /**
   * Input contained non-numerical, non point symbols.
   **/
   kInvalidSymbol,
   
   /**
   * Input had only fractional part
   **/
   kBelowOne
};
} // namespace CurrencyError

/**
* returns CurrencyError::kValid if the string checks out.
**/
int CheckCurrency(const std::string& number);
} //namespace FormatCheck
} //namespace BankFrontEnd

#endif //BANK_FRONTEND_FORMAT_CHECK_H_