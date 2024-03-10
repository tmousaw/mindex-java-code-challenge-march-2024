package com.mindex.challenge.data;

import java.util.List;
import org.springframework.data.annotation.Id;
import lombok.Data;

@Data
public class Employee {
    @Id
    private String employeeId;
    private String firstName;
    private String lastName;
    private String position;
    private String department;
    private List<String> directReports;
}
