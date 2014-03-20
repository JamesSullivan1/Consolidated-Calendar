package server.calAPI;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import server.framework.*;
import server.exception.*;

interface API {
	public void addEvents(ArrayList<server.framework.Event> e,
			HttpSession session) throws ServiceAccessException;

	public Calendar fetch(HttpSession session) throws ServiceAccessException;
}
