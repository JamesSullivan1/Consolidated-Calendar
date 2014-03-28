package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import server.web.*;
import server.exception.ServiceAccessException;
import server.framework.*;
import server.calAPI.*;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

public class TestEventPush {
	private static final String TEST_ACCOUNT = "consolidated.calendar@gmail.com";
	private static final String TEST_PASS = "O2aKBY9B";
	private static final long QUARTER_HOUR_MSEC = 900000l;

	public void setup(HttpSession session, JspWriter out) {

	}

	static ArrayList<Event> generateHourEventsA() {
		ArrayList<Event> events = new ArrayList<Event>();
		Date dtStart = new Date();
		long tStart = dtStart.getTime();
		long tEnd = tStart + QUARTER_HOUR_MSEC;
		Date dtEnd = new Date(tEnd);
		for (int i = 0; i < 10; i++) {
			Event a = new Event.EventBuilder(new Integer(i).toString(), dtStart)
					.withEnd(dtEnd).build();
			events.add(a);
			tStart += QUARTER_HOUR_MSEC;
			tEnd += QUARTER_HOUR_MSEC;
			dtStart = new Date(tStart);
			dtEnd = new Date(tEnd);
		}

		return events;
	}

	public static ArrayList<Event> testSuccessfulPushA(HttpSession session,
			JspWriter out) {
		// Verify that the user is authenticated with Google
		if (!ThreadHelper.isThreadsFinished(session)) {
			try {
				out.println("</br>Authentication processing. Page will refresh when finished.");
				return null;
			} catch (IOException e) {
				// Safe to duck - Indicates that the frontend is not responsive.
			}
		}

		ArrayList<Event> a = generateHourEventsA();

		// Create Google API Manager
		APIManager gCalAPIManager = new APIManager(new GoogleCalAPI());
		try {
			gCalAPIManager.addEvents(a, session);
			try {
				out.println("</br>Successful event push:");
				for (Event e : a) {
					out.println(e.toString());
				}
			} catch (IOException e1) {
				// Safe to duck - Indicates that the frontend is not responsive.
			}

		} catch (ServiceAccessException e1) {
			try {
				out.println("</br>Failed to push events:");
				for (Event e : a) {
					out.println("\n " + e.toString());
				}
				return null;
			} catch (IOException e2) {
				// Safe to duck - Indicates that the frontend is not responsive.
			}

		}
		return a;
	}

}
