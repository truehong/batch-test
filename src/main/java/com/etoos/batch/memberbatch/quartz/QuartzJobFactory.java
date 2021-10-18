package com.etoos.batch.memberbatch.quartz;

import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.etoos.batch.memberbatch.dto.ScheduleJob;

public class QuartzJobFactory implements Job {

 public static List<ScheduleJob> jobList = new ArrayList<>();

	static {

		for (int i = 0; i < 3; i++) {
			ScheduleJob job = new ScheduleJob();
			job.setJobId(String.valueOf(i));
			job.setJobName("job_name_" + i);
			job.setJobGroup("member_batch");
			// if (i%2==0) {
			// 	job.setJobGroup("job_group_even");
			// }else {
			// 	job.setJobGroup("job_group_odd");
			// }
			job.setJobStatus("1");
			job.setCronExpression(String.format("0 30 10-13 ? * WED,FRI"));
			job.setDesc("회원 분리보관 테이블 삭제");
			job.setInterfaceName("interface"+i);
			jobList.add(job);
		}

	}

	// simulate data from db
	public static List<ScheduleJob> getInitAllJobs() {
		return jobList;
	}
	
	
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    	ScheduleJob scheduleJob = (ScheduleJob)jobExecutionContext.getMergedJobDataMap().get("scheduleJob");
    	String jobName = scheduleJob.getJobName();

    	// execute task inner quartz system
    	// spring bean can be @Autowired


	}

}
