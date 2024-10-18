package com.rajesh;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestCronJob {
	@Scheduled(fixedRate = 60000)
	private void printSomeValue() {
		System.out.println("method start....");
		
		for(int i=0;i<10;i++) {
			System.out.println(i);
		}
		
		System.out.println("method end....");
	}
}
