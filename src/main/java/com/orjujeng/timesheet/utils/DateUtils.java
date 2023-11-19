package com.orjujeng.timesheet.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {
	 public static Integer getWorkDays(Date startDate, Date endDate) {
	        List<Date> list = new ArrayList<>();
	        Calendar tempStart = Calendar.getInstance();
	        //startDate.setMonth(startDate.getMonth());
	        tempStart.setTime(startDate);
	        
	        Calendar tempEnd = Calendar.getInstance();
	        //endDate.setMonth(endDate.getMonth());
	        tempEnd.setTime(endDate);
	        tempEnd.add(Calendar.DATE, +1);// 包含结束日期+1,将结束日期放入结果集
	        while (tempStart.before(tempEnd)||tempStart.equals(tempEnd)) {
	            //int day = tempStart.getTime().getDay();
	            int day = tempStart.get(Calendar.DAY_OF_WEEK) - 1;
	            if(day != 6 && day != 0){
	                list.add(tempStart.getTime());
	                //System.out.println(tempStart.getTime());
	            }
	            tempStart.add(Calendar.DAY_OF_YEAR, 1);
	        }
	        return list.size();
	    }

}
