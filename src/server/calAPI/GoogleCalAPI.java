package server.calAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import server.framework.*;

/**
 * Processes calls to and from the Google Calendar API
 */
public class GoogleCalAPI implements API{

	// Utility
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final String CALENDAR_URL = "https://www.googleapis.com/calendar/v3/calendars";

	/**
	 * Expects an Authentication Code, and makes an authenticated request for
	 * the user's Calendar List.
	 * 
	 * @return JSON formatted Calendar.
	 * @param authCode
	 *            authentication credential provided by Google
	 */
	private com.google.api.services.calendar.Calendar getClient(HttpSession session)
			throws IOException {

		Credential credential = (Credential) session
				.getAttribute("authCredential");
		// Construct http request.
		final HttpRequestFactory requestFactory = HTTP_TRANSPORT
				.createRequestFactory(credential);

		// Make an authenticated request
		final GenericUrl url = new GenericUrl(CALENDAR_URL);
		final HttpRequest request = requestFactory.buildGetRequest(url);
		request.getHeaders().setContentType("application/json");

		com.google.api.services.calendar.Calendar client = new com.google.api.services.calendar.Calendar.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(
						"Consolidated-Cal").build();

		return client;

	}

	// Asks for client and retrieves/parses GCal to our Calendar format
	public Calendar fetch(HttpSession session) {

		// get client's calendar
		com.google.api.services.calendar.Calendar client = null;
		try {
			client = getClient(session);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// retrieve primary calendar
		com.google.api.services.calendar.model.Calendar primeCal = null;
		try {
			primeCal = client
					.calendars().get("primary").execute();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String calname = primeCal.getSummary();
		String servId = primeCal.getId();

		// create google event list
		com.google.api.services.calendar.model.Events feed = null;
		try {
			feed = client.events()
					.list("primary").execute();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		List<Event> eventList = feed.getItems();

		// create our event list
		ArrayList<server.framework.Event> e = new ArrayList<server.framework.Event>();

		// Convert events
		for (Event event : eventList) {
			if (!event.getStatus().equals("cancelled")) {

				Date startDate = null;
				Date endDate = null;
				if (isAllDayEvent(event)) {
					startDate = new Date(event.getStart().getDate().getValue());
					endDate = new Date(event.getEnd().getDate().getValue());
				} else {
					startDate = new Date(event.getStart().getDateTime()
							.getValue());
					endDate = new Date(event.getEnd().getDateTime().getValue());
				}
				String summary = event.getSummary();
				String location = event.getLocation();

				server.framework.Event temp = new server.framework.Event(
						summary, location, startDate, endDate);
				e.add(temp);
			}
		}

		// Construct calendar from google data
		Calendar c = new Calendar(e, calname, servId);
		return c;
	}

	/**
	 * Adds events into the primary google calendar.
	 * 
	 * @param e
	 *            An ArrayList of events predetermined to not be redundant
	 * @param client
	 *            The current google client using the app
	 */
	public void addEvents(ArrayList<server.framework.Event> e, HttpSession session){

		
		// get client's calendar
		com.google.api.services.calendar.Calendar client = null;
		try {
			client = getClient(session);
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		// Get list of google events from our events
		ArrayList<Event> newEvents = convertEvents(e, client);

		// Add new events
		for (Event event : newEvents) {
			try {
				client.events().insert("primary", event).execute();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Converts an ArrayList of our Events into an ArrayList Google Events
	 * 
	 * @param e
	 *            An ArrayList of our Events
	 * @param client
	 *            The current google client using the app
	 */
	private ArrayList<Event> convertEvents(
			ArrayList<server.framework.Event> e,
			com.google.api.services.calendar.Calendar client) {
		
		final int HOUR_IN_MSEC = 3600000;
		ArrayList<Event> newEvents = new ArrayList<Event>();
		
		for (server.framework.Event event : e) {
			Event temp = new Event();
			temp.setSummary(event.getName());
			temp.setLocation(event.getLocation());
			temp.setStart(new EventDateTime().setDateTime(new DateTime(event
					.getStartDate())));
			// Date offset defaults to 1h after start.
			if (event.getEndDate() == null) {
				Date start = event.getStartDate();
				Date end = (Date) start.clone();
				end.setTime(end.getTime() + HOUR_IN_MSEC);

				temp.setEnd(new EventDateTime().setDateTime(new DateTime(end)));
			} else {
				temp.setEnd(new EventDateTime().setDateTime(new DateTime(event
						.getEndDate())));
			}

			newEvents.add(temp);
		}
		return newEvents;
	}

	private boolean isAllDayEvent(Event e) {
		return e.getEnd().getDateTime() == null;
	}

}
