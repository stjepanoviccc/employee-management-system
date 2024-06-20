package com.example.ems_app.dto;

import com.example.ems_app.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String position;
    private BigDecimal salary;
    private String department;

    public static EmployeeDTO convertToDto(Employee employee) {
        return EmployeeDTO.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .position(employee.getPosition())
                .salary(employee.getSalary())
                .department(employee.getDepartment())
                .build();
    }

    public Employee convertToModel() {
        return Employee.builder()
                .id(getId())
                .firstName(getFirstName())
                .lastName(getLastName())
                .email(getEmail())
                .position(getPosition())
                .salary(getSalary())
                .department(getDepartment())
                .build();
    }
}
