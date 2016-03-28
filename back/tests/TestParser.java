package tests;

import banksys.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Vector;
import java.util.Iterator;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

public class TestParser {
    


    //parseMaster method

    @Test
    // Tests the parseMaster method with an input that's active and a student
    public void testActStu() {	
        FileParser parser = new FileParser();
        Map<String, ArrayList<Account>> accounts = parser.parseMaster("tests/testParser1.txt");
        
        for(Map.Entry<String, ArrayList<Account>> entry : accounts.entrySet()) {
            String name = entry.getKey();
            ArrayList<Account> account = entry.getValue();
            for(int i = 0; i < account.size(); i++) {
                assertEquals("John Doe", name);
                assertEquals(12345, account.get(i).number);
                assertEquals(true, account.get(i).isActive);
                assertEquals(110.00, account.get(i).balance, 0.0);
                assertEquals(0000, account.get(i).transactionCount);
                assertEquals(true, account.get(i).isStudentPlan);
            }
        }
   }

    @Test
    // Tests the parseMaster method with an input that's disabled and a non-student
    public void testDisNon() {   
        FileParser parser = new FileParser();
        Map<String, ArrayList<Account>> accounts = parser.parseMaster("tests/testParser2.txt");
      
        for(Map.Entry<String, ArrayList<Account>> entry : accounts.entrySet()) {
            String name = entry.getKey();
            ArrayList<Account> account = entry.getValue();
            for(int i = 0; i < account.size(); i++) {
                assertEquals("Matt Cow", name);
                assertEquals(12346, account.get(i).number);
                assertEquals(false, account.get(i).isActive);
                assertEquals(10010.42, account.get(i).balance, 0.0);
                assertEquals(1030, account.get(i).transactionCount);
                assertEquals(false, account.get(i).isStudentPlan);
            }
        }
   }

    @Test
    // Tests the parseMaster method with an input that's the END_OF_FILE
    public void testEnd() {   
        FileParser parser = new FileParser();
        Map<String, ArrayList<Account>> accounts = parser.parseMaster("tests/testParser3.txt");
        assertEquals(true, accounts.isEmpty());
    }



    // concatTrans method

    @Test
    // Tests one file being concatenated
    public void testOneConcat() {
        final File expected = new File("tests/correctConcat1.txt");
        List<String> files = new ArrayList<String>();    
        files.add("tests/trans1.txt");
        
        FileParser parser = new FileParser();
        parser.concatTrans(files);
        File file = new File("concat.txt");
        File actual = new File("tests/actualConcat1.txt");
        file.renameTo(actual);
        try { 
            BufferedReader in = new BufferedReader(new FileReader(actual));
            List<String> actualLines = new ArrayList<String>();
            List<String> correctLines = new ArrayList<String>();

            String line;
            while((line = in.readLine()) != null) {
                actualLines.add(line);
            }
            in.close();
            
            in = new BufferedReader(new FileReader(expected));
            while((line = in.readLine()) != null) {
                correctLines.add(line);
            }
            in.close();

            assertEquals(correctLines, actualLines);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    // Tests three files are being concatenated
    public void testThreeConcat() {
        final File expected = new File("tests/correctConcat2.txt");
        List<String> files = new ArrayList<String>();    
        files.add("tests/trans1.txt");
        files.add("tests/trans2.txt");
        files.add("tests/trans3.txt");
        
        FileParser parser = new FileParser();
        parser.concatTrans(files);
        File file = new File("concat.txt");
        File actual = new File("tests/actualConcat2.txt");
        file.renameTo(actual);
        try { 
            BufferedReader in = new BufferedReader(new FileReader(actual));
            List<String> actualLines = new ArrayList<String>();
            List<String> correctLines = new ArrayList<String>();

            String line;
            while((line = in.readLine()) != null) {
                actualLines.add(line);
            }
            in.close();
            
            in = new BufferedReader(new FileReader(expected));
            while((line = in.readLine()) != null) {
                correctLines.add(line);
            }
            in.close();

            assertEquals(correctLines, actualLines);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    // Tests that a transaction was correctly parsed
    public void testTrans() {
        FileParser parser = new FileParser();
        Vector<Transaction> transactions = new Vector<Transaction>(parser.parseTrans("tests/correctTrans.txt")); 
        
        Iterator<Transaction> itr = transactions.iterator();
        while(itr.hasNext()){
            Transaction trans = itr.next();
            
            assertEquals(10, trans.code);
            assertEquals("admin", trans.accountName);
            assertEquals(00000, trans.accountNumber);
            assertEquals(0.00, trans.amount, 0.0);
            assertEquals("A ", trans.misc);

        }
    }


}
