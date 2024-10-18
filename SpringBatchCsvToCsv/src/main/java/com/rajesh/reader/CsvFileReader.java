package com.rajesh.reader;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import com.rajesh.entity.Employee;

//@Component
public class CsvFileReader {
	public FlatFileItemReader<Employee> flatFileItemReader() {
		FlatFileItemReader<Employee> reader = new FlatFileItemReader<Employee>();
		reader.setResource(new FileSystemResource("I:\\EclipseData\\SpringBatchWithFileReader\\InputFiles\\student.csv"));
		reader.setLineMapper(new DefaultLineMapper<Employee>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames("id","firstName","lastName","email");
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
}
