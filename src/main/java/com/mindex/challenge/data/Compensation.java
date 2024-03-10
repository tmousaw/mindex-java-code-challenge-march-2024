package com.mindex.challenge.data;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class Compensation {

  String employeeId;
  BigDecimal salary;
  Date effectiveDate;
}