package com.rajesh.config;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.rajesh.entity.Employee;
import com.rajesh.processor.CsvFileProcessor;
import com.rajesh.reader.CsvFileReader;
import com.rajesh.writer.CsvFileWriter;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private PlatformTransactionManager transactionManager;

//	@Autowired
//	private CsvFileReader csvFileReader;
//	
//	@Autowired
//	private CsvFileWriter csvFileWriter;

	@Autowired
	private CsvFileProcessor csvFileProcessor;

	@Bean
	Job firstJob() {
		return new JobBuilder("firstJob", jobRepository).start(firstChunkStep()).build();
	}

	private Step firstChunkStep() {
		return new StepBuilder("first chunk step", jobRepository).<Employee, Employee>chunk(3, transactionManager)
				.reader(flatFileItemReader())
				.processor(csvFileProcessor)
				.writer(flatFileItemWriter()).build();
	}

	@Bean
	public FlatFileItemReader<Employee> flatFileItemReader() {
		FlatFileItemReader<Employee> reader = new FlatFileItemReader<Employee>();
		reader.setResource(
				new FileSystemResource("I:\\EclipseData\\SpringBatchWithFileReader\\InputFiles\\student.csv"));
		reader.setLineMapper(new DefaultLineMapper<Employee>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames("id", "firstName", "lastName", "email");
						setDelimiter(",");
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {
					{
						setTargetType(Employee.class);
					}
				});
			}
		});
		reader.setLinesToSkip(1);
		return reader;
	}

	public FlatFileItemWriter<Employee> flatFileItemWriter() {
		FlatFileItemWriter<Employee> flatFileItemWriter = new FlatFileItemWriter<Employee>();
		flatFileItemWriter.setResource(
				new FileSystemResource("I:\\EclipseData\\SpringBatchWithFileReader\\InputFiles\\student_out.csv"));
		flatFileItemWriter.setHeaderCallback(new FlatFileHeaderCallback() {

			@Override
			public void writeHeader(Writer writer) throws IOException {
				writer.write("id,firstname,lastname,email");
			}
		});
		flatFileItemWriter.setLineAggregator(new DelimitedLineAggregator<Employee>() {
			{
				setFieldExtractor(new BeanWrapperFieldExtractor<Employee>() {
					{
						setNames(new String[] { "id", "firstName", "lastName", "email" });
					}
				});
			}
		});
		flatFileItemWriter.setFooterCallback(new FlatFileFooterCallback() {

			@Override
			public void writeFooter(Writer writer) throws IOException {
				writer.write("created @ " + new Date());
			}
		});
		return flatFileItemWriter;
	}
}
