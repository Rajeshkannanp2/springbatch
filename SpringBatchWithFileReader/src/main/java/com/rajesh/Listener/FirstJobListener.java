package com.rajesh.Listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class FirstJobListener implements JobExecutionListener{
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		System.out.println("before job");
	}
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("after job");
	}
}
