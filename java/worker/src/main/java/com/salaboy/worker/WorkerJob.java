package com.salaboy.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.diagrid.springboot.dapr.core.DaprKeyValueTemplate;

@Component
public class WorkerJob {

	@Autowired
	private DaprKeyValueTemplate keyValueTemplate;

    @Scheduled(fixedDelay = 1000)
	public void work() {
		System.out.println("Fetching votes..");

		KeyValueQuery<String> keyValueQuery = new KeyValueQuery<String>("'type' == 'vote'");

		Iterable<Vote> votes = keyValueTemplate.find(keyValueQuery, Vote.class);
		

		int optionA = 0;
		int optionB = 0;
		for(Vote vote : votes){
			if(vote.getOption().equals("a")){
				optionA++;
			}
			if(vote.getOption().equals("b")){
				optionB++;
			}
		}
		
		System.out.println("Storing results: a: "+ optionA + " - b: "+ optionB);
		// Count results and update using KeyValueTemplate
		Results results = new Results("results",optionA, optionB);
		keyValueTemplate.update(results);

	}


}