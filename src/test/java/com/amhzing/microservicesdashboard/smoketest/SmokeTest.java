package com.amhzing.microservicesdashboard.smoketest;

import com.amhzing.microservicesdashboard.MicroservicesDashboardApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MicroservicesDashboardApplication.class)
public class SmokeTest {

    @Value("${server.base-uri}")
    private String baseUri;

    @Value("${management.port}")
    private int managementPort;

    @Value("${management.context-path}")
    private String managementContextPath;

    private TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Test
    public void healthStatus() {
        final ResponseEntity<Map> entity = testRestTemplate.getForEntity(getUrl(), Map.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        final Map body = entity.getBody();
        assertThat(body).containsKey("status");
        assertThat(body.get("status")).isEqualTo("UP");
    }

    private String getUrl() {
        return baseUri + ":" + managementPort + "/" + managementContextPath + "/health";
    }
}