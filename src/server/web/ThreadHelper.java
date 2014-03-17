package server.web;

import javax.servlet.http.HttpSession;

/**
 * @author Daniel
 *
 */
public class ThreadHelper {
	/**
	 * Increases the count for the number of parsing threads running.
	 * 
	 * @param session server session object
	 */
	public static void incrementThreadCount(HttpSession session) {
		synchronized (session) {
			int threadCount = (Integer)session.getAttribute("threadCount");
			threadCount++;
			session.setAttribute("threadCount", threadCount);
		}
	}
	
	/**
	 * Decreases the count for the number of parsing threads running.
	 * 
	 * @param session server session object
	 */
	public static void decrementThreadCount(HttpSession session) {
		synchronized (session) {
			int threadCount = (Integer)session.getAttribute("threadCount");
			threadCount--;
			session.setAttribute("threadCount", threadCount);
		}
	}
	
	/**
	 * Checks if all parsing threads have finished execution.
	 * 
	 * @param session server session object
	 * @return false if any threads are still running, otherwise true
	 */
	public static boolean isThreadsFinished(HttpSession session) {
		synchronized (session) {
			int threadCount = (Integer)session.getAttribute("threadCount");
			if (threadCount > 0)
				return false;
			else
				return true;
		}
	}

}
