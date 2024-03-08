package com.mindex.challenge.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportId;
import lombok.Builder;
import lombok.Value;

public class EmployeeBuilder {

  public static Employee createTestEmployee(CreateTestEmployee e) {
    final Employee employee = new Employee();
    employee.setEmployeeId(e.getEmployeeId());
    employee.setFirstName(e.getFirstName());
    employee.setLastName(e.getLastName());
    employee.setPosition(e.getPosition());
    employee.setDepartment(e.getDepartment());
    employee.setDirectReports(e.getDirectReports());
    return employee;
  }

  @Value
  @Builder(toBuilder = true)
  public static class CreateTestEmployee {

    @Builder.Default
    String employeeId = UUID.randomUUID().toString();

    @Builder.Default
    String firstName = "John";

    @Builder.Default
    String lastName = "Doe";

    @Builder.Default
    String position = "Developer";

    @Builder.Default
    String department = "Engineering";

    @Builder.Default
    List<ReportId> directReports = new ArrayList<>();
  }
}
