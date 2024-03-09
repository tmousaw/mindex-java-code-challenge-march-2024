package com.mindex.challenge.controller;

import static com.mindex.challenge.utility.EmployeeBuilder.createTestEmployee;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.utility.EmployeeBuilder.CreateTestEmployee;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIT {

    private String employeeUrl;
    private String employeeIdUrl;
    private String reportingStructureUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        reportingStructureUrl = employeeIdUrl + "/reporting";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee);
        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = readEmployee(createdEmployee.getEmployeeId());
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");


        Employee updatedEmployee = updateEmployee(readEmployee);

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    private Employee readEmployee(String id) {
        return restTemplate.getForEntity(employeeIdUrl, Employee.class, id).getBody();
    }

    private Employee updateEmployee(Employee employee) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return restTemplate.exchange(employeeIdUrl,
            HttpMethod.PUT,
            new HttpEntity<>(employee, headers),
            Employee.class,
            employee.getEmployeeId()).getBody();
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

    @Test
    public void testReportingStructure() {
        // The example from the README.md file is already bootstrapped.
        final Employee johnLennon = readEmployee("16a596ae-edd3-4847-99fe-c4518e82c86f");

        ReportingStructure reportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, johnLennon.getEmployeeId()).getBody();
        assertNotNull(reportingStructure);
        assertEmployeeEquivalence(johnLennon, reportingStructure.getEmployee());
        assertEquals(4, reportingStructure.getNumberOfReports());

        // Now let's suppose we add a direct report for George Harrison who also has a direct report.
        final Employee johnDoe = createEmployee("John", "Doe", null);
        final Employee janeDoe = createEmployee("Jane", "Smith", Lists.newArrayList(johnDoe.getEmployeeId()));
        Employee georgeHarrison = readEmployee("c0c2293d-16bd-4603-8e08-638a9d18b22c");
        georgeHarrison.setDirectReports(Lists.newArrayList(janeDoe.getEmployeeId()));
        updateEmployee(georgeHarrison);

        // Adding two reports underneath George Harrison should also add two to John Lennon.
        reportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, johnLennon.getEmployeeId()).getBody();
        assertNotNull(reportingStructure);
        assertEmployeeEquivalence(johnLennon, reportingStructure.getEmployee());
        assertEquals(6, reportingStructure.getNumberOfReports());
    }

    private Employee createEmployee(String firstName, String lastName, List<String> directReports) {
        final Employee employee = createTestEmployee(CreateTestEmployee.builder().firstName(firstName).lastName(lastName).directReports(directReports).build());
        return restTemplate.postForEntity(employeeUrl, employee, Employee.class).getBody();
    }
}
