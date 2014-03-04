package server.web;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import server.oauth.GoogleAuthHelper;
import com.google.api.client.auth.oauth2.Credential;

public class IndexHelper {

	/**
	 * Checks for a returned authentication code and if found, converts it to an
	 * authentication token 
	 * 
	 * @param request
	 * @param session
	 * @param out
	 * @throws IOException
	 */
	public static void processGoogleAuth(HttpServletRequest request, HttpSession session, JspWriter out) throws IOException {
		String code = request.getParameter("code");
		String requestState = request.getParameter("state");
		String sessionState = (String)session.getAttribute("stateString");
		
		//Google has returned authentication code. Convert and store
		if (code != null && requestState != null && requestState.equals(sessionState)) {
			out.println("Found auth code, converting to credential.");
			session.removeAttribute("stateString");
			
			//Convert auth code to auth token.
			GoogleAuthHelper helper = new GoogleAuthHelper();
			Credential credential = helper.getAuthToken(code);
			
			//Store token in session.
			session.setAttribute("authCredential", credential);
		}
		else
			out.println("No auth code to read.");
	}
	
	public static void processPageTarget(HttpServletRequest request, HttpSession session) {
		String pageTarget = (String)request.getParameter("pageTarget");
		if (pageTarget == null) {
			pageTarget = (String)session.getAttribute("pageTarget");
			if (pageTarget == null) {
				pageTarget = "feedSelection.jsp";
			}
		}
		session.setAttribute("pageTarget", pageTarget);
	}
}
