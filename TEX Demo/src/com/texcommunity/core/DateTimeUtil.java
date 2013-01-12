package com.texcommunity.core;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
	private static final DateFormat dateFormatIn = new SimpleDateFormat("H:m");
	private static final DateFormat dateFormatOut = new SimpleDateFormat("HH:mm");

	public static String printTime(Date date) {
		return dateFormatOut.format(date);
	}
	
	public static Date createDateFromTime(int hour, int minute) {
		try {
			return dateFormatIn.parse(hour+":"+minute);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
