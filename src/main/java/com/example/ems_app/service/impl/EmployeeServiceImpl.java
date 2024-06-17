package com.example.ems_app.service.impl;

import com.example.ems_app.dto.EmployeeDTO;
import com.example.ems_app.exception.NotFoundException;
import com.example.ems_app.model.Employee;
import com.example.ems_app.repository.EmployeeRepository;
import com.example.ems_app.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.ems_app.dto.EmployeeDTO.convertToDto;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Override
    public List<EmployeeDTO> findAll() {
        return employeeRepository.findAll().stream()
                .map(EmployeeDTO::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO findById(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new NotFoundException("Employee not found"));
        return convertToDto(employee);
    }

    @Override
    public EmployeeDTO save(EmployeeDTO employeeDTO) {
        return convertToDto(employeeRepository.save(employeeDTO.convertToModel()));
    }

    @Override
    public EmployeeDTO update(Long id, EmployeeDTO employeeDTO) {
        EmployeeDTO existingEmployeeDTO = findById(id);

        existingEmployeeDTO.setFirstName(employeeDTO.getFirstName());
        existingEmployeeDTO.setLastName(employeeDTO.getLastName());
        existingEmployeeDTO.setEmail(employeeDTO.getEmail());
        existingEmployeeDTO.setPosition(employeeDTO.getPosition());
        existingEmployeeDTO.setSalary(employeeDTO.getSalary());

        return convertToDto(employeeRepository.save(existingEmployeeDTO.convertToModel()));
    }

    @Override
    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }
}
