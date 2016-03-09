import java.io.*;
import java.util.*;

public class Backend {

    public static final String ACTIVE = "A";
    public static final String STUDENT = "S";
    public static final String END = "END_OF_FILE         ";

    public static void main(String[] args){
        
        try{
            FileReader masterIn = new FileReader(args[0]);
            BufferedReader masterBr = new BufferedReader(masterIn);

            Map<String, ArrayList<Account>> accounts = new HashMap<String, ArrayList<Account>>();
            String line;
            while((line = masterBr.readLine()) != null){
                
                String num = line.substring(0, 5);
                String name = line.substring(6, 26);
                String stat = line.substring(27, 28);
                String bal = line.substring(29, 37);
                String trans = line.substring(38, 42);
                String plan = line.substring(43, 44);

                if(name.compareTo(END) != 0){
                    Account tempAccount = new Account();

                    tempAccount.num = Integer.parseInt(num);
                    tempAccount.name = name;
                    if(stat == ACTIVE){
                        tempAccount.stat = false;
                    } else {
                        tempAccount.stat = true;
                    }
                    tempAccount.bal = Double.parseDouble(bal);
                    tempAccount.trans = Integer.parseInt(trans);
                    if(plan == STUDENT){
                        tempAccount.plan = true;
                    } else {
                        tempAccount.plan = false;
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

        } catch (Exception e){
            System.out.println(e);
        }
    }
}

class Account {
    
    int num;
    String name;
    boolean stat;
    double bal;
    int trans;
    boolean plan;
}
