package com.orjujeng.timesheet.entity;

import java.util.Date;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimesheetInfoDTO {
	private Integer selectedSeqId;
	private Date selectedStartDate;
	private Date selectedEndDate;
	private String selectedFinished;
	private String selectedProgress;
	private Map<Integer,String> allOptionList; 
	private Map<Integer,TimesheetInfoDetailDTO> allSeqsDetailInfo;
}
