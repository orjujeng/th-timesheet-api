package com.orjujeng.timesheet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.orjujeng.timesheet.entity.NewTimesheetDTO;
import com.orjujeng.timesheet.entity.TimesheetInfo;
import com.orjujeng.timesheet.entity.TimesheetInfoDetail;

@Mapper
public interface TimesheetMapper {

	void addNewTimeSheet(TimesheetInfo timesheetInfo);

	Integer addTimeInfoDetail(@Param("seqId") Integer seqId, @Param("list") List<NewTimesheetDTO> newTimesheetDTO);

	List<TimesheetInfo> getTimesheetInfoList(@Param("seqId") String seqId,@Param("inProgress") String inProgress);

	List<TimesheetInfoDetail> getTimesheetInfoDetailList(@Param("seqId") String seqId);

	void updateTimesheetInfoBySeqId(TimesheetInfo timesheetInfo);

	void updateTimesheetInfoDetailBySeqId(TimesheetInfoDetail timesheetInfoDetail);

	void updateTimesheetInfoDetailById(TimesheetInfoDetail timesheetInfoDetail);

	void updateTimesheetFinshedStatusById(TimesheetInfoDetail timesheetInfoDetail);

}
