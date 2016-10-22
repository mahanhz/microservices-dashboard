package com.amhzing.microservicesdashboard.integrationtest;

import com.amhzing.microservicesdashboard.MicroservicesDashboardApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.LocalManagementPort;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MicroservicesDashboardApplication.class,
				webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MicroservicesDashboardApplicationTest {

	@LocalServerPort
	private int port = 0;

	@LocalManagementPort
	private int managementPort = 0;

	@Value("${server.context-path}")
	private String serverContextPath;

	@Value("${management.context-path}")
	private String managementContextPath;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void configurationAvailable() {
		final ResponseEntity<String> entity = testRestTemplate.getForEntity(
				"http://localhost:" + port + serverContextPath + "/config-message", String.class);

		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void managementAvailable() {
		ResponseEntity<Map> entity = testRestTemplate.getForEntity(
				"http://localhost:" + managementPort + managementContextPath, Map.class);

		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void envPostAvailable() {
		final MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		final ResponseEntity<Map> entity = testRestTemplate.postForEntity(
				"http://localhost:" + managementPort + managementContextPath + "/env", form, Map.class);

		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
}
