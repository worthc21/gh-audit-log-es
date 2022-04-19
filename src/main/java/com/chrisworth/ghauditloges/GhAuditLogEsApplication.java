package com.chrisworth.ghauditloges;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.chrisworth.ghauditloges.esclient.ESClient;

@SpringBootApplication
public class GhAuditLogEsApplication implements CommandLineRunner {

	private static Logger log = LoggerFactory.getLogger(GhAuditLogEsApplication.class);
	
	@Autowired
	private AuditLogFileParser parser;
	
	@Autowired
	private ESClient esClient;
	
	public static void main(String[] args) {
		SpringApplication.run(GhAuditLogEsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Creating ES index template");
		esClient.createIndexTemplate();
		Thread.sleep(5000);
		
		log.info("Parsing audit log file...");
		List<String> records = parser.loadFile();
		log.info("Start loading GH audit records into ES...");
		
		int counter = 1;
		int success = 0;
		HashMap<Integer, String> errors = new HashMap<Integer, String>();
		for (String record : records) {
			if (counter % 2500 == 0) {
				log.info("Indexed 2500 records - pausing 3 seconds");
				Thread.sleep(3000);
			}
			String result = esClient.addDocument(record);
			if ("ERROR".equals(result)) {
				errors.put(counter, record);
			} else {
				success++;
			}
			counter++;
		}
		
		for (Integer index : errors.keySet()) {
			log.error(index.toString() + " - " + errors.get(index));
		}
		
		log.info("Done loading GH audit records into ES");
		log.info("Loaded record count: " + success);
		log.info("Error record count: " + errors.size());
		
	}

}
