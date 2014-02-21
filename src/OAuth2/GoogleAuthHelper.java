package OAuth2;

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

/**
 * Helper class for OAuth2 Authentication API
 * @author james
 *
 */
public final class GoogleAuthHelper {

	// Application specific values for ID and Secret
	private static final String CLIENT_ID = "956173815322-2uhf88measmqiucl6682187t6dp2bdu6.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "USwY2ADR7v40DvxbF19SE8X1";
	
	// Scope declaration for authentication access
	private static final Iterable<String> SCOPE = Arrays.asList("https://www.googleapis.com/auth/userinfo.profile;https://www.googleapis.com/auth/userinfo.email".split(";"));
	// Return URI for google
	private static final String CALLBACK_URI = "http://localhost:8080/Consolidated-Cal/index.jsp";
    // Redirect to google user info page
	private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
    
	
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    
    private String stateToken;
    
    private final GoogleAuthorizationCodeFlow flow;
    
    /**
     * Constructor to initialize the Google Authentication Code Flow with CLIENT_ID, SECRET, and SCOPE
     * using an HTTP_TRANSPORT and JSON_FACTORY.
     */
    public GoogleAuthHelper() {
    	flow = new GoogleAuthorizationCodeFlow.Builder(
    			HTTP_TRANSPORT, 
    			JSON_FACTORY, 
    			CLIENT_ID, 
    			CLIENT_SECRET, 
    			(Collection<String>) SCOPE)
    	.build();
    	
    	generateStateToken();
    }
    
    /**
	 * Builds a login URL based on client ID, secret, callback URI, and scope 
	 */
	public String buildLoginUrl() {

		final GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();

		return url.setRedirectUri(CALLBACK_URI).setState(stateToken).build();
	}

	/**
	 * Generates a secure state token 
	 */
	private void generateStateToken(){

		SecureRandom sr1 = new SecureRandom();

		stateToken = "google;"+sr1.nextInt();

	}

	/**
	 * Accessor for state token
	 */
	public String getStateToken(){
		return stateToken;
	}

	/**
	 * Expects an Authentication Code, and makes an authenticated request for the user's profile information
	 * @return JSON formatted user profile information
	 * @param authCode authentication code provided by Google
	 */
	public String getUserInfoJson(final String authCode) throws IOException {

		// Gets a response token from Google
		final GoogleTokenResponse response = flow.newTokenRequest(authCode).setRedirectUri(CALLBACK_URI).execute();
		// Construct a credentials request
		final Credential credential = flow.createAndStoreCredential(response, null);
		final HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(credential);
		// Make an authenticated request
		final GenericUrl url = new GenericUrl(USER_INFO_URL);
		final HttpRequest request = requestFactory.buildGetRequest(url);
		request.getHeaders().setContentType("application/json");
		final String jsonIdentity = request.execute().parseAsString();

		return jsonIdentity;

	}
	
	
}
