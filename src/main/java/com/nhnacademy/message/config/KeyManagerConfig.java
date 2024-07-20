package com.nhnacademy.message.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KeyManagerConfig {
    private final RestTemplate restTemplate;

    @Value("${key-manager.api.key}")
    private String apiKey;

    @Value("${user.access.key.id}")
    private String accessKeyId;

    @Value("${secret.access.key}")
    private String accessKeySecret;

    @Value("${secret.key.redis.db}")
    private String redisDbKey;

    @Value("${secret.key.redis.host}")
    private String redisHostKey;

    @Value("${secret.key.redis.password}")
    private String redisPasswordKey;

    @Value("${secret.key.redis.port}")
    private String redisPortKey;

    @Value("${secret.key.jwt}")
    private String jwtKey;

    @Value("${secret.key.mail.password}")
    private String mailPasswordKey;


    private static final String BASE_URL = "https://api-keymanager.nhncloudservice.com/keymanager/v1.2/appkey/";

    @Bean
    public Integer redisDb() {
        String redisDb = getKey(getSecret(redisDbKey));
        log.info("redisDb: {}", redisDb);
        return Integer.parseInt(redisDb);
    }

    @Bean
    public String redisPassword() {
        String redisPassword = getKey(getSecret(redisPasswordKey));
        log.info("redisPassword: {}", redisPassword);
        return redisPassword;
    }

    @Bean
    public String redisHost() {
        String redisHost = getKey(getSecret(redisHostKey));
        log.info("redisHost: {}", redisHost);
        return redisHost;
    }

    @Bean
    public Integer redisPort() {
        String redisPort = getKey(getSecret(redisPortKey));
        log.info("redisPort: {}", redisPort);
        return Integer.parseInt(redisPort);
    }

    @Bean
    public SecretKey jwtKey() {
        String jwt = getKey(getSecret(jwtKey));
        log.info("jwtKey: {}", jwt);
        return new SecretKeySpec(jwt.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    @Bean
    public String emailPassword() {
        String emailPassword = getKey(getSecret(mailPasswordKey));
        log.info("emailPassword: {}", emailPassword);
        return emailPassword;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return objectMapper;
    }

    private String getKey(String jsonResponse) {
        try {
            Map<String, Object> responseMap = new ObjectMapper().readValue(jsonResponse, Map.class);
            Map<String, Object> bodyMap = (Map<String, Object>) responseMap.get("body");
            return (String) bodyMap.get("secret");
        } catch (Exception e) {
            log.error("Error parsing JSON response", e);
            return null;
        }
    }

    private String getSecret(String secretKey) {
        String url = BASE_URL + apiKey + "/secrets/" + secretKey;
        HttpHeaders headers = getAccessHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
    }

    private HttpHeaders getAccessHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TC-AUTHENTICATION-ID", accessKeyId);
        headers.add("X-TC-AUTHENTICATION-SECRET", accessKeySecret);
        return headers;
    }
}
