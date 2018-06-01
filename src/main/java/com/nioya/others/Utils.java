package com.nioya.others;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

public class Utils {
	private int ZERO = 0;
	private int NULLVAL = -1;
	private int MAXLIMITTIME = 59;
	private int DIGITCONT = 10;
	private int SECONDS = 60;
	private int MILLISEC = 1000;
	
	
	public Utils() {
		super();
	}

	public String[] getHashKeySet() {
		String[] hashKeySet = new String[SECONDS];
		Calendar calendar = Calendar.getInstance();
		calendar.setTime( new Date());
		calendar.add(Calendar.MINUTE, -1); // 1 min back
		
		int min = calendar.get(Calendar.MINUTE);		
		int sec = calendar.get(Calendar.SECOND);	
		for (int i = 0; i <= MAXLIMITTIME; i++) {
			hashKeySet[i] = dateBuilder(calendar,min,sec);
			sec++;
			if(sec > MAXLIMITTIME) {
				sec = ZERO;
				min++;
				if(min > MAXLIMITTIME) {
					min = ZERO;	
				}
			}
		}
		return hashKeySet;
		
	}
	public String getHashKeyFromTime(long timestamp) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime( new Date(timestamp));
		return dateBuilder(calendar);
		
	}
	
	
	private String dateBuilder(Calendar calendar) {
		return dateBuilder(calendar, NULLVAL, NULLVAL);
	}
	
	public boolean timestampChecker(long timestamp) {
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		double current = currentTimestamp.getTime();
    	double differenceInMinutes = (current - timestamp);
    	double targetInterval = SECONDS * MILLISEC;
    	if(differenceInMinutes< targetInterval) {
    		return true;
    	}else {
    		return false;
    	}
		
	}
	
	public String getClientIP(HttpServletRequest request) {
		String requestIP =  request.getHeader("X-FORWARDED-FOR");
		if(requestIP == null) {
			requestIP = request.getRemoteAddr();
		}
		return requestIP;
		
	}

	private String dateBuilder(Calendar calendar, int min, int sec) {
		StringBuilder builder = new StringBuilder();
		builder.append(calendar.get(Calendar.YEAR));
		int month = calendar.get(Calendar.MONTH);
		month++;
		builder = this.zeroAppender(month, builder);
		builder = this.zeroAppender(calendar.get(Calendar.DAY_OF_MONTH), builder);		
		builder = this.zeroAppender(calendar.get(Calendar.HOUR_OF_DAY), builder);
		if(min != NULLVAL) {
			builder = this.zeroAppender(min, builder);
		}else {
			builder = this.zeroAppender(calendar.get(Calendar.MINUTE), builder);	
		}
		if(sec != NULLVAL) {
			builder = this.zeroAppender(sec, builder);	
		}else {
			builder = this.zeroAppender(calendar.get(Calendar.SECOND), builder);	
		}

		return builder.toString();
	}
	private StringBuilder zeroAppender(int value, StringBuilder sb) { // to beautify time format, add 0 if value is one digit.
		if(value < DIGITCONT) {
			sb.append(ZERO);
		}
		sb.append(value);
		return sb;
		
	}


}
