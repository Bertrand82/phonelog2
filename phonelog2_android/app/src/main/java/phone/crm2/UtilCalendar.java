package phone.crm2;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import phone.crm2.model.AppAccount;
import phone.crm2.model.Contact;
import phone.crm2.model.Event;
import phone.crm2.model.EventCRM;
import phone.crm2.model.PhoneCall;
import phone.crm2.model.SMS;

public class UtilCalendar {

	private static HashMap<BgCalendar, Long> insertEventInSelectedCalendars(ContentResolver cr, long startMillis, long endMillis, List<BgCalendar> calendars, String title, String description, String sourceBdd) throws Exception {
		HashMap<BgCalendar, Long> h = new HashMap<BgCalendar, Long>();
		for (BgCalendar bgCalendar : calendars) {
			if (bgCalendar.isSelected()) {
				long id = insertEvent(cr, startMillis, endMillis, bgCalendar, title, description);
				h.put(bgCalendar, Long.valueOf(id));
			}
		}
		return h;
	}

	public static long insertEventTransactionnel(ContentResolver cr, long startMillis, long endMillis, BgCalendar calendar, String title_, String description) throws Exception {
		long calendarId = calendar.getCalID();
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		TimeZone timeZone = TimeZone.getDefault();
		int rawContactInsertIndex = ops.size();
		ops.add(ContentProviderOperation.newInsert(Events.CONTENT_URI).withValue(Events.DTSTART, startMillis).withValue(Events.DTEND, endMillis).withValue(Events.TITLE, title_).withValue(Events.DESCRIPTION, description).withValue(Events.CALENDAR_ID, calendarId).withValue(Events.EVENT_TIMEZONE, timeZone.getID()).build());

		ContentProviderResult[] cp = cr.applyBatch(CalendarContract.AUTHORITY, ops);
		Log.w("bg2", "insertEventTransactionnel cp.length :" + cp.length);
		if (cp.length > 0) {
			Log.w("bg2", "insertEventTransactionnel cp.length :" + cp[0].uri);
			Uri myContactUri = cp[0].uri;
			int lastSlash = myContactUri.toString().lastIndexOf("/");
			int length = myContactUri.toString().length();
			int id = Integer.parseInt((String) myContactUri.toString().subSequence(lastSlash + 1, length));
			return id;
		}
		return 0;
	}

	public static long insertEvent(ContentResolver cr, long startMillis, long endMillis, BgCalendar calendar, String title, String description) {
		try {
			return insertEventTransactionnel(cr, startMillis, endMillis, calendar, title, description);
		} catch (Exception e) {
			Log.w("bg2", "insertEvent EXception", e);
			return 0;
		}
	}

	private static long insertEventNoTransactionnel(ContentResolver cr, long startMillis, long endMillis, BgCalendar calendar, String title, String description) {
		long calendarId = calendar.getCalID();
		TimeZone timeZone = TimeZone.getDefault();
		Log.i("bg2", "insertEvent_22 timeZone: " + timeZone.getDisplayName() + " timeZone id:" + timeZone.getID() + "| title " + title + "| description :" + description);
		ContentValues values = new ContentValues();
		values.put(Events.DTSTART, startMillis);
		values.put(Events.DTEND, endMillis);
		values.put(Events.TITLE, title);
		values.put(Events.DESCRIPTION, description);
		values.put(Events.CALENDAR_ID, calendarId);
		values.put(Events.EVENT_TIMEZONE, timeZone.getID());
		Uri uri = cr.insert(Events.CONTENT_URI, values);

		// get the event ID that is the last element in the Uri
		long eventID = Long.parseLong(uri.getLastPathSegment());

		Log.i("bg2", "insertEvent22 eventID " + eventID + "  calendarId :" + calendarId + "  startMillis : " + startMillis);
		calendar.setEventId(startMillis, eventID);
		return eventID;
	}

	public static final String[] EVENT_PROJECTION = new String[] { Events._ID, // 0
			Events.DTSTART, // 1
			Events.DTEND, // 2
			Events.TITLE, // 3
			Events.DESCRIPTION // 4
	};

