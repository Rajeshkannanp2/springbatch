package com.rajesh;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.rajesh.Listener.FirstJobListener;
import com.rajesh.Listener.FirstStepListener;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
    private JobRepository jobRepository;
	@Autowired
    private PlatformTransactionManager transactionManager;
    
    @Autowired
    private JobExecutionListener firstJobListener;
    
    @Autowired
    private FirstStepListener firstStepListener;
    
    //@Bean
    public Job helloJob() {
        return new JobBuilder("helloJob", jobRepository)
        		.incrementer(new RunIdIncrementer())
                .start(step1())
                .next(step2())
                .listener(firstJobListener)
                .build();
    }
    
    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .tasklet(helloTasklet1(), transactionManager)
                .listener(firstStepListener)
                .build();
    }
    
    @Bean
    public Step step2() {
        return new StepBuilder("step1", jobRepository)
                .tasklet(helloTasklet2(), transactionManager)
                .build();
    }
    
    @Bean
    public Tasklet helloTasklet1() {
        return (contribution, chunkContext) -> {
            System.out.println("check1");
            return RepeatStatus.FINISHED;
        };
    }
    
    @Bean
    public Tasklet helloTasklet2() {
        return (contribution, chunkContext) -> {
            System.out.println("check2");
            return RepeatStatus.FINISHED;
        };
    }
}
