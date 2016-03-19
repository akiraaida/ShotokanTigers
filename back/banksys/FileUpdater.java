/*
* FileUpdater.java
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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

/** 
 * Takes the data structure (name->their accounts) that has had the transactions
 * applied to it and writes the new master accounts' file.
 */
class FileUpdater {
  /**
   * Loops through the data structure (name->their accounts) and writes all of
   * the accounts to a "newMaster.txt" file using the createAccount helper
   * function.
   *
   * @param accounts the data structure (name->their accounts)
   */
  protected void fileWriter(Map<String, ArrayList<Account>> accounts) {
    PrintWriter mpwriter = null;
    PrintWriter cpwriter = null;
    try {
      mpwriter = new PrintWriter("newMaster.txt");
      mpwriter.close();
      cpwriter = new PrintWriter("newCurr.txt");
      cpwriter.close();
      
      FileWriter mfwriter = new FileWriter("newMaster.txt", true);
      BufferedWriter mbwriter = new BufferedWriter(mfwriter);
      mpwriter = new PrintWriter(mbwriter);

      FileWriter cfwriter = new FileWriter("newCurr.txt", true);
      BufferedWriter cbwriter = new BufferedWriter(cfwriter);
      cpwriter = new PrintWriter(cbwriter);
      
      for(Map.Entry<String, ArrayList<Account>> entry : accounts.entrySet()) {
        String name = entry.getKey();
        ArrayList<Account> account = entry.getValue();
        for(int i = 0; i < account.size(); i++) {
          int num = account.get(i).number;
          boolean stat = account.get(i).isActive;
          double bal = account.get(i).balance;
          int trans = account.get(i).transactionCount;
          boolean plan = account.get(i).isStudentPlan;
          
          String masterAccount = createAccount(true, name, num, stat, bal,
                                               trans, plan);
          String currAccount = createAccount(false, name, num, stat, bal,
                                             trans, plan);
          mpwriter.println(masterAccount);
          cpwriter.println(currAccount);
        }
      }
      mpwriter.close();
      cpwriter.close();
    } catch (Exception e) {
      System.out.println(e);
    } finally {
      if(mpwriter != null) {
        mpwriter.close();
      }
      if(cpwriter != null) {
        cpwriter.close();
      }
    }
  }
  
  /**
   * A helper function for the fileWriter which formats a string (the account)
   * to be written to a file.
   *
   * @param master Set true, when the file being printed to is newMaster =
   *    accounts file or the current accounts file.
   * @param name The name of the account holder.
   * @param num The account number.
   * @param stat The status of the account (active/disabled).
   * @param bal The bank balance.
   * @param trans The number of transactions.
   * @param plan The status of the account's plan (student/non-student).
   * @return A string that has the formatted information for the account to be
   *    written to file.
   */
  private String createAccount(boolean master, String name, int num, 
      boolean stat, double bal, int trans, boolean plan) {

    String strName = String.format("%-20s", name);
    String strNum = String.format("%05d", num);
    String strStat;
    if(stat == true) {
      strStat = "A";
    } else {
      strStat = "D";
    }
    String strBal = String.format("%05.2f",bal);
    while(strBal.length() < 8) {
      strBal = "0" + strBal;
    }
    String strTrans = String.format("%04d", trans);
    String strPlan;
    if(plan == true) {
      strPlan = "S";
    } else {
      strPlan = "N";
    }
    String account;
    if(master == true) {
      account = strNum + " " + strName + " " + strStat + " " + strBal + " "
          + strTrans + " " + strPlan;
    } else {
      account = strNum + " " + strName + " " + strStat + " " + strBal + " "
          + strPlan;
    }

    return account;
  }
}
