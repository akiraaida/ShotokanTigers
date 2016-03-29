/*
* TransactionType.java
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
 * References transaction codes used in trasaction files.
 */
public class TransactionType {
  /** Code indicating a 'login' transaction. */
  public static final int LOGIN = 10;

  /** Code indicating a 'withdrawal' transaction. */
  public static final int WITHDRAWAL= 1;

  /** Code indicating a 'transfer' transaction */
  public static final int TRANSFER = 2;

  /** Code indicating a 'paybill' transaction. */
  public static final int PAYBILL = 3;

  /** Code indicating a 'deposit' transaction. */
  public static final int DEPOSIT = 4;

  /** Code indicating a 'create' transaction. */
  public static final int CREATE = 5;

  /** Code indicating a 'delete' transaction. */
  public static final int DELETE = 6;

  /** Code indicating a 'disable' transaction. */
  public static final int DISABLE = 7;

  /** Code indicating a 'changeplan' transaction. */
  public static final int CHANGEPLAN = 8;

  /** Code indicating a 'enable' transaction. */
  public static final int ENABLE = 9;

  /** Code indicating a 'logout' transaction. */
  public static final int LOGOUT = 0;
}
