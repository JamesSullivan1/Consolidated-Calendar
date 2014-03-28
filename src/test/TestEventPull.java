/**
 * 
 */
package test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

import server.calAPI.APIManager;
import server.calAPI.GoogleCalAPI;
import server.exception.ServiceAccessException;
import server.framework.*;
import server.web.ThreadHelper;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

public class TestEventPull {
	private static final String DATE_FORMAT_STRING = "MM dd HH:mm:ss z yyyy";
	/*
	 * DEFAULT EVENTS
	 * 
	 * Test Event A: Sat Mar 22 11:30:00 MDT 2014 to Sat Mar 22 12:30:00 MDT
	 * 2014
	 * 
	 * Test Event B: Sat Mar 22 12:30:00 MDT 2014 to Sat Mar 22 13:30:00 MDT
	 * 2014
	 * 
	 * Test Event All-Day A: Fri Mar 21 18:00:00 MDT 2014 to Sat Mar 22 18:00:00
	 * MDT 2014
	 * 
	 * Test Event All-Day B: Tue Mar 11 18:00:00 MDT 2014 to Wed Mar 12 18:00:00
	 * MDT 2014
	 */

	private static final String testAName = "Test Event A";
	private static final String testAStart = "03 22 11:30:00 MDT 2014";
	private static final String testAEnd = "03 22 12:30:00 MDT 2014";

	private static final String testBName = "Test Event B";
	private static final String testBStart = "03 22 12:30:00 MDT 2014";
	private static final String testBEnd = "03 22 13:30:00 MDT 2014";

	private static final String testAllDayAName = "Test Event All-Day A";
	private static final String testAllDayAStart = "03 21 18:00:00 MDT 2014";
	private static final String testAllDayAEnd = "03 22 18:00:00 MDT 2014";

	private static final String testAllDayBName = "Test Event All-Day B";
	private static final String testAllDayBStart = "03 11 18:00:00 MDT 2014";
	private static final String testAllDayBEnd = "03 12 18:00:00 MDT 2014";

	private static final String TEST_ACCOUNT = "consolidated.calendar@gmail.com";
	private static final String TEST_PASS = "O2aKBY9B";
	
	private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json";

	public static void testSuccessfulPull(HttpSession session, JspWriter out,
			ArrayList<Event> events) throws IOException {
		// Verify results are ready to show, otherwise show loading.
		if (!ThreadHelper.isThreadsFinished(session)) {
			out.println("</br>Events still loading. Page will refresh when finished.");
			return;
		}
		if (session.getAttribute("authCredential") == null) {
			out.println("</br>Need to reauthenticate. Return to index.jsp");
			return;
		}

		// Create Google API Manager
		APIManager gCalAPIManager = new APIManager(new GoogleCalAPI());

		// Pull calendar from google.
		Calendar primaryGCal = null;
		try {
			primaryGCal = gCalAPIManager.fetch(session);
		} catch (ServiceAccessException e) {
			out.println("</br>Failed to pull events from Google's Server.");
			return;
		}
		
		ArrayList<Event> testEvents = new ArrayList<Event>();
		Event testA;
		Event testB;
		Event testAllDayA;
		Event testAllDayB;
		DateFormat df = new SimpleDateFormat(DATE_FORMAT_STRING);
		try {
			testA = new Event.EventBuilder(testAName, df.parse(testAStart))
					.withEnd(df.parse(testAEnd)).build();
			testEvents.add(testA);
			testB = new Event.EventBuilder(testBName, df.parse(testBStart))
					.withEnd(df.parse(testBEnd)).build();
			testEvents.add(testB);
			testAllDayA = new Event.EventBuilder(testAllDayAName,
					df.parse(testAllDayAStart)).withEnd(
					df.parse(testAllDayAEnd)).build();
			testEvents.add(testAllDayA);
			testAllDayB = new Event.EventBuilder(testAllDayBName,
					df.parse(testAllDayBStart)).withEnd(
					df.parse(testAllDayBEnd)).build();
			testEvents.add(testAllDayB);
		} catch (ParseException e1) {
			return;
		}

		ArrayList<Event> gEvents = primaryGCal.getEvents();
		ArrayList<Event> invalidEvents = new ArrayList<Event>();
		boolean calendarsMatch = true;
		Iterator<Event> iter = events.iterator();

		while (iter.hasNext()) {
			Event e = iter.next();
			if (!gEvents.contains(e)) {
				if (calendarsMatch) {
					calendarsMatch = false;
				}
				invalidEvents.add(e);
			}
		}
		
		iter = testEvents.iterator(); 
		while(iter.hasNext()) {
			Event e = iter.next();
			if(!gEvents.contains(e)) {
				if (calendarsMatch) {
					calendarsMatch = false;
				}
				invalidEvents.add(e);
			}
		}

		out.println("</br><h1><b>Succeeded in pulling the following events:</b></h1>");
		for (Event e : gEvents) {
			out.println("</br>" + e.toString());
		}

		if (!calendarsMatch) {
			out.println("</br><h1><b>Failed something :</b></h1>:");
			for (Event e : invalidEvents) {
				out.println("</br>" + e.toString());
			}
			
		}

	}
	
	/**
	 * Assert that the account connecting is the test account.
	 */
	public static boolean validateAccountIdentity(HttpSession session, JspWriter out) throws IOException{
		HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
		// Construct a credentials request
		final Credential credential = (Credential) session.getAttribute("authCredential");
		final HttpRequestFactory requestFactory = HTTP_TRANSPORT
		.createRequestFactory(credential);
		// Make an authenticated request
		final GenericUrl url = new GenericUrl(USER_INFO_URL);
		final HttpRequest request = requestFactory.buildGetRequest(url);
		request.getHeaders().setContentType("application/json");
		final String jsonIdentity = request.execute().parseAsString();
		
		boolean isValid = jsonIdentity.contains(TEST_ACCOUNT);
		if(!isValid) {
			out.println("</br> Invalid account- use the test account ONLY.");
		}
		return isValid;
	}

}
