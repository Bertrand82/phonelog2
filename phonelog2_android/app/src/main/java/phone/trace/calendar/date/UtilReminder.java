package phone.trace.calendar.date;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.util.Log;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import phone.trace.BgCalendar;
import phone.trace.UtilCalendar;

public class UtilReminder {

	public static void testReminder(Activity activity, List<BgCalendar> listCalendars) {
		ContentResolver cr = activity.getContentResolver();
		UtilCalendar.runQueryListCalendar(listCalendars, cr);
		long startMillis = System.currentTimeMillis()+5l* 60l*1000l;
		long endMillis = startMillis + 1000;
		String title = "Cela est un test!!";
		String description = "Cela est un test description!!";
		long calendarId = listCalendars.get(0).getCalID();
		Long eventId = insertEvent( cr, startMillis,endMillis, calendarId, title, description) ;
		Log.i("bg2"," insert event "+eventId);
	}
	
	
	
	public static long insertEvent(ContentResolver cr,  long startMillis,  long endMillis, long calendarId,String title, String description) {
		TimeZone timeZone = TimeZone.getDefault();
		Log.i("bg2","insertEvent displayName : "+timeZone.getDisplayName()+"  id:" +timeZone.getID());
		ContentValues values = new ContentValues();
		values.put(Events.DTSTART, startMillis);
		values.put(Events.DTEND, endMillis);
		values.put(Events.TITLE, title);
		values.put(Events.DESCRIPTION, description);
		values.put(Events.CALENDAR_ID, calendarId);
		//values.put(Events.CALENDAR_COLOR,0xff0000 ) java.lang.IllegalArgumentException: Only the provider may write to calendar_color;
		// values.put(Events.CALENDAR_DISPLAY_NAME,"zorg");IllegalArgumentException: Only the provider may write to calendar_displayName
		values.put(Events.EVENT_TIMEZONE, timeZone.getID());
		Uri uri = cr.insert(Events.CONTENT_URI, values);

		// get the event ID that is the last element in the Uri
		long eventID = Long.parseLong(uri.getLastPathSegment());
		addReminder(eventID, cr);
		return eventID;
	}


	private static void addReminder(long eventID, ContentResolver cr){
		
		ContentValues values = new ContentValues();
		values.put(Reminders.MINUTES, 0);
		values.put(Reminders.EVENT_ID, eventID);
		//values.put(Reminders.METHOD, Reminders.METHOD_ALERT);
		values.put(Reminders.METHOD, Reminders.METHOD_DEFAULT);// Default depend de la configuration du Calendar
		Uri uri = cr.insert(Reminders.CONTENT_URI, values);
	}



	public static void setReminder(ContentResolver cr, BgCalendar bgCalendar, Date date, String eventStr) {
		Log.i("bg2","set Reminder : "+bgCalendar+" eventStr:" +eventStr);
		long startMillis = date.getTime();
		long endMillis = startMillis+1000;
		long idEvent = insertEvent(cr, startMillis, endMillis, bgCalendar.getCalID(), eventStr,eventStr);
		addReminder(idEvent, cr);
	}

	
	

}
