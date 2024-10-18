package com.rajesh.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.rajesh.entity.Student;

@Component
public class FirstItemWriter implements ItemWriter<Student> {

	@Override
	public void write(Chunk<? extends Student> chunk) throws Exception {
		// TODO Auto-generated method stub
		chunk.forEach(System.out::println);
	}
}
