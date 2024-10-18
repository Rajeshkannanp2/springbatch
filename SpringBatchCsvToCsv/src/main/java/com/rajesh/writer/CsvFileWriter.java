package com.rajesh.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import com.rajesh.entity.Employee;

//@Component
public class CsvFileWriter {
	public FlatFileItemWriter<Employee> flatFileItemWriter() {
		FlatFileItemWriter<Employee> flatFileItemWriter = new FlatFileItemWriter<Employee>();
		flatFileItemWriter.setResource(new FileSystemResource("I:\\EclipseData\\SpringBatchWithFileReader\\InputFiles\\student_out.csv"));
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
}
