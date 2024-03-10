package com.mindex.challenge.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mindex.challenge.data.Compensation;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CompensationControllerIT {

  private String compensationUrl;
  private String compensationEmployeeIdUrl;

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @BeforeEach
  public void setup() {
    compensationUrl = "http://localhost:" + port + "/compensation";
    compensationEmployeeIdUrl = "http://localhost:" + port + "/compensation/{employeeId}";
  }

  @Test
  void testCreateRead() {
    final String employeeId = UUID.randomUUID().toString();
    final BigDecimal firstSalary = BigDecimal.valueOf(125000.23);
    final Calendar cal = Calendar.getInstance();
    cal.add(Calendar.YEAR, -1);
    final Date firstEffectiveDate = cal.getTime();
    Compensation firstTestCompensation = new Compensation();
    firstTestCompensation.setEmployeeId(employeeId);
    firstTestCompensation.setSalary(firstSalary);
    firstTestCompensation.setEffectiveDate(firstEffectiveDate);

    final Compensation firstCreatedCompensation = restTemplate.postForEntity(compensationUrl, firstTestCompensation, Compensation.class).getBody();

    assertNotNull(firstCreatedCompensation);
    assertCompensationEquivalence(firstTestCompensation, firstCreatedCompensation);

    List<Compensation> readCompensationList =
        restTemplate
            .exchange(
                compensationEmployeeIdUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Compensation>>() {},
                firstCreatedCompensation.getEmployeeId())
            .getBody();
    assertNotNull(readCompensationList);
    assertEquals(1, readCompensationList.size());
    assertCompensationEquivalence(firstCreatedCompensation, readCompensationList.get(0));

    final BigDecimal secondSalary = BigDecimal.valueOf(150000.46);
    cal.add(Calendar.YEAR, 1);
    final Date secondEffectiveDate = cal.getTime();
    final Compensation secondTestCompensation = new Compensation();
    secondTestCompensation.setEmployeeId(employeeId);
    secondTestCompensation.setSalary(secondSalary);
    secondTestCompensation.setEffectiveDate(secondEffectiveDate);

    final Compensation secondCreatedCompensation = restTemplate.postForEntity(compensationUrl, secondTestCompensation, Compensation.class).getBody();

    assertNotNull(secondCreatedCompensation);
    assertCompensationEquivalence(secondTestCompensation, secondCreatedCompensation);

    readCompensationList =
        restTemplate
            .exchange(
                compensationEmployeeIdUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Compensation>>() {},
                firstCreatedCompensation.getEmployeeId())
            .getBody();
    assertNotNull(readCompensationList);
    assertEquals(2, readCompensationList.size());
    final List<Compensation> expectedCompensations = Lists.newArrayList(firstCreatedCompensation, secondCreatedCompensation);
    assertTrue(expectedCompensations.containsAll(readCompensationList) && readCompensationList.containsAll(expectedCompensations));
  }

  private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
    assertEquals(expected.getEmployeeId(), actual.getEmployeeId());
    assertEquals(expected.getSalary(), actual.getSalary());
    assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
  }
}
