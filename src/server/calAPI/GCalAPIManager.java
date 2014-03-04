package server.calAPI;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

import server.framework.*;

public class GCalAPIManager {
	public static Calendar fetchGCal(HttpTransport httpTransport, JsonFactory JSON_FACTORY, Credential credential, String APPLICATION_NAME){
		
		com.google.api.services.calendar.Calendar client = new com.google.api.services.calendar.Calendar.Builder(
		          httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
		
		ArrayList<Event> e = new ArrayList<Event>();
		
		Calendar c = new Calendar(); // Construct calendar from google data
		return c;
	}
	
	//Google's sample for updating Calendars
	private static Calendar updateCalendar(Calendar newcal, com.google.api.services.calendar.Calendar client) throws IOException {
	    
		Calendar result = client.calendars().patch(newcal.getId(), newcal).execute();
	    return result;
	  }
}
