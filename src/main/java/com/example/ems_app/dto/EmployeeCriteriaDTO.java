package com.example.ems_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeCriteriaDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String position;
    private BigDecimal salaryFrom;
    private BigDecimal salaryTo;
    private String department;
}
