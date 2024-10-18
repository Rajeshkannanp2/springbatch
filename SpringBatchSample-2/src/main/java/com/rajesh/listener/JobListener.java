package com.rajesh.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobListener implements JobExecutionListener{
	@Override
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("before execution");
	}
	@Override
	public void afterJob(JobExecution jobExecution) {
		System.out.println("after execution");
	}
}
