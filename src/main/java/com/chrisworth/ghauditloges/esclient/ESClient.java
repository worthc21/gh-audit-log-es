package com.chrisworth.ghauditloges.esclient;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.chrisworth.ghauditloges.config.ElasticSearchProperties;

@Component
@EnableConfigurationProperties(ElasticSearchProperties.class)
public class ESClient {
	
	private static final Logger log = LoggerFactory.getLogger(ESClient.class);
	
	@Autowired
	private ElasticSearchProperties properties;
	
	private String esHost;
	private String indexPath;
	
	@PostConstruct
	public void init() {
		esHost = properties.getScheme() + "://" + properties.getHost() + ":" + properties.getPort();
		indexPath = "/" + properties.getIndexName() + "/_doc";
		log.info("Confgired ES host - " + esHost);
		log.info("Configured ES index path - " + indexPath);
	}
	
	public String addDocument(String jsonData) {
		String url = esHost + indexPath;
		return SimpleHttpClient.makePostRequest(url, jsonData);
	}
	
	public void createIndexTemplate() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		String reqBody = IOUtils.toString(classLoader.getResourceAsStream("index-template.json"));
		String url = esHost + "/_index_template/template_1";
		SimpleHttpClient.makePostRequest(url, reqBody);
	}
	
}
