package banksys;

import java.util.*;
import java.io.*;

class FileUpdater {

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
                    int num = account.get(i).num;
                    boolean stat = account.get(i).stat;
                    double bal = account.get(i).bal;
                    int trans = account.get(i).trans;
                    boolean plan = account.get(i).plan;
                    
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
