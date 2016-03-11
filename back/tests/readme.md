javac -cp junit-4.10.jar [ source files ]
java -cp "junit-4.10.jar;;" org.junit.runner.JUnitCore  [whatever ]-- to run

use @Test annotation
can use @Test(expected=RuntimeException.class) or something like that too?

import static.org.junit.Assert*;
import junit.framework.JUnit4TestAdapter;
import org.junit.Test;

assertEquals([expected], [actual])

Should add junit to path?? or maybe flags to 
location of junit? idk

Alex knows how to get it working in eclipse

public static junit.framework.Test suite() {return new JUnitTest4Adapter([w/e].class);
}

will probably have more than 1 test function for each method