package com.orjujeng.timesheet.service.impl;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orjujeng.timesheet.entity.NewTimesheetDTO;
import com.orjujeng.timesheet.entity.TimesheetInfo;
import com.orjujeng.timesheet.entity.TimesheetInfoDTO;
import com.orjujeng.timesheet.entity.TimesheetInfoDetail;
import com.orjujeng.timesheet.entity.TimesheetInfoDetailDTO;
import com.orjujeng.timesheet.mapper.TimesheetMapper;
import com.orjujeng.timesheet.service.TimesheetService;
import com.orjujeng.timesheet.utils.DateUtils;
import com.orjujeng.timesheet.utils.Result;
import com.orjujeng.timesheet.utils.ResultCode;

@Service
public class TimesheetServiceImpl implements TimesheetService{
	@Autowired
	TimesheetMapper timesheetMapper;
	@Autowired
	RabbitTemplate rabbitTemplate;
	@Override
	@Transactional
	public Result newTimesheet(List<NewTimesheetDTO> newTimesheetDTO) {
		Integer workDay = DateUtils.getWorkDays(newTimesheetDTO.get(0).getStartDate(), newTimesheetDTO.get(0).getEndDate());
		TimesheetInfo timesheetInfo = new TimesheetInfo();
		timesheetInfo.setStartDate(newTimesheetDTO.get(0).getStartDate());
		timesheetInfo.setEndDate(newTimesheetDTO.get(0).getEndDate());
		timesheetInfo.setExceptFinishDate(newTimesheetDTO.get(0).getExceptFinishDate());
		timesheetInfo.setBankHolidayDate(newTimesheetDTO.get(0).getBankHolidayDate());
		timesheetInfo.setBankHolidayDays(newTimesheetDTO.get(0).getBankHolidayDays());
		Integer bankHolidayDays = Integer.parseInt(newTimesheetDTO.get(0).getBankHolidayDays());
		timesheetInfo.setWorkDays((workDay-bankHolidayDays)+"");
		timesheetInfo.setFinished(newTimesheetDTO.get(0).getFinished());
		for (NewTimesheetDTO newTimesheetDTOSub : newTimesheetDTO) {
			newTimesheetDTOSub.setWorkDays(workDay.toString());
		}
		timesheetMapper.addNewTimeSheet(timesheetInfo);
		Integer seqId = timesheetInfo.getSeqId();
		Integer num = timesheetMapper.addTimeInfoDetail(seqId,newTimesheetDTO);
		rabbitTemplate.convertAndSend("newTimesheetExchange","newTimesheet",seqId);
		return Result.successWithMsg(num + " row has been added", null);
	}
	
	
	@Override
	public Result getTimesheetInfo(String seqId, String finished) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<TimesheetInfo> timesheetInfoList = null;
		List<TimesheetInfoDetail> timesheetInfoDetailList =null;
		Integer maxSeqId=null;
		String selectedFinished = null;
		Boolean seqIdMismatch = true;
		int finishedNum = 0;
		if(seqId==null||seqId.equals("")) {
			if(finished!=null&&finished.equals("Y")) {
				timesheetInfoList = timesheetMapper.getTimesheetInfoList(null,"Y");
				//max
				if(timesheetInfoList.size()==0) {
					return Result.error(ResultCode.NO_DATA_SHOWN.code, "No Data Shown", null);
				}
				maxSeqId = timesheetInfoList.get(0).getSeqId();
				timesheetInfoDetailList =  timesheetMapper.getTimesheetInfoDetailList(maxSeqId.toString());
			}else {
				timesheetInfoList = timesheetMapper.getTimesheetInfoList(null,"N");
				//MAX
				if(timesheetInfoList.size()==0) {
					return Result.error(ResultCode.NO_DATA_SHOWN.code, "No Data Shown", null);
				}
				maxSeqId = timesheetInfoList.get(0).getSeqId();
				timesheetInfoDetailList = timesheetMapper.getTimesheetInfoDetailList(maxSeqId.toString());
			}
		}else {
			if(finished!=null&&finished.equals("Y")) {
				timesheetInfoList = timesheetMapper.getTimesheetInfoList(null,"Y");
				for (TimesheetInfo timesheetInfo : timesheetInfoList) {
					if(timesheetInfo.getSeqId()==Integer.parseInt(seqId)) {
						seqIdMismatch=false;
						break;
					}
				}
				if(seqIdMismatch == true) {
					return Result.error(ResultCode.FAIL.code, "SeqId MisMatched", null);
				}
				timesheetInfoDetailList = timesheetMapper.getTimesheetInfoDetailList(seqId);
			}else {
				timesheetInfoList = timesheetMapper.getTimesheetInfoList(null,"N");
				for (TimesheetInfo timesheetInfo : timesheetInfoList) {
					if(timesheetInfo.getSeqId()==Integer.parseInt(seqId)) {
						seqIdMismatch=false;
						break;
					}
				}
				if(seqIdMismatch == true) {
					return Result.error(ResultCode.FAIL.code, "SeqId MisMatched", null);
				}
				timesheetInfoDetailList = timesheetMapper.getTimesheetInfoDetailList(seqId);
			}
		}
		TimesheetInfoDTO timesheetInfoDTO = new TimesheetInfoDTO();
		timesheetInfoDTO.setSelectedSeqId(seqId==null?maxSeqId:Integer.parseInt(seqId));
		timesheetInfoDTO.setSelectedStartDate(timesheetInfoDetailList.get(0).getStartDate());
		timesheetInfoDTO.setSelectedEndDate(timesheetInfoDetailList.get(0).getEndDate());
		timesheetInfoDTO.setSelectedFinished(finished);
		for (TimesheetInfoDetail timesheetInfoDetail : timesheetInfoDetailList) {
			if(timesheetInfoDetail.getFinished().equals("Y")) {
				finishedNum++;
			}
		} 
		Integer progressInfo = 100*finishedNum/timesheetInfoDetailList.size();
		timesheetInfoDTO.setSelectedProgress(progressInfo.toString());
		
