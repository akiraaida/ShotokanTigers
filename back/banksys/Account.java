/*
* Account.java
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
 * POD containing storing the fields of an account-syntaxed string.
 *
 * <h1>Example Usage:</h1>
 * <p>if(currentAccount.isActive) {<br />
 *   System.out.printf("Account #%d is active%n", currentAccount.number);<br/>
 * } else {<br/>
 *   System.out.printf("Account #%d is disabled%n", currentAccount.number);<br/>
 * }</p>
 */
public class Account {
  /** Unique identifier that should be 5 digits i.e. <=99999 */
  public int number;

  /** When false, indicates a disabled account. */
  public boolean isActive;

  /**
   * Dollars (CAD) in account.
   *
   * <p>Should be nonnegative, and should not exceed $99999.99 or have a
   * fractional part exceeding 2 digits.</p>
   */
  public double balance;

  /**
   * Number of transactions applied to this account e.g. deposits,
   * disables.
   */
  public int transactionCount;

  /** When false, indicates that this account is on a non-student plan. */
  public boolean isStudentPlan;
}
