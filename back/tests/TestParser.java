package tests;

import banksys.*;

import java.util.ArrayList;
import java.util.Map;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

public class TestParser {

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
}
