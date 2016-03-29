/*
* TestTransactionCalculator.java
* CSCI 3060U/SOFE 3980U: Course Project Back End
* Winter 2016
*
* Shotokan Tigers:
* -----
* Akira Aida          100526064
* Kathryn McKay       100524201
* Alexander Wheadon   100514985
*
* Adapted console output test code from:
*  http://stackoverflow.com/questions/1119385/junit-test-for-system-out-println
*/

package tests;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Vector;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.After;
import org.junit.Before;
import banksys.TransactionCalculator;
import banksys.TransactionType;
import banksys.Account;
import banksys.Transaction;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class TestTransactionCalculator {
  private static final String USERNAME_JOHN_DOE = "John Doe";
  private static final String USERNAME_MATT_COW = "Matt Cow";
  private static final String USERNAME_NEW_GUY = "New Guy";
  private static final Integer[] ACCOUNTS_JOHN_DOE = {12345, 12346, 12347};
  private static final Integer[] ACCOUNTS_MATT_COW = {12348, 12349};
  private static final Integer[] ACCOUNTS_NEW_GUY = {12350, 12351};
  private static double DEFAULT_ACCOUNT_BALANCE = 100.0;
  private static double DEFAULT_DEBIT_AMOUNT = 5.00;
  private static Integer DEFAULT_TRANSACTION_COUNT = 0;
  private static Integer DISABLED_ACCOUNT_NUMBER = 12346;
  private static Integer STUDENT_PLAN_ACCOUNT_NUMBER = 12347;

  /**
   * Holds on to the referance to stdout printstream
   */
  private PrintStream oldConsoleOut;
  
  /**
   * Storage of stdout data
   */
  private final ByteArrayOutputStream consoleOutContent = new ByteArrayOutputStream();
  
  /**
   * Number to test console out scheme
   */
  private final Integer numberForConsoleOutTest = 0;
  
  /**
   * Test objects
   */
  private final TransactionCalculator transactionCalculator = new TransactionCalculator();
  
  /**
   * Stack of commands to test
   */
  private final Vector<Transaction> transactionList = new Vector<Transaction>();
  
  /**
   * Initializes database.
   */
  @Before
  public void setupCalculator() {
    // Initialize customer data 1
    ArrayList<Account> john_does_stuff = new ArrayList<Account>();
    for(int accountNumber : ACCOUNTS_JOHN_DOE) {
      john_does_stuff.add(genAccount(accountNumber));
    }
    
    // Initialize customer data 2 
    ArrayList<Account> matt_cows_stuff = new ArrayList<Account>();
    for(int accountNumber : ACCOUNTS_MATT_COW) {
      matt_cows_stuff.add(genAccount(accountNumber));
    }
    
    // Assemble account table 
    Map<String, ArrayList<Account>> accountTable = new HashMap<String, ArrayList<Account>>();
    accountTable.put(USERNAME_JOHN_DOE, john_does_stuff);
    accountTable.put(USERNAME_MATT_COW, matt_cows_stuff);
    
    // Finarry
    transactionCalculator.setAccountTable(accountTable);
  }
  
  /**
   * Swaps out old stdout destination to storage
   */
  @Before
  public void setupStreams() {
    oldConsoleOut = System.out;
    System.setOut(new PrintStream(consoleOutContent));
  }
  
  /**
   * Restores previous state of stdout
   */
  @After
  public void cleanupStreams() {
    System.setOut(oldConsoleOut);
  }
  
  /**
   * Routine to report unexpected exceptions e.g. null pointer, etc.
   */
  public void failUnexpectedException(Exception e) {
    e.printStackTrace(System.err);
    fail(String.format("Exception was thrown:%n%s%ncause: %s", e.toString(), e.getCause()));
  }
  
  /**
   * Based on the number, setup account parameters.
   */
  private Account genAccount(int accountNumber) {
    Account account = new Account();
    account.number = accountNumber;
    account.isActive = accountNumber != DISABLED_ACCOUNT_NUMBER;
    account.balance = DEFAULT_ACCOUNT_BALANCE;
    account.transactionCount = DEFAULT_TRANSACTION_COUNT;
    account.isStudentPlan = accountNumber == STUDENT_PLAN_ACCOUNT_NUMBER;
    return account;
  }
  
  /**
   * Transaction constructor stub(?)
   */
  private Transaction makeTransaction(int code, String accountName, int accountNumber, double amount, String misc) {
    Transaction transaction = new Transaction();
    transaction.code = code;
    transaction.accountName = accountName;
    transaction.accountNumber = accountNumber;
    transaction.amount = amount;
    transaction.misc = misc;
    return transaction;
  }
  
  /**
   * Initialize transaction with nothing but the code.
   */
  private Transaction makeTransaction(int code) {
    return makeTransaction(code, "", 0, 0.0, "");
  }
  
  /**
   * Ensures statement, decision, & loop coverage of 'applyTransactions'
   */
  @Test
  public void checkApplyTransactions() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  /**
   * Ensures statement, decision, & loop coverage of 'getAccount'
   *
   * <p><h1>Coverage:</h1><ul><li>
   *   Covers statements:<ul><li>
   *     (Assignment and for check always occurs for any input)</li><li>
   *     S1 Account number check (if statement)</li><li>
   *     S2 Successful return of account</li><li>
   *     S3 Unsuccessful return of null</li></ul></li><li>
   *   Covers decisions:<ul><li>
   *     D1 Block executed when account number checked DOES match the input
   *          transaction's account number</li></ul></li><li>
   *   Covers Loops:<ul><li>
   *     L1-1 Examines one account</li><li>
   *     L1-2 Examines two accounts</li><li>
   *     L1-* Examines many accounts</li></ul></li></ul><p>
   *
   * As the bank system is not expected to have customer names with zero 
   * accounts, there is no coverage for the L1 loop to execute zero times.
   * </p><p>
   *
   * The experiment will be to apply a typical withdrawal transaction on one of
   * John Doe's accounts. The independent variable is whether to use the first,
   * second, third, or some nonexistent account. </p><p>
   *
   * Whether the account CAN be found will influence whether D1 is executed, and
   * consequentally whether the S3 return statement can be reached.
   * Additionally, since the L1 for loop will scan the customer's accounts in
   * order, the account number will determine how many times the loop will 
   * execute. </p></p>
   *
   * The outputs tested are the absence or presence of error messages, as well
   * as whether the account was modified (determined by checking whether the
   * transaction count of the current account is 0 or 1).</p><ul><li>
   *  The D1 block is known to be executed IFF no error messages have occurred.
   *    </li><li>
   *  The loop is only known to have checked all accounts up to the input one
   *   IFF it is shown that a transaction has been applied to it.</li></ul><p>
   *
   * The tests are as follows:</p><table><tr><th>
   *    Name</th><th> Account</th><th>        Error?</th><th> Coverage</th></tr><td>
   *    T1</td><td>   first</td><td>          no</td><td>     S1 S2 D1 L1-1</td></tr><td>
   *    T2</td><td>   invalid(00000)</td><td> yes</td><td>    S3 L1-*</td></tr><td>
   *    T3</td><td>   second</td><td>         no</td><td>     L1-2</td></tr></table>
   */
  @Test
  public void checkGetAccount() {
    try {
      doCheckGetAccountTest(0, false); //T1
      doCheckGetAccountTest(null, true); //T2
      doCheckGetAccountTest(1, false); //T3
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  
  /**
   * Ensures statement, decision, & loop coverage of 'getTransactionFee'
   */
  @Test
  public void checkGetTransactionFee() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  
  /**
   * Ensures statement, decision, & loop coverage of 'accountNumberExists'
   */
  @Test
  public void checkAccountNumberExists() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  
  /**
   * Ensures statement, decision, & loop coverage of 'handleLogin'
   */
  @Test
  public void checkHandleLogin() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  
  /**
   * Ensures statement, decision, & loop coverage of 'handleLogout'
   */
  @Test
  public void checkHandleLogout() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  
  /**
   * Ensures statement, decision, & loop coverage of 'handleWithdrawal'
   */
  @Test
  public void checkHandleWithdrawal() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  /**
   * Ensures statement, decision, & loop coverage of 'handleTransfer'
   */
  @Test
  public void checkHandleTransfer() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  
  /**
   * Ensures statement, decision, & loop coverage of 'handlePaybill'
   */
  @Test
  public void checkHandlePaybill() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  
  /**
   * Ensures statement, decision, & loop coverage of 'handleDeposit'
   */
  @Test
  public void checkHandleDeposit() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  
  /**
   * Ensures statement, decision, & loop coverage of 'handleChangePlan'
   */
  @Test
  public void checkHandleChangePlan() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  
  /**
   * Ensures statement, decision, & loop coverage of 'handleDelete'
   */
  @Test
  public void checkHandleDelete() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  
  /**
   * Ensures statement, decision, & loop coverage of 'handleCreate'
   */
  @Test
  public void checkHandleCreate() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  
  /**
   * Ensures statement, decision, & loop coverage of 'handleDisable'
   */
  @Test
  public void checkHandleDisable() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  
  /**
   * Ensures statement, decision, & loop coverage of 'handleEnable'
   */
  @Test
  public void checkHandleEnable() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  /**
   * Confirms that the stdout storage used in other tests works as expected.
   */
  @Test
  public void testNothing() {
    assertNotNull(System.out);
    assertNotNull(numberForConsoleOutTest);
    System.out.println(numberForConsoleOutTest);
    assertEquals("Failed to read stdout properly", String.format("%s%n", numberForConsoleOutTest.toString()), consoleOutContent.toString());
    System.out.println(numberForConsoleOutTest);
    assertEquals("Failed to read stdout in entirety", String.format("%s%n%s%n", numberForConsoleOutTest.toString(), numberForConsoleOutTest.toString()), consoleOutContent.toString());
  }
  
  /**
   * Confirms, along with testNothing, that the storage is reset as expected for
   * each test.
   */
  @Test
  public void testNothingEvenStill() {
    assertNotNull(System.out);
    assertNotNull(numberForConsoleOutTest);
    System.out.print(numberForConsoleOutTest);
    assertEquals("Failed to read stdout once", String.format("%s", numberForConsoleOutTest.toString()), consoleOutContent.toString());
  }
  
  /**
  * Extracts one of the named customer's accounts.
  * @param name Customer's name
  * @param whichOne 0 indicates first, 1 indicates second, etc.
  */
  private Account getAccount(String name, int whichOne) {
    Map<String, ArrayList<Account>> accountTable = transactionCalculator.getAccountTable();
    return accountTable.get(name).get(whichOne);
  }
  
  /**
  * Tests with a standard withdrawal to one of John Doe's accounts.
  *
  * @param whichAccount 0 Indicates first, 1 indicates second, etc.
  *                      When null, account number is input as 00000
  * @param expectError Whether the 'getAccount' function should succeed or
  *                       fail.
  * @see The checkGetAccount function has a more detailed explanation of the
  *       experiment's conditions. 
  */
  private void doCheckGetAccountTest(Integer whichAccount,
                                        boolean expectError) {
    // process transaction
    int accountNumber = whichAccount == null ? 0 : ACCOUNTS_JOHN_DOE[whichAccount];
    transactionList.add(makeTransaction(TransactionType.LOGIN, "admin", 0, 0.0, ""));
    transactionList.add(makeTransaction(TransactionType.WITHDRAWAL,
                                        USERNAME_JOHN_DOE, accountNumber,
                                        DEFAULT_DEBIT_AMOUNT, ""));
    transactionList.add(makeTransaction(TransactionType.LOGOUT));
    transactionCalculator.applyTransactions(transactionList);
    
    // check if any errors occured
    boolean errorOccured = consoleOutContent.size() != 0;
    assertEquals("Unexpected getAccount result:", expectError, errorOccured);
    
    // check if account was modified correctly
    if(!expectError) {
      Account account = getAccount(USERNAME_JOHN_DOE, whichAccount);
      assertEquals(String.format("Account %d: was not modified",
                                  accountNumber), 1, account.transactionCount);
    }
    
    // cleanup for next times
    transactionList.clear();
  }
}