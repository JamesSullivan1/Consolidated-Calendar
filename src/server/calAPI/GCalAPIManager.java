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

public class GCalAPIManager {

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
	public static com.google.api.services.calendar.Calendar getClient(
			HttpSession session) throws IOException {

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
	public static Calendar fetchGCal(
			com.google.api.services.calendar.Calendar client)
			throws IOException {

		// retrieve primary calendar
		com.google.api.services.calendar.model.Calendar primeCal = client
				.calendars().get("primary").execute();
		String calname = primeCal.getSummary();
		String servId = primeCal.getId();

		// create google event list
		com.google.api.services.calendar.model.Events feed = client.events()
				.list("primary").execute();
		List<Event> eventList = feed.getItems();

		// create our event list
		ArrayList<server.framework.Event> e = new ArrayList<server.framework.Event>();

		// Convert events
		for (Event event : eventList) {
			Date startDate = new Date(event.getStart().getDateTime().getValue());
			Date endDate = new Date(event.getEnd().getDateTime().getValue());
			String summary = event.getSummary();
			String location = event.getLocation();

			server.framework.Event temp = new server.framework.Event(summary,
					location, startDate, endDate);
			e.add(temp);

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
	public static void addEvents(ArrayList<server.framework.Event> e,
			com.google.api.services.calendar.Calendar client)
			throws IOException {
		// Get list of google events from our events
		ArrayList<Event> newEvents = convertEvents(e, client);

		// retrieve primary calendar
		com.google.api.services.calendar.model.Calendar primeCal = client
				.calendars().get("primary").execute();

		// Add new events
		for (Event event : newEvents) {
			client.events().insert("primary", event).execute();
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
	private static ArrayList<Event> convertEvents(
			ArrayList<server.framework.Event> e,
			com.google.api.services.calendar.Calendar client) {
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
				end.setHours(end.getHours() + 1);

				temp.setEnd(new EventDateTime().setDateTime(new DateTime(end)));
			} else {
				temp.setEnd(new EventDateTime().setDateTime(new DateTime(event
						.getEndDate())));
			}

			newEvents.add(temp);
		}
		return newEvents;
	}

}
