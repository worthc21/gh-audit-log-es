package com.chrisworth.ghauditloges;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.chrisworth.ghauditloges.config.AuditLogFileProperties;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;

@Component
@EnableConfigurationProperties(AuditLogFileProperties.class)
public class AuditLogFileParser {

	private static Logger log = LoggerFactory.getLogger(AuditLogFileParser.class);

	@Autowired
	private AuditLogFileProperties properties;
	
	public List<String> loadFile() throws IOException {
		List<String> recordJsonStrings = new ArrayList<String>();
		File file = new File(properties.getFilename());
		Reader reader = new FileReader(file);
		Gson gson = new GsonBuilder()
	    	.setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
	    	.create();
		List<Object> records = gson.fromJson(reader, ArrayList.class);
		if (records != null) {
			log.debug("Total GH audit records: " + records.size());
			for (Object r : records) {
				recordJsonStrings.add(gson.toJson(r));
			}
		} else {
			log.warn("Parsed GH audit records list was null");
		}
		return recordJsonStrings;
	}
	
}
