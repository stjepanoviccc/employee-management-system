package com.example.ems_app.service;

import com.example.ems_app.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDTO> findAll();
    EmployeeDTO findById(Long id);
    EmployeeDTO save(EmployeeDTO employeeDTO);
    EmployeeDTO update(Long id, EmployeeDTO employeeDTO);
    void delete(Long id);
}
