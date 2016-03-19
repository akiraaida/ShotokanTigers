/*
* Transaction.java
* CSCI 3060U/SOFE 3980U: Course Project Back End
* Winter 2016
*
* Shotokan Tigers:
* -----
* Akira Aida          100526064
* Kathryn McKay       100524201
* Alexander Wheadon   100514985
*/

package banksys;

/**
* POD storing the elements of a transaction string.
*/
class Transaction {
  /**
  * Indicates what kind of transaction this is.
  * @see TransactionType
  */
  int code;

  /**
  * Account owner name as parameter to this transaction.
  */
  String accountName;

  /**
  * Account number as parameter to this transaction.
  * @see Account class.
  */
  int accountNumber;

  /**
  * Dollar amount in CAD as parameter to this transaction.
  *
  * <p>
  * Should be 5 digits, i.e. <=99999
  * </p>
  **/
  double amount;

  /**
  * 2-character parameter to this transaction.
  **/
  String misc;
}
