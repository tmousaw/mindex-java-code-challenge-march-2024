package com.mindex.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataBootstrap {
    private static final String DATASTORE_LOCATION = "/static/employee_database.json";

    private final EmployeeRepository employeeRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    public DataBootstrap(EmployeeRepository employeeRepository, ObjectMapper objectMapper) {
        this.employeeRepository = employeeRepository;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        InputStream inputStream = this.getClass().getResourceAsStream(DATASTORE_LOCATION);

        Employee[] employees;

        try {
            employees = objectMapper.readValue(inputStream, Employee[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Employee employee : employees) {
            employeeRepository.insert(employee);
        }
    }
}
