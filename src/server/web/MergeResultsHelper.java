package server.web;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import server.framework.*;

/**
 * Main class that provides all java functionality for feedResults.jsp.
 * 
 * @author James
 */
public class MergeResultsHelper {

	/**
	 * Shows the found events that will be sent to the output calendar.
	 * 
	 * @param session server session object
	 * @param out
	 */
	public static void showResults(HttpSession session, JspWriter out) {
		//Verify results are ready to show, otherwise show loading.
		if (!ThreadHelper.isThreadsFinished(session)) {
			try {
				out.println("</br>Events still loading. Page will refresh when finished.");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Print out any encountered parsing errors.
		@SuppressWarnings("unchecked")
		ArrayList<String> errors = (ArrayList<String>)session.getAttribute("parseErrors");
		if (errors != null)
			for (String error : errors)
				try {
					out.println(error+"</br>");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	
		// Get Consolidated session calendar if it exists
		Calendar consolidated = (Calendar) session
				.getAttribute("consolidatedCalendar");

		if (consolidated.getEvents().isEmpty()) {
			try {
				out.println("No events found that are not already in your Calendar.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				out.println("<h2>Found " + consolidated.getEvents().size()
						+ " new events.</h2>");
				for (Event e : consolidated.getEvents()) {
					if (e != null) {
						out.println("<li>" + e.toString() + "</li>");
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
