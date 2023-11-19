package com.orjujeng.timesheet.entity;

import java.util.Date;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimesheetInfoDetailDTO {
	private Integer id;
	private Integer seqId;
	private Date startDate;  
	private Date endDate;
	private Date exceptFinishDate;
	private	String bankHolidayDate;
	private String annualLeavingDate;
	private String sickLeavingDate;
	private String annualLeavingDays;
	private String sickLeavingDays;
	private String workDays;
	private	String actWorkDays;
	private Integer memberId;
	private String memberName;
	private String accountNum;
	private String finished;
}
