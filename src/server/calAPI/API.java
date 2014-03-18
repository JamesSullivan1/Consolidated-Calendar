package server.calAPI;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import server.framework.*;

interface API {
	public void addEvents(ArrayList<server.framework.Event> e, HttpSession session);
	public Calendar fetch(HttpSession session);
}
