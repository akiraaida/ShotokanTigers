package banksys;

/**
* References transaction codes used in trasaction files.
*/
class TransactionType {
  /**
  * Code indicating a 'login' transaction.
  */
  public static final int login = 10;

  /**
  * Code indicating a 'withdrawal' transaction.
  */
  public static final int withdrawal = 1;

  /**
  * Code indicating a 'transfer' transaction.
  */
  public static final int transfer = 2;

  /**
  * Code indicating a 'paybill' transaction.
  */
  public static final int paybill = 3;

  /**
  * Code indicating a 'deposit' transaction.
  */
  public static final int deposit = 4;

  /**
  * Code indicating a 'create' transaction.
  */
  public static final int create = 5;

  /**
  * Code indicating a 'delete' transaction.
  */
  public static final int delete = 6;

  /**
  * Code indicating a 'disable' transaction.
  */
  public static final int disable = 7;

  /**
  * Code indicating a 'changeplan' transaction.
  */
  public static final int changeplan = 8;

  /**
  * Code indicating a 'enable' transaction.
  */
  public static final int enable = 9;

  /**
  * Code indicating a 'logout' transaction.
  */
  public static final int logout = 0;
}
