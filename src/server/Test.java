package server;
import java.io.FileNotFoundException;


public class Test {

	public static void main(String[] args) {
		ICSFeedParser parser = new ICSFeedParser();
		
		try {
			ICSFeedParser.ParseFeed("C:/Users/Scott/Documents/CPSC/CPSC 301/Consolidated Calendar/src/feed.ics"); //takes local files for now
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("FILE NOT FOUND!!!!!!");
		}
		
	}

}
