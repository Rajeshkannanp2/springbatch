package com.rajesh.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class JobService {
	
	@Autowired
	JobLauncher jobLauncher;
	
	@Qualifier("firstJob")
	@Autowired
	Job firstJob;
	
	@Qualifier("secondJob")
	@Autowired
	Job secondJob;
	
	@Async
	public void startJob(String jobName) {
		 JobParameters jobParameters = new JobParametersBuilder()
	              .addLong("time", System.currentTimeMillis())
	              .toJobParameters();
			try {
				JobExecution jobExecution = null;
				if(jobName.trim().equals("First Job")) {
					jobExecution=jobLauncher.run(firstJob, jobParameters);
				} else if(jobName.trim().equals("Second Job")) {
					jobExecution=jobLauncher.run(secondJob, jobParameters);
				}
				System.out.println("status: "+jobExecution.getStatus());
			}catch(Exception e) {
				System.out.println("exception while starting the job");
			}
			
	}
}
