package com.inzyme.spatiotemporal.web.ai.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Author: fly
 * @Date: 2021/08/14 19:18
 * @Description:
 */

@Component
public class ESClientBuilder {



	@Value("${spring.elasticsearch.cluster_host}")
	private String cluster_host;
	@Value("${spring.elasticsearch.cluster_port}")
	private Integer cluster_port;

	@Bean
	public RestHighLevelClient getEsClient() {
		RestClientBuilder clientBuilder = RestClient.builder(new HttpHost(cluster_host,
				Integer.valueOf(cluster_port), "http"));
		RestHighLevelClient client = new RestHighLevelClient(clientBuilder);
		return client;
	}
}
