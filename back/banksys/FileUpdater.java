package banksys;

import java.util.*;
import java.io.*;

/* 
 * Takes the data structure (name->their accounts) that has had the transactions
 * applied to it and writes the new master accounts' file.
 */

class FileUpdater {

    /*
     * Loops through the data structure (name->their accounts) and writes all of the
     * accounts to a "newMaster.txt" file using the createAccount helper function
     * @param accounts the data structure (name->their accounts)
     */
    public void fileWriter(Map<String, ArrayList<Account>> accounts){
    
        PrintWriter pwriter = null;
        try{
            pwriter = new PrintWriter("newMaster.txt");
            pwriter.close();

            FileWriter fwriter = new FileWriter("newMaster.txt", true);
            BufferedWriter bwriter = new BufferedWriter(fwriter);
            pwriter = new PrintWriter(bwriter);

            for(Map.Entry<String, ArrayList<Account>> entry : accounts.entrySet()){
                String name = entry.getKey();
                ArrayList<Account> account = entry.getValue();
                for(int i = 0; i < account.size(); i++){
                    int num = account.get(i).number;
                    boolean stat = account.get(i).is_active;
                    double bal = account.get(i).balance;
                    int trans = account.get(i).transaction_count;
                    boolean plan = account.get(i).is_student_plan;
                    
                    String updatedAccount = createAccount(name, num, stat, bal, trans, plan);
                    pwriter.println(updatedAccount);
                }
            }
        } catch (Exception e){
            System.out.println(e);
        } finally {
            if(pwriter != null){
                pwriter.close();
            }
        }
    }
    
    /*
     * A helper function for the fileWriter which formats a string (the account) to be written to a file
     * @param name the name of the account holder
     * @param num the account number
     * @param stat the status of the account (active/disabled)
     * @param bal the bank balance
     * @param trans the number of transactions
     * @param plan the status of the account's plan (student/non-student)
     * @return a string that has the formatted information for the account to be written to file
     */
    public String createAccount(String name, int num, boolean stat, double bal, int trans, boolean plan){

        String strName, strNum, strStat, strBal, strTrans, strPlan;

        strName = String.format("%-20s", name);
        strNum = String.format("%05d", num);
        if(stat == true){
            strStat = "A";
        } else {
            strStat = "D";
        }
        strBal = String.format("%05.2f",bal);
        while(strBal.length() < 8){
            strBal = "0" + strBal;
        }
        strTrans = String.format("%04d", trans);
        if(plan == true){
            strPlan = "S";
        } else {
            strPlan = "N";
        }
        
        String account = strNum + " " + strName + " " + strStat + " " + strBal + " " + strTrans + " " + strPlan;
        return account;
    }
}
