package backend;

import java.io.*;
import java.util.*;

class FileParser {

    public static final String ACTIVE = "A";
    public static final String STUDENT = "S";
    public static final String END = "END_OF_FILE";

    public Map<String, ArrayList<Account>> parseMaster(String masterFile){
    
        Map<String, ArrayList<Account>> accounts = new HashMap<String, ArrayList<Account>>();
        try{
            FileReader masterFr = new FileReader(masterFile);
            BufferedReader masterBr = new BufferedReader(masterFr);

            String line;
            while((line = masterBr.readLine()) != null){
                
                String num = line.substring(0, 5);
                String name = line.substring(6, 26);
                String stat = line.substring(27, 28);
                String bal = line.substring(29, 37);
                String trans = line.substring(38, 42);
                String plan = line.substring(43, 44);

                name = name.trim();
    
                if(name.compareTo(END) != 0){
                    Account tempAccount = new Account();

                    tempAccount.number = Integer.parseInt(num);
                    if(stat.compareTo(ACTIVE) == 0){
                        tempAccount.is_active = true;
                    } else {
                        tempAccount.is_active = false;
                    }
                    tempAccount.balance = Double.parseDouble(bal);
                    tempAccount.transaction_count = Integer.parseInt(trans);
                    if(plan.compareTo(STUDENT) == 0){
                        tempAccount.is_student_plan = true;
                    } else {
                        tempAccount.is_student_plan = false;
                    }
                    ArrayList<Account> accountList = accounts.get(name);
                    if(accounts.get(name) == null){
                        accountList = new ArrayList<Account>();
                        accountList.add(tempAccount);
                        accounts.put(name, accountList);
                    } else {
                        accountList.add(tempAccount);
                    }
                }
            }
            masterBr.close();
            return accounts;
        } catch (Exception e){
            System.out.println(e);
            System.exit(0);
        }
        return accounts;
    }

    public void concatTrans(List<String> transFiles){
        
        PrintWriter pwriter = null;
        try{
            pwriter = new PrintWriter("concat.txt");
            pwriter.close();

            FileWriter writer = new FileWriter("concat.txt", true);
            BufferedWriter bwriter = new BufferedWriter(writer);
            pwriter = new PrintWriter(bwriter);
            for(int i = 0; i < transFiles.size(); i++){
                FileReader transFr = new FileReader(transFiles.get(i));
                BufferedReader transBr = new BufferedReader(transFr);
                String line;
                while((line = transBr.readLine()) != null){
                    pwriter.println(line);
                }
                transBr.close();
            }

        } catch (Exception e){
            System.out.println(e);
            System.exit(0);
        } finally {
            if(pwriter != null){
                pwriter.close();
            }
        }
    }

    public List<Transaction> parseTrans(){
        
        List<Transaction> transactions = new ArrayList<Transaction>();
        try{
            FileReader parseFr = new FileReader("concat.txt");
            BufferedReader parseBr = new BufferedReader(parseFr);

            String line;
            while((line = parseBr.readLine()) != null){

                String code = line.substring(0,2);
                String name = line.substring(3, 23);
                String num = line.substring(24,29);
                String amount = line.substring(30, 38);
                String misc = line.substring(39,41);
                
                name = name.trim();

                Transaction tempTrans = new Transaction();
                tempTrans.code = Integer.parseInt(code);
                tempTrans.account_name = name;
                tempTrans.account_number = Integer.parseInt(num);
                tempTrans.amount = Double.parseDouble(amount);
                tempTrans.misc = misc;

                transactions.add(tempTrans);    
            }
            parseBr.close();
            return transactions;
        } catch (Exception e){
            System.out.println(e);
            System.exit(0);
        }
        return transactions;
    }
}
