package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestCalendar.class, TestEvent.class, TestICSDownload.class,
		TestParser.class })
public class AllTests {

}
