package com.mindex.challenge.data;

import java.util.List;
import org.springframework.data.annotation.Id;
import lombok.Data;

@Data
public class Employee {
    @Id
    String employeeId;

    String firstName;

    String lastName;

    String position;

    String department;

    List<ReportId> directReports;
}
