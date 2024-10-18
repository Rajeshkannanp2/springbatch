package com.rajesh.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import com.rajesh.config.BatchConfig.CustomFieldSetMapper;
import com.rajesh.entity.Customer;
import com.rajesh.listener.JobListener;
import com.rajesh.processor.CustomerItemProcessor;


public class BatchConfig {
	
	@Bean
    public JobExecutionListener createListener() {
        return new JobListener();
    }
	
//	@Bean
//	public FlatFileItemReader<Customer> createReader() throws Exception {
//	    FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();
//	    System.out.println("check");
//	    reader.setResource(new ClassPathResource("input.csv"));
//
//	    DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
//	    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
//	    tokenizer.setDelimiter(",");
//	    tokenizer.setNames("id", "firstName", "lastName", "email");
//
//	    lineMapper.setLineTokenizer(tokenizer);
//	    
//	    lineMapper.setFieldSetMapper(new CustomFieldSetMapper());
//
//	    reader.setLineMapper(lineMapper);
//	    System.out.println("check: "+reader.getCurrentItemCount());
//	    return reader;
//	}
	
	@Bean
	public FlatFileItemReader<Customer> reader() throws Exception {
		FlatFileItemReader<Customer> check = new FlatFileItemReaderBuilder<Customer>()
				.name("customerItemReader")
				.resource(new ClassPathResource("input.csv"))
				.delimited()
				.names("id", "firstName", "lastName", "email")
				.targetType(Customer.class)
				.build();
		return check;
	}
	
	@Bean
	public JdbcBatchItemWriter<Customer> writer(DataSource dataSource) {
	    JdbcBatchItemWriter<Customer> writer = new JdbcBatchItemWriter<>();
	    writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
	    writer.setSql("INSERT INTO customer (id, first_name, last_name, email) VALUES (:id, :firstName, :lastName, :email)");
	    writer.setDataSource(dataSource);
	    return writer;
	}
	
	@Bean
    public Job job(JobRepository jobRepository, Step step1, JobExecutionListener createListener) {
        return new JobBuilder("job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(createListener)
                .start(step1)
                .build();
    }
	@Bean
	public CustomerItemProcessor processor() {
		return new CustomerItemProcessor();
	}

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                      FlatFileItemReader<Customer> reader, ItemProcessor<Customer, Customer> processor, 
                      JdbcBatchItemWriter<Customer> writer) {
        return new StepBuilder("step1", jobRepository)
                .<Customer, Customer>chunk(1, transactionManager)	
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
	
	public class CustomFieldSetMapper implements FieldSetMapper<Customer> {
	    @Override
	    public Customer mapFieldSet(FieldSet fieldSet) {
	        Customer customer = new Customer();
	        System.out.println("check");
	        customer.setId(fieldSet.readInt("id"));
	        customer.setFirstName(fieldSet.readString("firstName"));
	        customer.setLastName(fieldSet.readString("lastName"));
	        customer.setEmail(fieldSet.readString("email"));
	        return customer;
	    }
	}

}
