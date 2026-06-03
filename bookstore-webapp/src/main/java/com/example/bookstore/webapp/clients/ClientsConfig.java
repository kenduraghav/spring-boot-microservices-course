package com.example.bookstore.webapp.clients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.example.bookstore.webapp.ApplicationProperties;
import com.example.bookstore.webapp.clients.catalog.CatalogServiceClient;
import com.example.bookstore.webapp.clients.orders.OrderServiceClient;

@Configuration
class ClientsConfig {

	private final ApplicationProperties applicationProperties;

	ClientsConfig(ApplicationProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	@Bean
	CatalogServiceClient catalogServiceClient(RestClient.Builder builder) {
		RestClient restClient = RestClient.builder()
				.baseUrl(applicationProperties.apiGatewayUrl())
				.build();
		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
				.build();
		return factory.createClient(CatalogServiceClient.class);
	}

	@Bean
	OrderServiceClient orderServiceClient(RestClient.Builder builder) {
		RestClient restClient = RestClient.builder()
				.baseUrl(applicationProperties.apiGatewayUrl())
				.build();
		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
				.build();
		return factory.createClient(OrderServiceClient.class);
	}
}