		Map allOptionList = new HashMap<Integer,String>();
		for (TimesheetInfo tl : timesheetInfoList) {
			if(seqId!=null&&tl.getSeqId()==Integer.parseInt(seqId)) {
				continue;
			}
			if(maxSeqId!=null&&tl.getSeqId()==maxSeqId) {
				continue;
			}
			allOptionList.put(tl.getSeqId(),formatter.format(tl.getStartDate())+"~"+formatter.format(tl.getEndDate()));
		}
		timesheetInfoDTO.setAllOptionList(allOptionList);
		
		Map allSeqsDetailInfo = new HashMap<Integer,TimesheetInfoDetailDTO>();
		for (TimesheetInfoDetail timesheetInfoDetail : timesheetInfoDetailList) {
			TimesheetInfoDetailDTO timesheetInfoDetailDTO = new TimesheetInfoDetailDTO();
			timesheetInfoDetailDTO.setId(timesheetInfoDetail.getId());
			timesheetInfoDetailDTO.setStartDate(timesheetInfoDetail.getStartDate());
			timesheetInfoDetailDTO.setEndDate(timesheetInfoDetail.getEndDate());
			timesheetInfoDetailDTO.setExceptFinishDate(timesheetInfoDetail.getExceptFinishDate());
			timesheetInfoDetailDTO.setBankHolidayDate(timesheetInfoDetail.getBankHolidayDate());
			timesheetInfoDetailDTO.setMemberId(timesheetInfoDetail.getMemberId());
			timesheetInfoDetailDTO.setMemberName(timesheetInfoDetail.getMemberName());
			timesheetInfoDetailDTO.setAccountNum(timesheetInfoDetail.getAccountNum());
			timesheetInfoDetailDTO.setFinished(timesheetInfoDetail.getFinished());
			timesheetInfoDetailDTO.setSeqId(timesheetInfoDetail.getSeqId());
			timesheetInfoDetailDTO.setAnnualLeavingDate(timesheetInfoDetail.getAnnualLeavingDate());
			timesheetInfoDetailDTO.setSickLeavingDate(timesheetInfoDetail.getSickLeavingDate());
			timesheetInfoDetailDTO.setAnnualLeavingDays(timesheetInfoDetail.getAnnualLeavingDays());
			timesheetInfoDetailDTO.setSickLeavingDays(timesheetInfoDetail.getSickLeavingDays());
			timesheetInfoDetailDTO.setWorkDays(timesheetInfoDetail.getWorkDays());
			timesheetInfoDetailDTO.setActWorkDays(timesheetInfoDetail.getActWorkDays());
			
			allSeqsDetailInfo.put(timesheetInfoDetail.getId(), timesheetInfoDetailDTO);
		}
		timesheetInfoDTO.setAllSeqsDetailInfo(allSeqsDetailInfo);
		return Result.success(timesheetInfoDTO);
	}
	@Override
	@Transactional
	public Result finishTimesheet(String seqId) {
		TimesheetInfo timesheetInfo = new TimesheetInfo();
		timesheetInfo.setSeqId(Integer.parseInt(seqId));
		timesheetInfo.setFinished("Y");
		timesheetMapper.updateTimesheetInfoBySeqId(timesheetInfo);
		List<TimesheetInfoDetail> timesheetInfoDetailList = timesheetMapper.getTimesheetInfoDetailList(seqId);
		for (TimesheetInfoDetail timesheetInfoDetail : timesheetInfoDetailList) {
			
			Integer annualLeavingDays = timesheetInfoDetail.getAnnualLeavingDays()==null||timesheetInfoDetail.getAnnualLeavingDays().equals("null")||timesheetInfoDetail.getAnnualLeavingDays().equals("")?0:Integer.parseInt(timesheetInfoDetail.getAnnualLeavingDays());
			Integer sickLeavingDays = timesheetInfoDetail.getSickLeavingDays()==null||timesheetInfoDetail.getSickLeavingDays().equals("null")||timesheetInfoDetail.getSickLeavingDays().equals("")?0:Integer.parseInt(timesheetInfoDetail.getSickLeavingDays());
			Integer actWorkDays = Integer.parseInt(timesheetInfoDetail.getWorkDays()) - annualLeavingDays - sickLeavingDays- Integer.parseInt(timesheetInfoDetail.getBankHolidayDays());
			timesheetInfoDetail.setActWorkDays(actWorkDays.toString()); 
			timesheetInfoDetail.setFinished("Y");
			timesheetMapper.updateTimesheetInfoDetailBySeqId(timesheetInfoDetail);
		}
		rabbitTemplate.convertAndSend("finishTimesheetExchange", "finishTimesheet",seqId);
		return Result.success(null);
	}
	
	@Override
	@Transactional
	public Result updateTimesheetById(TimesheetInfoDetail timesheetInfoDetail) {
		timesheetMapper.updateTimesheetInfoDetailById(timesheetInfoDetail);
		return Result.success(null);
	}
	@Override
	@Transactional
	public Result updateTimesheetFinshedStatusById(TimesheetInfoDetail timesheetInfoDetail) {
		timesheetMapper.updateTimesheetFinshedStatusById(timesheetInfoDetail);
		return Result.success(null);
	}
	
	
	@Override
	@Transactional
	public Result openTimesheet(String seqId) {
		TimesheetInfo timesheetInfo = new TimesheetInfo();
		timesheetInfo.setSeqId(Integer.parseInt(seqId));
		timesheetInfo.setFinished("N");
		timesheetMapper.updateTimesheetInfoBySeqId(timesheetInfo);
		List<TimesheetInfoDetail> timesheetInfoDetailList = timesheetMapper.getTimesheetInfoDetailList(seqId);
		for (TimesheetInfoDetail timesheetInfoDetail : timesheetInfoDetailList) {
			timesheetInfoDetail.setActWorkDays("0");
			timesheetInfoDetail.setFinished("N");
			timesheetMapper.updateTimesheetInfoDetailBySeqId(timesheetInfoDetail);
		}
		return Result.success(null);
	}
	
}
