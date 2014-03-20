package server.calAPI;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import server.framework.*;

interface API {
	public void addEvents(ArrayList<server.framework.Event> e,
			HttpSession session) throws IOException;

	public Calendar fetch(HttpSession session) throws IOException;
}