	private static void listEvent___(ContentResolver cr, long calendarId, String tag) {
		Uri uri = Events.CONTENT_URI;

		String selection = "(" + Events.TITLE + " like  ?)";
		String[] selectionArgs = new String[] { tag };

		Cursor cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
		int i = 0;
		while (cur.moveToNext()) {
			i++;

			// Get the field values
			long eventID = cur.getLong(0);
			long dstart = cur.getLong(1);
			long dEnd = cur.getLong(2);
			String title = cur.getString(3);
			String description = cur.getString(4);

			// Do something with the values...
			// Log.i("bg2", "listEvent_az | i : " + i + " | displayName: " +
			// eventID + " " + dstart + " " + dEnd + " " + title + " " +
			// description);
		}
		Log.i("bg2", "listEvent_b | nb event " + i);
		cur.close();
	}

	public static final String[] EVENT_PROJECTION_LIST_CALENDAR = new String[] { Calendars._ID, // 0
			Calendars.ACCOUNT_NAME, // 1
			Calendars.CALENDAR_DISPLAY_NAME, // 2
			Calendars.OWNER_ACCOUNT, // 3
			Calendars.ACCOUNT_TYPE // 4
	};

	// The indices for the projection array above.
	private static final int PROJECTION_ID_INDEX = 0;
	private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
	private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
	private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
	private static final int PROJECTION_ACCOUNT_TYPE = 4;

	/**
	 * 
	 * @param listCalendars
	 * @param cr
	 */
	public static void runQueryListCalendar(List<BgCalendar> listCalendars, ContentResolver cr) {
		try {
			// Run query
			Uri uri = Calendars.CONTENT_URI;

			String selection = "";
			String[] selectionArgs = new String[] {};

			Cursor cur = cr.query(uri, EVENT_PROJECTION_LIST_CALENDAR, selection, selectionArgs, null);
			int i = 0;
			// Use the cursor to step through the returned records
			listCalendars.clear();
			while (cur.moveToNext()) {

				i++;

				// Get the field values
				long calID = cur.getLong(PROJECTION_ID_INDEX);
				String displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
				String accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
				String ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
				String accountType = cur.getString(PROJECTION_ACCOUNT_TYPE);
				Log.i("bg2","runQueryListCalendar i:"+i+" displayName :"+displayName+"  accountName :"+accountName+" ownerName :"+ownerName+"  accountType: " +accountType);
				// Do something with the values...
				if (CalendarContract.ACCOUNT_TYPE_LOCAL.equals(accountType)) {
					// A priori on elimine ce type de calendar. (Voir javadoc sur ACCOUNT_TYPE_LOCAL)
				}else {
					BgCalendar bgCalendar = new BgCalendar(displayName, accountName, ownerName, calID, accountType);
					listCalendars.add(bgCalendar);
				}
			}
			cur.close();
			Log.i("bg2","runQueryListCalendar nb de result :"+i);
		} catch (Exception e) {
			Log.w("bg2", "runQueryListCalendar Exception", e);
		}
	}

	private static final String TAG_PHONE_CALL = "Phone Call";
	private static final String[] TYPES = { "ALERT", "INCOMING", "OUTGOING", "MISSED", "INCOMING_SMS", "OUTGOING_SMS", " unknown" };

	public static void insertEventInSelectedCalendars(ApplicationBg applicationBg, SMS sms) {
		ContentResolver cr = applicationBg.getContentResolver();
		List<BgCalendar> listCAlendars = applicationBg.getListCalendars();
		long startMillis = sms.getDate();
		long endMillis = startMillis + (1000L);
		String title = toTitleFromPhoneCall(sms);
		String description = sms.getMessage();
		if (description == null) {
			description = "";
		}
		try {
			insertEventInSelectedCalendars(cr, startMillis, endMillis, listCAlendars, title, description, sms.getBddId());
		} catch (Exception e) {
			Log.w("bg2", "insertEventInSelectedCalendars sms ", e);
		}

	}

	public static HashMap<BgCalendar, Long> insertEventInSelectedCalendars(ApplicationBg applicationBg, PhoneCall phoneCall) {
		ContentResolver cr = applicationBg.getContentResolver();
		List<BgCalendar> listCAlendars = applicationBg.getListCalendars();
		long startMillis = phoneCall.getDate();
		long endMillis = startMillis + phoneCall.getDuration_ms();
		String title = toTitleFromPhoneCall(phoneCall);
		String description = phoneCall.getComment();
		if (description == null) {
			description = "";
		}
		try {
			return insertEventInSelectedCalendars(cr, startMillis, endMillis, listCAlendars, title, description, phoneCall.getBddId());
		} catch (Exception e) {
			Log.w("bg2", "insertEventInSelectedCalendars phoneCAll EXCEPTION", e);
			return null;
		}
	}

	public static int limit = 20;

