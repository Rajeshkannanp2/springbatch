package com.rajesh.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import com.rajesh.entity.Customer;

public class CustomerItemProcessor implements ItemProcessor<Customer, Customer> {

  private static final Logger log = LoggerFactory.getLogger(CustomerItemProcessor.class);

  @Override
  public Customer process(final Customer customer) {
    final String firstName = customer.getFirstName().toUpperCase();
    final String lastName = customer.getLastName().toUpperCase();

    final Customer transformedPerson = new Customer(0, firstName, lastName, lastName);
    System.out.println(firstName+"@@@@@@@@");                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            

    log.info("Converting (" + customer + ") into (" + transformedPerson + ")");

    return transformedPerson;
  }

}