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
    String account_name;

    /**
    * Account number as parameter to this transaction.
    * @see Account class.
    */
    int account_number;

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
