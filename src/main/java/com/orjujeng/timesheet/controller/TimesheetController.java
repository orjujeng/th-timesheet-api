package com.orjujeng.timesheet.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.orjujeng.timesheet.entity.NewTimesheetDTO;
import com.orjujeng.timesheet.entity.TimesheetInfo;
import com.orjujeng.timesheet.entity.TimesheetInfoDetail;
import com.orjujeng.timesheet.service.TimesheetService;
import com.orjujeng.timesheet.utils.Result;

import lombok.extern.slf4j.Slf4j;
@Controller
@RequestMapping("/timesheet")
@Slf4j
@ResponseBody
public class TimesheetController {
	@Autowired
	TimesheetService timesheetService;
	@PostMapping("/newTimesheet")
	public Result newTimesheet(@RequestBody List<NewTimesheetDTO> newTimesheetDTO) {
		Result result = timesheetService.newTimesheet(newTimesheetDTO);
		return result;
	}
	@GetMapping("/getTimesheetInfo")
	public Result getTimesheetInfo(@RequestParam(required = false,value="seqId") String seqId,@RequestParam(required = true,value="finished") String finished) throws ParseException {
		Result result = timesheetService.getTimesheetInfo(seqId,finished);
		return result;
	}
	
	@PostMapping("/finishTimesheet")
	public Result finishTimesheet(@RequestParam(required = true,value="seqId") String seqId) {
		Result result = timesheetService.finishTimesheet(seqId);
		return result;
	}
	
	@PostMapping("/updateTimesheetById")
	public Result updateTimesheetById(@RequestBody TimesheetInfoDetail timesheetInfoDetail) {
		Result result = timesheetService.updateTimesheetById(timesheetInfoDetail);
		return result;
	}
	
	@PostMapping("/updateTimesheetFinshedStatusById")
	public Result updateTimesheetFinshedStatusById(@RequestBody TimesheetInfoDetail timesheetInfoDetail) {
		Result result = timesheetService.updateTimesheetFinshedStatusById(timesheetInfoDetail);
		return result;
	}
	
	@PostMapping("/openTimesheetByseqId")
	public Result openTimesheet(@RequestParam(required = true,value="seqId") String seqId) {
		Result result = timesheetService.openTimesheet(seqId);
		return result;
	}
}
