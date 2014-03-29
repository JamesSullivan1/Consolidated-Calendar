package test;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestCalendar.class, TestEvent.class, TestICSDownload.class,
		TestParser.class })
public class AllTests {

	public static void main(String[] args) throws Exception {
		JUnitCore.main("test.AllTests");
	}

}
