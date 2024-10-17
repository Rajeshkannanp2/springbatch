package com.rajesh.config;

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
    Job firstJob() {
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

    @Bean
    Job secondJob() {
        return new JobBuilder("secondJob", jobRepository)
        		.incrementer(new RunIdIncrementer())
                .start(step1())
                .listener(firstJobListener)
                .build();
    }

    @Bean
    Step step1() {
        return new StepBuilder("step1", jobRepository)
                .tasklet(helloTasklet1(), transactionManager)
                .listener(firstStepListener)
                .build();
    }

    @Bean
    Tasklet helloTasklet1() {
        return (contribution, chunkContext) -> {
            System.out.println("check1");
            return RepeatStatus.FINISHED;
        };
    }
}

