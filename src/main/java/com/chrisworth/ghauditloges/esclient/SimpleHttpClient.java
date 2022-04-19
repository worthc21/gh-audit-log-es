package com.chrisworth.ghauditloges.esclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleHttpClient {
	
	private static final Logger log = LoggerFactory.getLogger(SimpleHttpClient.class);

	public static String makePostRequest(String url, String postData) { 
		return makePostRequest(url, postData, null);
	}
	public static String makePostRequest(String url, String postData, String authHeader) {
		return makePostOrPutRequest(url, postData, "POST", authHeader);
	}
	
	public static String makePutRequest(String url, String putData) {
		return makePutRequest(url, putData, null);
	}
	public static String makePutRequest(String url, String putData, String authHeader) {
		return makePostOrPutRequest(url, putData, "PUT", authHeader);
	}
	
	public static String makePostOrPutRequest(String url, String data, String method, String authHeader) {
		String result = null;
		
		log.debug("Executing HTTP " + method + " for: " + url);
		
		HttpURLConnection connection = null;
		OutputStreamWriter outputStreamWriter = null;
		BufferedReader reader = null;
		InputStream is = null;
		int responseCode;
		
		try {
			URL httpUrl = new URL(url);
			connection = (HttpURLConnection) httpUrl.openConnection();
			connection.setRequestMethod(method);
			connection.setRequestProperty("Content-Type", "application/json");
			if (authHeader != null) {
				connection.setRequestProperty("Authorization", authHeader);
			}
			
			if (data != null) {
				connection.setDoOutput(true);
				outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
		        outputStreamWriter.write(data);
		        outputStreamWriter.flush();
			}

	        responseCode = connection.getResponseCode();
	        log.debug("Got response code - " + responseCode + " for: " + url);
	        
	        StringBuilder response = new StringBuilder();
	        if (responseCode >= 200 && responseCode < 300) {
		        	is = connection.getInputStream();
		        	reader = new BufferedReader(new InputStreamReader(is));
		        	String line;
		        	while ((line = reader.readLine()) != null) { 
		        		response.append(line);
		        	}
		        	
		        	result = response.toString();
		        	
	        } else if (responseCode >= 400 && responseCode < 500) {
		        	is = connection.getErrorStream();
		        	reader = new BufferedReader(new InputStreamReader(is));
		        	StringBuilder error = new StringBuilder();
		        	String line;
		        	while ((line = reader.readLine()) != null) { 
		        		error.append(line);
		        	}
		        	log.error("Received error response from HTTP server");
		        	log.error(error.toString());
		        	log.error(data);
		        	
		        	result = "ERROR";
	        }
	        
		} catch (MalformedURLException e) {
			log.error("Caught MalformedURLException for url: " + url + " / " + e.getMessage());
		} catch (IOException e) {
			log.error("Caught IOException during " + method + " call for url: " + url + " / " + e.getMessage());
		} finally {
			try {
				if (outputStreamWriter != null) {
					outputStreamWriter.close();
				}
				if (is != null) {
					is.close();
				}
				if (reader != null) {
					reader.close();
				}
				if (connection != null) {
					connection.disconnect();
				}
			} catch (IOException e) {
				log.warn("Caught IOException while closing resources - " + e.getMessage());
			}
		}
		
		return result;
	}
	
	public static String makeGetRequest(String url) {
		return makeGetRequest(url, null);
	}
	
	public static String makeGetRequest(String url, String authHeader) {
		String result = null;
		
		log.debug("Executing HTTP GET for: " + url);
		
		HttpURLConnection connection = null;
		BufferedReader reader = null;
		InputStream is = null;
		int responseCode;
		
		try {
			URL httpUrl = new URL(url);
			connection = (HttpURLConnection) httpUrl.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			if (authHeader != null) {
				connection.setRequestProperty("Authorization", authHeader);
			}
			
	        responseCode = connection.getResponseCode();
	        log.debug("Got response code - " + responseCode);
	        
	        StringBuilder response = new StringBuilder();
	        if (responseCode >= 200 && responseCode < 300) {
		        	is = connection.getInputStream();
		        	reader = new BufferedReader(new InputStreamReader(is));
		        	String line;
		        	while ((line = reader.readLine()) != null) { 
		        		response.append(line);
		        	}
	        } else if (responseCode >= 400 && responseCode < 500) {
		        	is = connection.getErrorStream();
		        	reader = new BufferedReader(new InputStreamReader(is));
		        	StringBuilder error = new StringBuilder();
		        	String line;
		        	while ((line = reader.readLine()) != null) { 
		        		error.append(line);
		        	}
		        	log.error("Received error response from HTTP server");
		        	log.error(error.toString());
	        }
	        
	        result = response.toString();
			
		} catch (MalformedURLException e) {
			log.error("Caught MalformedURLException for url: " + url + " / " + e.getMessage());
		} catch (IOException e) {
			log.error("Caught IOException during POST call for url: " + url + " / " + e.getMessage());
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (connection != null) {
					connection.disconnect();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				log.warn("Caught IOException while closing resources - " + e.getMessage());
			}
		}
		
		return result;
	}
	
	public String makeDeleteRequest(String url) {
		return makeDeleteRequest(url, null);
	}
	
	public String makeDeleteRequest(String url, String authHeader) {
		String result = null;
		
		log.debug("Executing HTTP DELETE for: " + url);
		
		HttpURLConnection connection = null;
		BufferedReader reader = null;
		InputStream is = null;
		int responseCode;
		
		try {
			URL httpUrl = new URL(url);
			connection = (HttpURLConnection) httpUrl.openConnection();
			connection.setRequestMethod("DELETE");
			connection.setRequestProperty("Content-Type", "application/json");
			if (authHeader != null) {
				connection.setRequestProperty("Authorization", authHeader);
			}
			connection.setDoOutput(false);
			
	        responseCode = connection.getResponseCode();
	        log.debug("Got response code - " + responseCode);
	        
	        StringBuilder response = new StringBuilder();
	        if (responseCode >= 200 && responseCode < 300) {
		        	is = connection.getInputStream();
		        	reader = new BufferedReader(new InputStreamReader(is));
		        	String line;
		        	while ((line = reader.readLine()) != null) { 
		        		response.append(line);
		        	}
	        } else if (responseCode >= 400 && responseCode < 500) {
		        	is = connection.getErrorStream();
		        	reader = new BufferedReader(new InputStreamReader(is));
		        	StringBuilder error = new StringBuilder();
		        	String line;
		        	while ((line = reader.readLine()) != null) { 
		        		error.append(line);
		        	}
		        	log.error("Received error response from HTTP server");
		        	log.error(error.toString());
	        }
	        
	        result = response.toString();
			
		} catch (MalformedURLException e) {
			log.error("Caught MalformedURLException for url: " + url + " / " + e.getMessage());
		} catch (IOException e) {
			log.error("Caught IOException during DELETE call for url: " + url + " / " + e.getMessage());
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (connection != null) {
					connection.disconnect();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				log.warn("Caught IOException while closing resources - " + e.getMessage());
			}
		}
		
		return result;
	}
	
}
