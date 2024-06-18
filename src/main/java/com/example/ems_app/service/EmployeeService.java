package com.example.ems_app.service;

import com.example.ems_app.dto.EmployeeCriteriaDTO;
import com.example.ems_app.dto.EmployeeDTO;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeService {
    List<EmployeeDTO> findAll();
    List<EmployeeDTO> findAllWithOptionalFields(EmployeeCriteriaDTO employeeCriteriaDTO);
    List<EmployeeDTO> findAllByPositionsAndDepartmentsAndSalaryRangeWithOptionalFields(String positions, String departments,
            BigDecimal salaryFrom, BigDecimal salaryTo);
    EmployeeDTO findById(Long id);
    EmployeeDTO findByEmail(String email);
    List<EmployeeDTO> findEmployeesWithSalaryGreaterThan(BigDecimal salary);
    List<EmployeeDTO> findEmployeesWithSalaryLessThan(BigDecimal salary);
    List<EmployeeDTO> findEmployeesBySalaryRange(BigDecimal salaryFrom, BigDecimal salaryTo);
    List<EmployeeDTO> findEmployeesByPositionAndSalaryGreaterThan(String position, BigDecimal salary);
    List<EmployeeDTO> findEmployeesByPositionAndSalaryLessThan(String position, BigDecimal salary);
    EmployeeDTO findEmployeeByHighestSalary();
    EmployeeDTO findEmployeeByLowestSalary();
    EmployeeDTO create(EmployeeDTO employeeDTO);
    EmployeeDTO update(Long id, EmployeeDTO employeeDTO);
    void delete(Long id);

    // helping
    boolean isEmailTakenByAnotherUser(EmployeeDTO employeeDTO, String email);
}
