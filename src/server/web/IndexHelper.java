package server.web;

import java.net.URL;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import server.exception.AuthenticationException;
import server.framework.Calendar;
import server.oauth.GoogleAuthHelper;

import com.google.api.client.auth.oauth2.Credential;

/**
 * Main class that provides all java functionality for index.jsp.
 * 
 * @author Daniel Wehr
 */
public class IndexHelper {

	/**
	 * Checks for a returned authentication code in request and if found,
	 * converts it to an authentication token to be stored in the user session.
	 * 
	 * @param request
	 *            server request object
	 * @param session
	 *            server session object
	 * @throws AuthenticationException
	 *             On error getting a valid authentication token from Google.
	 */
	public static void processGoogleAuth(HttpServletRequest request,
			HttpSession session) throws AuthenticationException {
		String code = request.getParameter("code");
		String requestState = request.getParameter("state");
		String sessionState = (String) session.getAttribute("stateString");

		// Google has returned authentication code. Convert and store
		if (code != null && requestState != null
				&& requestState.equals(sessionState)) {
			session.removeAttribute("stateString");

			// Convert auth code to auth token.
			GoogleAuthHelper helper = new GoogleAuthHelper();
			Credential credential = helper.getAuthToken(code);

			// Store token in session.
			session.setAttribute("authCredential", credential);
		} else {
			return;
		}

		// Start thread to process calendar info from google
		AuthThread authThread = new AuthThread(session);
		authThread.start();
	}

	/**
	 * Passes a request to change the target page to the user session so that
	 * the new target will be used on the next/current page load.
	 * 
	 * @param request
	 *            server request object
	 * @param session
	 *            server session object
	 */
	public static void processPageTarget(HttpServletRequest request,
			HttpSession session) {
		String pageTarget = (String) request.getParameter("pageTarget");

		// Check if new page request exists
		if (pageTarget == null) {
			// If no request, grab the current page saved in session.
			pageTarget = (String) session.getAttribute("pageTarget");
			if (pageTarget == null) {
				pageTarget = "feedSelection.jsp"; // Default page target
			}
		}
		session.setAttribute("pageTarget", pageTarget);
	}

	/**
	 * Processes a log out request and removes the google authentication
	 * credential object from the user session if it exists.
	 * 
	 * @param request
	 *            server request object
	 * @param session
	 *            server session object
	 */
	public static void processLogOut(HttpServletRequest request,
			HttpSession session) {
		if (request.getParameter("logOut") != null)
			session.removeAttribute("authCredential");
	}

	/**
	 * Initialize session required session vars that will be used throughout the
	 * program.
	 * 
	 * @param session
	 *            server session object
	 */
	public static void init(HttpSession session) {
		Boolean initDone = (Boolean) session.getAttribute("initDone");
		if (initDone == null || !initDone) {
			session.setAttribute("icsList", new ArrayList<URL>());
			session.setAttribute("consolidatedCalendar",
					new Calendar.CalendarBuilder("Consolidated", null)
							.withService("Consolidated-Cal").build());
			session.setAttribute("threadCount", 0);
			session.setAttribute("parseErrors", new ArrayList<String>());
			session.setAttribute("initDone", new Boolean(true));
		}
	}
}
