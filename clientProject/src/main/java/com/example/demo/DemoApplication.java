package com.example.demo;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.SSLContext;

@SpringBootApplication
public class DemoApplication {

	@Value("${server.ssl.trust-store-password}")
	private String trustStorePassword;
	@Value("${server.ssl.trust-store}")
	private Resource trustStore;
	@Value("${server.ssl.key-store-password}")
	private String keyStorePassword;
	@Value("${server.ssl.key-password}")
	private String keyPassword;
	@Value("${server.ssl.key-store}")
	private Resource keyStore;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() throws Exception {
		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
		restTemplate.setErrorHandler(
				new DefaultResponseErrorHandler() {
					@Override
					protected boolean hasError(HttpStatus statusCode) {
						return false;
					}
				});

		return restTemplate;
	}

	private ClientHttpRequestFactory clientHttpRequestFactory() throws Exception {
		return new HttpComponentsClientHttpRequestFactory(httpClient());
	}

	private CloseableHttpClient httpClient() throws Exception {
		// Load our keystore and truststore containing certificates that we trust.
		SSLContext sslcontext =
				SSLContexts.custom().loadTrustMaterial(trustStore.getFile(), trustStorePassword.toCharArray())
						.loadKeyMaterial(keyStore.getFile(), keyStorePassword.toCharArray(),
								keyPassword.toCharArray()).build();
		SSLConnectionSocketFactory sslConnectionSocketFactory =
				new SSLConnectionSocketFactory(sslcontext, new NoopHostnameVerifier());
		return HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
	}
}
