package it.esercitazione.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

/**
 * Configura il RestClient verso la Producer API.
 * L'indirizzo base arriva dalla property {@code api.base-url}.
 */
@Configuration
public class RestClientConfig {

    @Bean
    public RestClient producerRestClient(
            @Value("${api.base-url}") String baseUrl,
            @Value("${api.connect-timeout-seconds:3}") long connectTimeoutSeconds,
            @Value("${api.read-timeout-seconds:5}") long readTimeoutSeconds) {

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(connectTimeoutSeconds))
                .build();

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(readTimeoutSeconds));

        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
