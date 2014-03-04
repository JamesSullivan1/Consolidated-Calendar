package server;
import java.net.MalformedURLException;
import java.net.URL;


public class Test {

	public static void main(String[] args) {
		String[] input = null;
		URL url = null;
		try {
			url = new URL("https://d2l.ucalgary.ca/d2l/le/calendar/feed/user/feed.ics?token=a49a4svg4wl7jqq954fa");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		try {
			input = ICSFeedParser.getCalendarData(url);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("FILE NOT FOUND!!!!!!");
		}
		System.out.println(input[0] + " " + input[1]);
	}

}
