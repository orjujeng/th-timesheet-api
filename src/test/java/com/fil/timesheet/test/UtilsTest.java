package com.fil.timesheet.test;

import java.util.Calendar;
import java.util.Date;

import com.orjujeng.timesheet.utils.DateUtils;

public class UtilsTest {
	public static void main(String[] args) {
		Calendar scalendar=Calendar.getInstance();
	    scalendar.set(2023,11,15);  //年月日  也可以具体到时分秒如calendar.set(2015, 10, 12,11,32,52); 
	    Date startDate=scalendar.getTime();//date就是你需要的时间
	    Calendar ecalendar=Calendar.getInstance();
	    ecalendar.set(2023,11,25);  //年月日  也可以具体到时分秒如calendar.set(2015, 10, 12,11,32,52); 
	    Date endDate=ecalendar.getTime();//date就是你需要的时间
		Integer workDays = DateUtils.getWorkDays(startDate,endDate);
		System.out.println(workDays);
	}
}
