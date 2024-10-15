package com.rajesh.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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
import com.rajesh.processor.FirstItemProcessor;
import com.rajesh.reader.FirstItemReader;
import com.rajesh.writer.FirstItemWriter;

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
    
    @Autowired
    private FirstItemReader firstItemReader;
    
    @Autowired
    private FirstItemProcessor firItemProcessor;
    
    @Autowired
    private FirstItemWriter firstItemWriter;
    
    @Bean
    public Job firstJOb() {
        return new JobBuilder("firstJob", jobRepository)
        		.incrementer(new RunIdIncrementer())
        		.start(firstChunkStep())
                .build();
    }
    
    private Step firstChunkStep() {
    	return new StepBuilder("first chunk step", jobRepository)
    			.<Integer, Long>chunk(3,transactionManager)
    			.reader(firstItemReader)
    			.processor(firItemProcessor)
    			.writer(firstItemWriter)
    			.build();
    	
    }
}

