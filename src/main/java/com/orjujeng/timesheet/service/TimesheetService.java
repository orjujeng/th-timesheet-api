package com.orjujeng.timesheet.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.orjujeng.timesheet.entity.NewTimesheetDTO;
import com.orjujeng.timesheet.entity.TimesheetInfo;
import com.orjujeng.timesheet.entity.TimesheetInfoDetail;
import com.orjujeng.timesheet.utils.Result;

@Service
public interface TimesheetService {

	Result newTimesheet(List<NewTimesheetDTO> newTimesheetDTO);

	Result getTimesheetInfo(String seqId, String inProgress) throws ParseException;

	Result finishTimesheet(String seqId);

	Result updateTimesheetById(TimesheetInfoDetail timesheetInfoDetail);

	Result updateTimesheetFinshedStatusById(TimesheetInfoDetail timesheetInfoDetail);

	Result openTimesheet(String seqId);

}
