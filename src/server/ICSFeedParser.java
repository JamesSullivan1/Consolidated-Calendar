package server;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public final class ICSFeedParser {
	public static ArrayList<String> ParseFeed(String link) throws FileNotFoundException{
		File feed = new File(link);
		Scanner parser = new Scanner(feed);
		parser.useDelimiter(Pattern.compile("\\n"));
		String current = null;
		String servid = null;
		String calname = null;
		String[] splitter = null;
		String eventData[] = new String[4];
		ArrayList<String> parseData = new ArrayList<String>();
		boolean flag = false;
		
		while(parser.hasNext()){
			current = parser.next();
			splitter = current.split(":");

			if(splitter[0].equals("PRODID") && servid == null){
				servid = current.split(":")[1];
				parseData.add(servid);
			} 
			else if(splitter[0].equals("X-WR-CALNAME") && calname == null){
				calname = current.split(":")[1];
				parseData.add(calname);
			}

			if(current.equals("BEGIN:VEVENT\r")){
				flag = true;
			}
			
			while(flag == true){

				current = parser.next();
				splitter = current.split(":");
				
				if(splitter[0].equals("SUMMARY")){
					eventData[0] = splitter[1];
				}else if(splitter[0].equals("LOCATION")){
					eventData[1] = splitter[1];
				}else if(splitter[0].equals("DTSTART")){
					eventData[2] = splitter[1];
				}else if(splitter[0].equals("DTEND")){
					eventData[3] = splitter[1];
				}else if(current.equals("END:VEVENT\r")){
					flag = false;
					for (int i = 0; i < eventData.length; i++) {
						parseData.add(eventData[i]);
					}
				}
				
			}
		}
		parser.close();
		return parseData;
	}
	
//	public ArrayList<String> getCalendarData(ArrayList<String> rawData) {
//		
//	}
//	
//	public ArrayList<Event> getEvents(ArrayList<String> rawData) {
//		
//	}
}
