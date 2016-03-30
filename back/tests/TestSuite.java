package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
@RunWith(Suite.class)
@Suite.SuiteClasses({
   TestParser.class,
   TestUpdater.class,
   TestMain.class,
   TestTransactionCalculator.class
})
public class TestSuite {   
}  
