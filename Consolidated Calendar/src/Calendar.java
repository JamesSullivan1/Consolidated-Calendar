import java.util.ArrayList;

public interface Calendar {
	public void Calendar(ArrayList data);
	public String getService();
	public String getName();
	public Event[] getEvents();
	public void addEvent(Event event);
	public void removeEvent(Event event);
}
