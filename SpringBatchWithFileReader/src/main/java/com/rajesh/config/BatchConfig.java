package com.rajesh.config;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.rajesh.Listener.FirstStepListener;
import com.rajesh.entity.Student;
import com.rajesh.processor.FirstItemProcessor;
import com.rajesh.reader.FirstItemReader;
import com.rajesh.writer.FirstItemWriter;
import java.util.Date;


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
    			.<Student, Student>chunk(3,transactionManager)
    			.reader(flatFileItemReader())
    			//.processor(firItemProcessor)
    			.writer(flatFileItemWriter())
    			.build();
    	
    }
    
    public FlatFileItemReader<Student> flatFileItemReader() {
    	FlatFileItemReader<Student> flatFileItemReader = new FlatFileItemReader<Student>();
    	flatFileItemReader.setResource(new FileSystemResource("I:\\EclipseData\\SpringBatchWithFileReader\\InputFiles\\student.csv"));
    	
    	flatFileItemReader.setLineMapper(new DefaultLineMapper<Student>() {
    		{
    			setLineTokenizer(new DelimitedLineTokenizer() {
    				{
    					setNames("id","firstName","lastName","email");
    					setDelimiter(",");
    				}
    			
    			});
    			
    			setFieldSetMapper(new BeanWrapperFieldSetMapper<Student>() {
    				{
    					setTargetType(Student.class);
    				}
    			});
    		}
    	});
    	flatFileItemReader.setLinesToSkip(1);
    	return flatFileItemReader;
    }
    
    public FlatFileItemWriter<Student> flatFileItemWriter() {
    	FlatFileItemWriter<Student> flatFileItemWriter = new FlatFileItemWriter<Student>();
    	flatFileItemWriter.setResource(new FileSystemResource("I:\\EclipseData\\SpringBatchWithFileReader\\InputFiles\\student_out.csv"));
    	flatFileItemWriter.setHeaderCallback(new FlatFileHeaderCallback() {
			
			@Override
			public void writeHeader(Writer writer) throws IOException {
				writer.write("id,firstname,lastname,email");
			}
		});
    	flatFileItemWriter.setLineAggregator(new DelimitedLineAggregator<>() {
    		{
    			setFieldExtractor(new BeanWrapperFieldExtractor<Student>() {
    				{
    					setNames(new String[] {"id", "firstName", "lastName", "email"});
    				}
    			});
    		}
    	});
    	flatFileItemWriter.setFooterCallback(new FlatFileFooterCallback() {
			
			@Override
			public void writeFooter(Writer writer) throws IOException {
				writer.write("created @ "+ new Date());
				
			}
		});
    	return flatFileItemWriter;
    }

//    @Bean
//    Job secondJob() {
//        return new JobBuilder("secondJob", jobRepository)
//        		.incrementer(new RunIdIncrementer())
//                .start(step1())
//                .listener(firstJobListener)
//                .build();
//    }
//
//    @Bean
//    Step step1() {
//        return new StepBuilder("step1", jobRepository)
//                .tasklet(helloTasklet1(), transactionManager)
//                .listener(firstStepListener)
//                .build();
//    }
//
//    @Bean
//    Tasklet helloTasklet1() {
//        return (contribution, chunkContext) -> {
//            System.out.println("check1");
//            return RepeatStatus.FINISHED;
//        };
//    }
    
    
}

