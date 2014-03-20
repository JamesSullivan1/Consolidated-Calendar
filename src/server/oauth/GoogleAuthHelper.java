package server.oauth;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;

import server.exception.AuthenticationException;
import server.exception.ServiceAccessException;

/**
 * Helper class for OAuth2 Authentication.\n Constructs a redirection link for
 * the user to follow, with a particular scope request and callback URI.\n
 * 
 * 
 * @author james
 * 
 */
public final class GoogleAuthHelper {

	// Application specific values for ID and Secret
	private static final String CLIENT_ID = "956173815322-2uhf88measmqiucl6682187t6dp2bdu6.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "USwY2ADR7v40DvxbF19SE8X1";

	// Scope declaration for authentication access
	private static final Iterable<String> SCOPE = Arrays.asList(
			"https://www.googleapis.com/auth/userinfo.profile",
			"https://www.googleapis.com/auth/userinfo.email",
			"https://www.googleapis.com/auth/calendar");

	// Return URI for google
	private static final String CALLBACK_URI = "http://localhost:8080/Consolidated-Cal/index.jsp";
	// RESTful URI for Calendar info request
	private static final String CALENDAR_LIST_URL = "https://www.googleapis.com/calendar/v3/users/me/calendarList";

	// Utility
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private final GoogleAuthorizationCodeFlow flow;

	// Unique application state token
	private String stateToken;

	/**
	 * Constructor to initialize the Google Authentication Code Flow and
	 * generate a unique state token.
	 */
	public GoogleAuthHelper() {
		flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
				JSON_FACTORY, CLIENT_ID, CLIENT_SECRET,
				(Collection<String>) SCOPE).build();

		generateStateToken();
	}

	/**
	 * Builds a redirection URL for the user that contains the callback URI,
	 * scope, and state token.
	 */
	public String buildLoginUrl() {

		final GoogleAuthorizationCodeRequestUrl url = flow
				.newAuthorizationUrl();

		return url.setRedirectUri(CALLBACK_URI).setState(stateToken).build();
	}

	/**
	 * Generates a secure state token
	 */
	private void generateStateToken() {

		SecureRandom sr1 = new SecureRandom();

		stateToken = "google;" + sr1.nextInt();

	}

	/**
	 * Accessor for state token
	 */
	public String getStateToken() {
		return stateToken;
	}

	/**
	 * Uses a given authentication code to request and return an authentication
	 * token which can be used to push and pull data from Google.
	 * 
	 * @param authCode
	 *            authentication code provided by google
	 * @return Authentication token wrapped in a credential object.
	 * @throws IOException
	 */
	public Credential getAuthToken(final String authCode) throws AuthenticationException {
		final Credential credential;
		try {
		// Gets a response token from Google
		final GoogleTokenResponse response = flow.newTokenRequest(authCode)
				.setRedirectUri(CALLBACK_URI).execute();
		// Construct a credentials request
		credential = flow.createAndStoreCredential(response,
				null);
		} catch(IOException e){
			throw new AuthenticationException(e.getMessage());
		}
		return credential;
	}
}
