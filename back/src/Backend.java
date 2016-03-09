import java.io.*;
import java.util.*;

public class Backend {

    public static final String ACTIVE = "A";
    public static final String STUDENT = "S";
    public static final String END = "END_OF_FILE";

    public static Map<String, ArrayList<Account>> parseMaster(String masterFile){
    
        Map<String, ArrayList<Account>> accounts = new HashMap<String, ArrayList<Account>>();
        try{
            FileReader masterIn = new FileReader(masterFile);
            BufferedReader masterBr = new BufferedReader(masterIn);

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
            return accounts;
        } catch (Exception e){
            System.out.println(e);
        }
        return accounts;
    }

    public static void concatTrans(List<String> transFiles){
        
        PrintWriter pwriter = null;
        try{
            pwriter = new PrintWriter("concat.txt");
            pwriter.close();

            FileWriter writer = new FileWriter("concat.txt", true);
            BufferedWriter bwriter = new BufferedWriter(writer);
            pwriter = new PrintWriter(bwriter);
            for(int i = 0; i < transFiles.size(); i++){
                FileReader transIn = new FileReader(transFiles.get(i));
                BufferedReader transBr = new BufferedReader(transIn);
                String line;
                while((line = transBr.readLine()) != null){
                    pwriter.println(line);
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

    public static void main(String[] args){
        
        if(args.length < 2){
            System.out.println("ERROR, NOT ENOUGH ARGUEMENTS.");
            System.exit(0);
        }

        Map<String, ArrayList<Account>> map = parseMaster(args[0]);
        
        List<String> transFiles = new ArrayList<String>();
        for(int i = 1; i < args.length; i++){
            transFiles.add(args[i]);
        }
        concatTrans(transFiles);
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
