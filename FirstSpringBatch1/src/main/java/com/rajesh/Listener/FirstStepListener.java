package com.rajesh.Listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class FirstStepListener implements StepExecutionListener{
	@Override
	public void beforeStep(StepExecution stepExecution) {
		System.out.println("before step1 execution");
	}
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		System.out.println("after step1 execution");
		return null;
	}
}
