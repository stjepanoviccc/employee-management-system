package com.example.ems_app.service;

import com.example.ems_app.dto.EmployeeDTO;

public interface EmployeeConsumerService {
    void consumeMessage(EmployeeDTO employeeDTO);
}
