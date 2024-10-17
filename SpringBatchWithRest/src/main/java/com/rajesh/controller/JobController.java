package com.rajesh.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.launch.support.JobOperatorFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rajesh.service.JobService;

@RestController
@RequestMapping("/api/job")
public class JobController {
	
	@Autowired
	JobService jobService;
	
	@Autowired
	JobOperator jobOperator;
	
	@GetMapping("/start/{jobName}")
	public String startJob(@PathVariable String jobName) throws Exception { 
		jobService.startJob(jobName);
		return "Job Started...";
	}
	@GetMapping("/stop/{executionId}")
	public String stopJob(@PathVariable int executionId) throws NoSuchJobExecutionException, JobExecutionNotRunningException {
		jobOperator.stop(executionId);
		return "Job stopped....";
	}

}
