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
  private static final Integer[] ACCOUNTS_JOHN_DOE = {12345, 12346, 12347, 12360, 12361};
  private static final Integer[] ACCOUNTS_MATT_COW = {12348, 12349};
  private static final Integer[] ACCOUNTS_NEW_GUY = {12350, 12351};
  private static double DEFAULT_ACCOUNT_BALANCE = 100.0;
  private static double DEFAULT_DEBIT_AMOUNT = 5.00;
  private static double WEIRD_WITHDRAWAL_AMOUNT = 1.00;
  private static Integer DEFAULT_TRANSACTION_COUNT = 0;
  private static Integer DISABLED_ACCOUNT_NUMBER = 12346;
  private static Integer STUDENT_PLAN_ACCOUNT_NUMBER = 12347;
  private static Integer BAD_TRANSACTION_ACCOUNT_NUMBER = 12360;
  private static Integer HUGE_TRANSACTION_ACCOUNT_NUMBER = 12361;
  private static int NEGATIVE_TRANSACTION_VALUE = -1;
  private static int MAX_TRANSACTION_VALUE = 9999;

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
    account.transactionCount = accountNumber == BAD_TRANSACTION_ACCOUNT_NUMBER ?
                                                NEGATIVE_TRANSACTION_VALUE
                                                : DEFAULT_TRANSACTION_COUNT;
    account.transactionCount = accountNumber == HUGE_TRANSACTION_ACCOUNT_NUMBER ?
                                                MAX_TRANSACTION_VALUE
                                                : DEFAULT_TRANSACTION_COUNT;
    account.isStudentPlan = accountNumber == STUDENT_PLAN_ACCOUNT_NUMBER;
    return account;
  }
  
  /**
   * Transaction constructor stub(?)
   */
  private Transaction makeTransaction(int code, String accountName,
                                      int accountNumber, double amount,
                                      String misc) {
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
   *
   * <h1>Coverage:</h1><ul><li>
   *   Covers statements:<ul><li>
   *     (Everything up to "start scanning" will always execute)</li><li>
   *     (Statements in switch cases are equivalent to decision coverage points,
   *      are listed there)</li><li>
   *     (In the transaction count block, the statement checking for null
   *      accounts should ALWAYS pass, it is only there for debugging purposes.
   *      Therefore, it will not be included in statement coverage.)</li><li>
   *     S1 Begin iterating through transactions</li><li>
   *     S2 Get current transaction </li><li>
   *     S3 Add to transaction count</li><li>
   *     S4 Transaction count check</li><li>
   *     S5 Growth of transaction count</li></ul></li><li>
   *   Covers decisions:<ul><li>
   *     D1           Response to null accountTable</li><li>
   *     DLogin       Handling of 'login' transaction</li><li>
   *     DLogout      Handling of 'logout' transaction</li><li>
   *     DWithdrawal  Handling of 'withdrawal' transaction</li><li>
   *     DTransfer    Handling of 'transfer' transaction</li><li>
   *     DPaybill     Handling of 'paybill' transaction</li><li>
   *     DDeposit     Handling of 'deposit' transaction</li><li>
   *     DChangeplan  Handling of 'changeplan' transaction</li><li>
   *     DDelete      Handling of 'delete' transaction</li><li>
   *     DCreate      Handling of 'create' transaction</li><li>
   *     DDisable     Handling of 'disable' transaction</li><li>
   *     DEnable      Handling of 'enable' transaction</li><li>
   *     DDefault     Response to unknown transaction code</li><li>
   *     D2           Incrementation of the transaction count</li><li>
   *     D3           Response to invalid existing transaction count.</li></ul></li><li>
   *   Covers Loops:<ul><li>
   *     L1-0 No transactions are processed.</li><li>
   *     L1-1 One transaction is processed.</li><li>
   *     L1-2 Two transactions are processed.</li><li>
   *     L1-3 Login, something, Logout with 3 transactions.</li><li>
   *     L1-4 Login, something x2, Logout with 4 transactions</li><li>
   *     L1-* Many transactions are processed in between login/logout.</li></ul></li></ul>
   *
   * <p> The procedure for this test is to feed applyTransactions certain
   *  sequences of transactions, so that we can be certain that each of the
   *  above statements, decision blocks, and loop cases are covered. To
   *  determine with confidence whether the expected lines were covered, it is
   *  nessecary to define expectations for the presence and content of error
   *  messages, whether successful transactions consumed the transactions stack
   *  or not, and finally whether the state of the account table has changed. 
   *  Each test uses some combination of these to narrow down the possible
   *  statements that were hit.</p>
   * 
   * <h1>Tests</h1><ul><li>
   *  <b>Test name, transaction sequence, error message, sequence afterwards,
   *       cases covered, other input</b></li><li>
   *     T1, [Empty], no error, L1-0 & S1</li><li>
   *     T2, [Login as admin], invalid transaction, L1-1 & S2</li><li>
   *     T3, [Login as admin, Logout], no error, L1-2</li><li>
   *     T4, [Login as admin, withdraw from 12345, Logout], no error,
   *          L1-3 & S3 & S5 & D2
   *          transaction count would
   *          become 1</li><li>
   *     T5, [Login as admin, withdraw from 12345, withdraw again, Logout],
   *          no error, L1-4, transaction count would become 2</li><li>
   *     T6, [Login as admin, withdraw from 12345, withdraw again, withdraw
   *          again, Logout], no error, L1-5, transaction count would become 3
   *          </li><li>
   *     T7, [Login as admin, withdraw from the invalid transaction count
   *          account], Invalid transaction count error, S4</li><li>
   *     T8, [Empty], Null account table error, D1</li><li>
   *     T9, [Login as admin, Login as admin], Can't login twice error,
   *          DLogin</li><li>
   *     T10, [Logout], Can't logout twice error, DLogout</li><li>
   *     T11, [Login as admin, Withdraw non-canadian amount from 12345],
   *          Invalid withdrawal amount error, DWithdrawal</li><li>
   *     T12, [Login as admin, Transfer from John Doe to Matt Cow with different
               amounts],
   *          Mismatched transfer amounts error, DTransfer</li><li>
   *     T13, [Login as admin, Pay bill on 12345 to TV, Logout], no error, 
   *          DPaybill, 12345 has reduced balance</li><li>
   *     T14, [Login as admin, Deposit to 12345, Logout], no error,
   *          DDeposit, amount of money is raised in 12345
   *          </li><li>
   *     T15, [Login as admin, Change plan of 12345, Logout], no error,
   *          DChangeplan, plan flag is changed</li><li>
   *     T16, [Login as admin, Delete 12345, Logout], no error, DChangeplan,
   *          12346 becomes first account of John Doe</li><li>
   *     T17, [Login as admin, Create New Guy, Logout, Login as New Guy, Logout]
   *          no error, DCreate</li><li>
   *     T18, [Login as admin, Disable 12345, Logout], no error,
   *          DDisable, 12345 now has isActive == false</li><li>
   *     T19, [Login as admin, Enable Disabled Account, Logout], no error,
   *          DEnable, Disabled account now has isActive == true</li><li>
   *     T20, [Login as admin, use nonexistent code (-1)], Invalid code error,
   *          DDefault</li><li>
   *     T21, [Login as admin, withdraw from big transaction code guy], overflow
   *          transaction code error, N/A</li></ul>
   */
   /*
   * TODO: 
   * Make skeleton of function so this one will be as neat as the getAccount one
   */
  @Test
  public void checkApplyTransactions() {
    try { 
      // T1 (no loop)
      doApplyTransactionsTest(transactionList, "");
      
      // T2 (loop once)
      transactionList.clear();
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(new Vector<Transaction>(transactionList.subList(1,
                                                  transactionList.size() - 1)),
                      TransactionCalculator.ERROR_INVALID_TRANSACTION_SEQUENCE);
      
      // T3 (in & out)
      transactionList.clear();
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList, "");
      
      // T4 (1 withdrawal)
      transactionList.clear();
      transactionList.add(makeTransaction(TransactionType.WITHDRAWAL, 
                          USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0],
                          DEFAULT_DEBIT_AMOUNT, ""));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList, "");
      assertEquals("Expected transaction count to change", 1, 
        getAccount(USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0]).transactionCount);
      
      // T5 (2 withdraw)
      transactionList.clear();
      transactionList.add(makeTransaction(TransactionType.WITHDRAWAL, 
                          USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0],
                          DEFAULT_DEBIT_AMOUNT, ""));
      transactionList.add(makeTransaction(TransactionType.WITHDRAWAL, 
                          USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0],
                          DEFAULT_DEBIT_AMOUNT, ""));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList, "");
      assertEquals("Expected transaction count to change", 2, 
        getAccount(USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0]).transactionCount);
                          
      
      // T6 (3 withdraw)
      transactionList.clear();
      transactionList.add(makeTransaction(TransactionType.WITHDRAWAL, 
                          USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0],
                          DEFAULT_DEBIT_AMOUNT, ""));
      transactionList.add(makeTransaction(TransactionType.WITHDRAWAL, 
                          USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0],
                          DEFAULT_DEBIT_AMOUNT, ""));
      transactionList.add(makeTransaction(TransactionType.WITHDRAWAL, 
                          USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0],
                          DEFAULT_DEBIT_AMOUNT, ""));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList, "");
      assertEquals("Expected transaction count to change", 3, 
        getAccount(USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0]).transactionCount);
        
      // T7 test for invalid (negative) transaction count
      transactionList.clear();
      transactionList.add(makeTransaction(TransactionType.WITHDRAWAL, 
            USERNAME_JOHN_DOE, BAD_TRANSACTION_ACCOUNT_NUMBER,
            DEFAULT_DEBIT_AMOUNT, ""));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList,
            TransactionCalculator.ERROR_NEGATIVE_TRANSACTION_COUNT);
      
      // Skip to T9 for now (instead of T8, save that one for last)
      // Confirm login reached
      transactionList.clear();
      logSandwich("admin", transactionList);
      transactionList.add(0, transactionList.firstElement());
      doApplyTransactionsTest(transactionList,
            TransactionCalculator.ERROR_LOGIN_TWICE);
            
      // T10 confirm logout reached
      transactionList.clear();
      logSandwich("admin", transactionList);
      transactionList.add(0, transactionList.lastElement());
      doApplyTransactionsTest(transactionList,
            TransactionCalculator.ERROR_LOGOUT_TWICE);
            
      // T11 confirm withdrawal reached
      transactionList.clear();
      transactionList.add(makeTransaction(TransactionType.WITHDRAWAL, 
            USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0],
            WEIRD_WITHDRAWAL_AMOUNT, ""));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList,
        TransactionCalculator.ERROR_NONCANADIAN_WITHDRAWAL_AMOUNT);
      
      // T12 confirm transfer reached
      transactionList.clear();
      transactionList.add(makeTransaction(TransactionType.TRANSFER, 
            USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0],
            WEIRD_WITHDRAWAL_AMOUNT, ""));
      transactionList.add(makeTransaction(TransactionType.TRANSFER, 
            USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0],
            DEFAULT_DEBIT_AMOUNT, ""));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList,
        TransactionCalculator.ERROR_MISMATCHED_TRANSFER_AMOUNT);
        
      // T13 confirm paybill
      transactionList.clear();
      transactionList.add(makeTransaction(TransactionType.PAYBILL,
            USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0], 
            DEFAULT_DEBIT_AMOUNT, "TV"));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList, "");
      assertEquals("Expected balance to change", DEFAULT_DEBIT_AMOUNT, 
        DEFAULT_ACCOUNT_BALANCE
        - getAccount(USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0]).balance, 0.01);
      
      // T14 confirm deposit
      transactionList.clear();
      transactionList.add(makeTransaction(TransactionType.DEPOSIT,
            USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0], 
            DEFAULT_DEBIT_AMOUNT, ""));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList, "");
      assertEquals("Expected balance to change", -DEFAULT_DEBIT_AMOUNT, 
        DEFAULT_ACCOUNT_BALANCE
        - getAccount(USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0]).balance, 0.01);
      
      // T15  check that change plan is reached
      transactionList.clear();
      transactionList.add(makeTransaction(TransactionType.CHANGEPLAN,
            USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0], 
            0.0, "S "));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList, "");
      assertEquals("Expected plan to change", true, 
            getAccount(USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0]).isStudentPlan);
            
      // T17 (skip T16, do second last before T8)
      // ensure creation is reached
      transactionList.clear();
      transactionList.add(makeTransaction(TransactionType.CREATE,
            USERNAME_NEW_GUY, ACCOUNTS_NEW_GUY[0], 
            DEFAULT_ACCOUNT_BALANCE, "N "));
      logSandwich("admin", transactionList);
      transactionList.add(makeTransaction(TransactionType.LOGIN,
            USERNAME_NEW_GUY, 0, 0.0, "S "));
      transactionList.add(makeTransaction(TransactionType.LOGOUT));
      doApplyTransactionsTest(transactionList, "");
      assertEquals("Expected creation stack to complete in full", true, 
                    transactionList.isEmpty());
      
      // T18 ensure coverage of disable
      transactionList.add(makeTransaction(TransactionType.DISABLE,
                          USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0], 0.0, ""));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList, "");
      assertEquals("Expected account to get disabled", false, 
                  getAccount(USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0]).isActive);
                  
      // T19 Ensure coverage of enable
      transactionList.add(makeTransaction(TransactionType.ENABLE,
                          USERNAME_JOHN_DOE, DISABLED_ACCOUNT_NUMBER, 0.0, ""));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList, "");
      assertEquals("Expected account to get enabled", true, 
                  getAccount(USERNAME_JOHN_DOE,
                              DISABLED_ACCOUNT_NUMBER).isActive);
      
      // T20 Ensure coverage of invalid transaction code errors
      transactionList.clear();
      transactionList.add(makeTransaction(-1));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList,
          TransactionCalculator.ERROR_INVALID_TRANSACTION_CODE);
      
      // T21 Ensure coverage of transaction count overflow
      transactionList.clear();
      transactionList.add(makeTransaction(TransactionType.WITHDRAWAL, 
        USERNAME_JOHN_DOE, HUGE_TRANSACTION_ACCOUNT_NUMBER,
        DEFAULT_DEBIT_AMOUNT, ""));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList,
        TransactionCalculator.ERROR_OVERFLOW_TRANSACTION_COUNT);
      
      // Do T16: ensure deletion is reached
      transactionList.clear();
      transactionList.add(makeTransaction(TransactionType.DELETE,
            USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0], 
            0.0, ""));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList, "");
      assertEquals("Expected removal of first account", ACCOUNTS_JOHN_DOE[1], 
            transactionCalculator.getAccountTable().get(USERNAME_JOHN_DOE).get(0));
        
      // Do T8: test with null account table
      transactionCalculator.setAccountTable(null);
      doApplyTransactionsTest(new Vector<Transaction>(), 
                            TransactionCalculator.ERROR_UNSET_ACCOUNT_TABLE);
      
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  /**
   * Ensures statement, decision, & loop coverage of 'getAccount'
   *
   * <h1>Coverage:</h1><ul><li>
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
   * 
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
  * Surrounds transactions with login/logout
  * it both alters the transactions param and returns the pointer.
  */
  private Vector<Transaction> logSandwich(String name, Vector<Transaction> transactions){
    transactions.add(0, makeTransaction(TransactionType.LOGIN, name, 0, 0.0, 
                     name.compareTo("admin") == 0 ? "A " : "S "));
    transactions.add(makeTransaction(TransactionType.LOGOUT));
    return transactions;
  }
  
  /**
  * Resets data, (unless accountTable is null) runs transactions, and compares
  * actual error to expected error.
  */
  private void doApplyTransactionsTest(Vector<Transaction> transactions,
                        String expectedError) {
    fail("apply transactions test not implemented"); //todo
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