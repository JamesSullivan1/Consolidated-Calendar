package server.calAPI;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import server.framework.Calendar;

public class APIManager {
	private API api;
	
	public APIManager(API a){
		this.api = a;
	}
	
	public void addEvents(ArrayList<server.framework.Event> e, HttpSession session){
		 this.api.addEvents(e, session);
	}
	
	public Calendar fetch(HttpSession session){
		return this.api.fetch(session);
	}
}
