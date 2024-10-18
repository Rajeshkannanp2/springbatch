package com.rajesh.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.rajesh.entity.Employee;

@Component
public class CsvFileProcessor implements ItemProcessor<Employee, Employee>{

	@Override
	public Employee process(Employee item) throws Exception {
		item.setFirstName(item.getFirstName().toUpperCase());
		return item;
	}
	
}
