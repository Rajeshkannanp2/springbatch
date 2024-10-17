package com.rajesh.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SecondJobScheduler {
	
	@Autowired
	JobLauncher jobLauncher;
	
	@Qualifier("secondJob")
	@Autowired
	Job secondJob;
	
	@Scheduled(cron = "0 0/1 * 1/1 * ?")
	public void secondJobStarter() {
		JobParameters jobParameters = new JobParametersBuilder()
	              .addLong("time", System.currentTimeMillis())
	              .toJobParameters();
			try {
				JobExecution jobExecution = jobLauncher.run(secondJob, jobParameters);
				System.out.println("status: "+jobExecution.getStatus());
			}catch(Exception e) {
				System.out.println("exception while starting the job");
			}
	}
}
