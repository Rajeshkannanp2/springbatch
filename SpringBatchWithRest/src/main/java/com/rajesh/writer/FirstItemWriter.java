package com.rajesh.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class FirstItemWriter implements ItemWriter<Long> {

	@Override
	public void write(Chunk<? extends Long> chunk) throws Exception {
		// TODO Auto-generated method stub
		chunk.forEach(System.out::println);
	}
}
