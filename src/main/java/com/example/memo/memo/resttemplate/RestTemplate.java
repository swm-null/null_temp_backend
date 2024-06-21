package com.example.memo.memo.resttemplate;

import org.springframework.stereotype.Service;

@Service
public class RestTemplate {
	private static String aiUrl = "http://rapisim.mynetgear.com:8000/user_query?query=";

	public Object callRestTemplate(String content) {

		final String callUri = String.format("%s----%s", aiUrl, content);

		return null;
	}
}