	public static List<Event> getListEvent(Context context, BgCalendar bgCalendar, int page) {
		if (bgCalendar == null) {
			return new ArrayList<Event>();
		}
		ContentResolver cr = context.getContentResolver();
		String tag = TAG_PHONE_CALL;
		List<Event> events = new ArrayList<Event>();
		Uri uri = Events.CONTENT_URI;

		String selection = "( (" + Events.TITLE + " like  ?) AND (" + Events.CALENDAR_ID + " = ?) ) ";
		// String order = ""+Events.DTSTART+"  DESC ";
		int offset = page * limit;
		String order = "" + Events.DTSTART + "  DESC LIMIT " + limit + " OFFSET " + offset;

		String[] selectionArgs = new String[] { "%" + tag + "%", "" + bgCalendar.getCalID() };

		Cursor cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, order);
		int i = 0;
		while (cur.moveToNext()) {
			i++;

			// Get the field values
			long eventID = cur.getLong(0);
			long dstart = cur.getLong(1);
			long dEnd = cur.getLong(2);
			String title = cur.getString(3);
			String description = cur.getString(4);

			Event phoneCall = parsePhoneCall(title, description, dstart, dEnd, eventID, bgCalendar);
			if (phoneCall != null) {
				events.add(phoneCall);
			}
		}
		cur.close();

