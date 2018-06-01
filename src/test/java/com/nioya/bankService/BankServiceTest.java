package com.nioya.bankService;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.nioya.objects.Transaction;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * Basic integration tests for service demo application.
 *
 * @author Emrah Onder
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.port=0"})
public class BankServiceTest {

	@LocalServerPort
	private int port;

	@Value("${local.management.port}")
	private int mgt;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void getStatisticsTest() throws Exception {
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> entity = this.testRestTemplate.getForEntity(
				"http://localhost:" + this.port + "/statistics", Map.class);

		then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void setTransactionSuccessTest() throws Exception {
		Transaction transaction = new Transaction();
		transaction.setAmount(1);
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		transaction.setTimestamp(currentTimestamp.getTime());
		ResponseEntity<Map> entity = this.testRestTemplate.postForEntity(
				"http://localhost:" + this.port + "/transactions",transaction, Map.class);
		then(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}
	
	@Test
	public void setTransactionFailureTest() throws Exception {
		Transaction transaction = new Transaction();
		transaction.setAmount(Double.parseDouble("1.0"));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime( new Date());
		calendar.add(Calendar.MINUTE, -2); // 2 min back

		transaction.setTimestamp(calendar.getTimeInMillis());
		ResponseEntity<Map> entity = this.testRestTemplate.postForEntity(
				"http://localhost:" + this.port + "/transactions",transaction, Map.class);
		then(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
	
	@Test
	public void getStatisticsDetailsTest() throws Exception {
		ResponseEntity<Map> entity = this.testRestTemplate.getForEntity(
				"http://localhost:" + this.port + "/statistics", Map.class);

		then(entity.getBody().get("sum")).isEqualTo(1.0);
	}

}

