package com.orjujeng.timesheet.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimesheetInfo {
	private Integer seqId;
	private Date startDate;  
	private Date endDate;
	private Date exceptFinishDate;
	private	String bankHolidayDate;
	private String bankHolidayDays;
	private String workDays;
	private String finished;
}