		return events;
	}

	private static List<Event> getListEvent_WORKING_OLD(ContentResolver cr, BgCalendar bgCalendar) {

		String tag = TAG_PHONE_CALL;
		List<Event> events = new ArrayList<Event>();
		Uri uri = Events.CONTENT_URI;

		String selection = "( (" + Events.TITLE + " like  ?) AND (" + Events.CALENDAR_ID + " = ?))";
		String[] selectionArgs = new String[] { "%" + tag + "%", "" + bgCalendar.getCalID() };

		Cursor cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
		int i = 0;
		while (cur.moveToNext()) {
			i++;

			// Get the field values
			long eventID = cur.getLong(0);
			long dstart = cur.getLong(1);
			long dEnd = cur.getLong(2);
			String title = cur.getString(3);
			String description = cur.getString(4);

			Event phoneCall = parsePhoneCall(title, description, dstart, dEnd, eventID, bgCalendar);
			if (phoneCall != null) {
				events.add(phoneCall);
			}
		}
		Log.i("bg2", "listEvent_b | nb event " + i);

		cur.close();

		return events;
	}

	public static String toTitleFromPhoneCall(Event event) {
		if (event == null) {
			return "";
		}
		String label = event.getContact().getNameRemember() + "|" + TAG_PHONE_CALL + " | " + TYPES[event.getType()] + " | " + event.getContact().getNumber() + "  | " + event.getContact().getName() + "|" + getEmail(event) + " | " + trim(event.getMessageText());
		return label;
	}

	private static String getEmail(Event event) {
		if (event == null) {
			return "";
		}
		if (event.getAccount() == null) {
			return "";
		}
		return event.getAccount().getMail();
	}

	private static String trim(String s) {
		if (s == null) {
			return "";
		}
		s = s.trim();
		s = s.replace('\n', ' ');
		s = s.replace('\t', ' ');
		return s;
	}

	public static Event parsePhoneCall(String title, String description, long dstart, long dEnd, long eventID, BgCalendar bgCalendar) {
		try {
			if (title == null) {
				return null;
			}

			if (title.startsWith("CRM ")) {
				String message = title.substring("CRM ".length());
				EventCRM event = new EventCRM(dstart, message);
				return event;
			}

			if (title.indexOf(TAG_PHONE_CALL) >= 0) {
				int i0 = 1;
				if (title.startsWith(TAG_PHONE_CALL)) {
					i0 = 0;
				}
				String[] s = title.split("\\|");
				if (s.length < i0 + 3) {
					return null;
				} else {
					PhoneCall phoneCall = new PhoneCall();
					phoneCall.setDate(dstart);
					phoneCall.setDuration_ms((int) (dEnd - dstart));
					phoneCall.setId(eventID);
					phoneCall.setComment(description);

					String typeStr = s[i0 + 1];
					String number = s[i0 + 2].trim();
					String contactName = s[i0 + 3].trim();
					AppAccount account = null;
					if (s.length > (i0 + 5)) {
						String email = s[i0 + 4].trim();
						if (email.length() > 0) {
							account = new AppAccount();
							account.setMail(email);
						}
					}
					int type = toInt(typeStr);
					phoneCall.setType(type);
					Contact contact = new Contact();
					contact.setNumber(number);
					contact.setName(contactName);
					phoneCall.setContact(contact);
					phoneCall.setAccount(account);
					phoneCall.setBddId("" + bgCalendar.getCalID());
					return phoneCall;
				}
			} else {
				return null;
			}

		} catch (Exception e) {
			Log.w("bg2", "parsePhoneCall Exception title: " + title, e);
			return null;
		}
	}

	private static int toInt(String typeStr) {
		if (typeStr == null) {
			return 0;
		}
		typeStr = typeStr.trim();
		int i = 0;
		for (String t : TYPES) {
			if (t.equals(typeStr)) {
				return i;
			}
			i++;
		}
		return 0;
	}

	private static Event getEvent(ContentResolver cr, BgCalendar bgCalendar, long startMillis, long endMilli) {
		String tag = TAG_PHONE_CALL;
		tag = "Phone";
		List<Event> events = new ArrayList<Event>();
		Uri uri = Events.CONTENT_URI;

		// String selection= "( (" + Events.TITLE +
		// " like  ?) AND ("+Events.CALENDAR_ID+" = ?) AND ("+Events.DTSTART+"=?) AND ("+Events.DTEND+"=?) )";
		// String[] selectionArgs = new String[] {tag+"%",
		// ""+bgCalendar.getCalID(), "" +startMillis, ""+endMilli};
		String selection_ = "( (" + Events.TITLE + " like  ?) AND (" + Events.CALENDAR_ID + " = ?) AND (" + Events.DTSTART + "=?) )";
		String[] selectionArgs_ = new String[] { "%" + tag + "%", "" + bgCalendar.getCalID(), "" + startMillis };

		Cursor cur = cr.query(uri, EVENT_PROJECTION, selection_, selectionArgs_, null);
		int i = 0;
		while (cur.moveToNext()) {
			i++;

			// Get the field values
			long eventID = cur.getLong(0);
			long dstart = cur.getLong(1);
			long dEnd = cur.getLong(2);
			String title = cur.getString(3);
			String description = cur.getString(4);

			Event phoneCall = parsePhoneCall(title, description, dstart, dEnd, eventID, bgCalendar);
			if (phoneCall != null) {
				events.add(phoneCall);
			}
		}
		Log.i("bg2", "getEvent | nb event " + i);

		cur.close();
		if (events.size() == 0) {
			return null;
		} else {
			return events.get(0);
		}

	}

	public static UpdateResult update(ApplicationBg applicationBg, PhoneCall phoneCall) {
		Log.w("bg2", "update " + phoneCall.gethIds());
		ContentResolver cr = applicationBg.getContentResolver();
		List<BgCalendar> listCAlendars = applicationBg.getListCalendarsSelected();
		long startMillis = phoneCall.getDate();
		long endMillis = startMillis + (phoneCall.getDuration_ms());
		String title = toTitleFromPhoneCall(phoneCall);
		String description = phoneCall.getComment();
		if (description == null) {
			description = "";
		}
		// I need Id
		return updateEventInSelectedCalendars(cr, startMillis, endMillis, listCAlendars, title, description, phoneCall.getBddId(), phoneCall.getId(), phoneCall.gethIds());
	}

	private static UpdateResult updateEventInSelectedCalendars(ContentResolver cr, long startMillis, long endMillis, List<BgCalendar> listCAlendars, String title, String description, String bddSource, long eventIdSource, HashMap<BgCalendar, Long> hIds) {
		for (BgCalendar bgc : hIds.keySet()) {
			//Log.w("bg2", "updateEventInSelectedCalendars bgCalendar hIds " + bgc + " hashCode " + bgc.hashCode() + " id: " + hIds.get(bgc));
		}
		UpdateResult result = new UpdateResult();
		for (BgCalendar bgCalendar : listCAlendars) {
			Long idH = hIds.get(bgCalendar);
			if (idH == null) {
				idH = 0L;
			}
			if (idH != 0L) {
				eventIdSource = idH;
			}
			boolean b = updateEventInSelectedCalendars(cr, startMillis, endMillis, bgCalendar, title, description, eventIdSource);
			result.hResult.put(bgCalendar, b);

		}
		return result;
	}

	private static boolean updateEventInSelectedCalendars(ContentResolver cr, final long startMillis, long endMillis, BgCalendar bgCalendar, final String title, String description, long eventIdSource) {
		try {
			long eventID = 0;
			long eventIdSource2 = eventIdSource;// Debug
			if (eventIdSource2 != 0L) {
				eventID = eventIdSource;
			} else {
				Event event = getEvent(cr, bgCalendar, startMillis, endMillis);
				
				if (event == null) {
					eventID = bgCalendar.getEventId(startMillis);
				} else {
					eventID = event.getId();
				}
			}
			if (eventID != 0) {
				TimeZone timeZone = TimeZone.getDefault();
				Log.w("bg2", "updateEvent_22  timeZone: " + timeZone.getDisplayName() + " timeZone id:" + timeZone.getID() + "| title " + title + "| description :" + description + " event id" + eventID);
				ContentValues values = new ContentValues();
				values.put(Events.DTSTART, startMillis);
				values.put(Events.DTEND, endMillis);
				values.put(Events.TITLE, title);
				values.put(Events.DESCRIPTION, description);
				values.put(Events.CALENDAR_ID, bgCalendar.getCalID());
				values.put(Events.EVENT_TIMEZONE, timeZone.getID());
				
				Uri updateUri = ContentUris.withAppendedId(Events.CONTENT_URI, eventID);
				int rows = cr.update(updateUri, values, null, null);
			} else {
				insertEvent(cr, startMillis, endMillis, bgCalendar, title, description);
			}
			return true;
		} catch (RuntimeException e) {
			Exception ee = e;
			Log.w("bg2", "updateEventInSelectedCalendars  RuntimeException", ee);
			// System.err.println("bg222",e);
			return false;
		}
	}

	public static List<Event> getListEventByContact(Context context, BgCalendar bgCalendar, Contact contact, int page) {
		String param = "%" + TAG_PHONE_CALL+ "%" + contact.getNumber() + "%";
		return getListEventByParam(context, bgCalendar, contact, page, param);
	}
	
	public static List<Event> getListEventByContactAndCommentNotNull(Context context, BgCalendar bgCalendar, Contact contact, int page) {
		String tag = TAG_PHONE_CALL;
		String param = "%" + tag + "%" + contact.getNumber() + "%";
		 List<Event> listEvents = new ArrayList<Event>();
		 List<Event> l = getListEventByParam(context, bgCalendar, contact, page, param);
		while( l.size()>0 && listEvents.size()<20) {
			 for(Event ev : l){
				 if(ev==null){
					 
				 }else if (ev instanceof PhoneCall){
					 PhoneCall pc  =(PhoneCall) ev;
					 if ( pc.getComment() == null) {
						 
					 }else if (pc.getComment().trim().length() ==0){
						 
					 }else {
						 listEvents.add(pc);
					 }
				 }
				 
			 }
			 page++;
			 l = getListEventByParam(context, bgCalendar, contact, page, param);
		}
		return listEvents;
	}

	public static List<Event> getListEventByNumeroClient(Context context, BgCalendar bgCalendar, Contact contact, int page) {
		String param = "%CRM%" + contact.getClientId() + "%";
		return getListEventByParam(context, bgCalendar, contact, page, param);
	}

	public static List<Event> getListEventByParam(Context context, BgCalendar bgCalendar, Contact contact, int page, String param) {

		ContentResolver cr = context.getContentResolver();
		List<Event> events = new ArrayList<Event>();
		Uri uri = Events.CONTENT_URI;
		String selection = "( (" + Events.TITLE + " like  ?) AND (" + Events.CALENDAR_ID + " = ?) ) ";
		// String order = ""+Events.DTSTART+"  DESC ";
		int offset = page * limit;
		String order = "" + Events.DTSTART + "  DESC LIMIT " + limit + " OFFSET " + offset;

		String[] selectionArgs = new String[] { param, "" + bgCalendar.getCalID() };

		Cursor cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, order);
		int i = 0;
		while (cur.moveToNext()) {
			i++;

			// Get the field values
			long eventID = cur.getLong(0);
			long dstart = cur.getLong(1);
			long dEnd = cur.getLong(2);
			String title = cur.getString(3);
			String description = cur.getString(4);

			Event event = parsePhoneCall(title, description, dstart, dEnd, eventID, bgCalendar);
			if (event != null) {
				event.setBddId("" + bgCalendar.getCalID());
			}
			if (event == null) {
				
			} else if (event instanceof EventCRM) {
				events.add(event);
				
			} else {
				event.getContact().getExtra(context);
				events.add(event);
				
			}
		}
		
		cur.close();

		return events;
	}

}
