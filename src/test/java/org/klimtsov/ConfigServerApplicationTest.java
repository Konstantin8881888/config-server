package org.klimtsov;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                //Явно указываем native и отключаем Git.
                "spring.cloud.config.server.git.enabled=false",
                "spring.cloud.config.server.native.search-locations=classpath:/config-repo-test",
                //Отключаем Eureka для тестов.
                "eureka.client.enabled=false",
                "eureka.client.fetch-registry=false",
                "eureka.client.register-with-eureka=false"
        }
)
class ConfigServerApplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        //Пустой тест для проверки загрузки контекста.
    }

    @Test
    void configServerReturnsUserServiceConfig() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/user-service/default",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("server.port");
        assertThat(response.getBody()).contains("9999");
    }

    @Test
    void configServerHealthEndpointWorks() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/actuator/health",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("\"status\":\"UP\"");
    }
}