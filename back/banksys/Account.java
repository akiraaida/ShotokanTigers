package banksys;

/**
* POD containing storing the fields of an account-syntaxed string.
*
* <h1>Example Usage:</h1>
* <p>
*  if(currentAccount.isActive) <br />
* System.out.printf("Account #%d is active%n", currentAccount.number); <br/>
*  else <br/>
* System.out.printf("Account #%d is disabled%n", currentAccount.number); <br/>
* </p>
*/
class Account {
    /**
    * Unique identifier that should be 5 digits i.e. <=99999
    */
    int number;

    /**
    * When false, indicates a disabled account.
    */
    boolean isActive;

    /**
    * Dollars (CAD) in account.
    *
    * <p>
    * Should be nonnegative, and should not exceed $99999.99 or have fractional
    * part exceeding 2 digits.
    * </p>
    */
    double balance;

    /**
    * Number of transactions applied to this account e.g. deposits, disables.
    */
    int transactionCount;

    /**
    * When false, indicates that this account is on a non-student plan.
    */
    boolean isStudentPlan;
}
