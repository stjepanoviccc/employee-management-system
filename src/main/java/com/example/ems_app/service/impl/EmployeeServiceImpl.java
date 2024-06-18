package com.example.ems_app.service.impl;

import com.example.ems_app.dto.EmployeeDTO;
import com.example.ems_app.exception.BadRequestException;
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
        Employee employee = employeeRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Employee with id %s not found", id)));
        return convertToDto(employee);
    }

    @Override
    public EmployeeDTO findByEmail(String email) {
        Employee employee = employeeRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundException(String.format("Employee with email %s not found", email)));
        return convertToDto(employee);
    }

    @Override
    public EmployeeDTO create(EmployeeDTO employeeDTO) {
        if(employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        return convertToDto(employeeRepository.save(employeeDTO.convertToModel()));
    }

    @Override
    public EmployeeDTO update(Long id, EmployeeDTO employeeDTO) {
        EmployeeDTO existingEmployeeDTO = findById(id);

        if(isEmailTakenByAnotherUser(existingEmployeeDTO, employeeDTO.getEmail())) {
            throw new BadRequestException("The email you are trying to enter is already taken by another user.");
        }

        existingEmployeeDTO.setFirstName(employeeDTO.getFirstName());
        existingEmployeeDTO.setLastName(employeeDTO.getLastName());
        existingEmployeeDTO.setEmail(employeeDTO.getEmail());
        existingEmployeeDTO.setPosition(employeeDTO.getPosition());
        existingEmployeeDTO.setSalary(employeeDTO.getSalary());

        return convertToDto(employeeRepository.save(existingEmployeeDTO.convertToModel()));
    }

    @Override
    public void delete(Long id) {
        EmployeeDTO existingEmployeeDTO = findById(id);
        employeeRepository.deleteById(existingEmployeeDTO.getId());
    }

    // helping
    public boolean isEmailTakenByAnotherUser(EmployeeDTO firstEmployeeDTO, String email) {

        // in this block im checking if new email already exists with another user
        if (employeeRepository.existsByEmail(email)) {
            EmployeeDTO anotherEmployeeDTO = findByEmail(email);
            if (!anotherEmployeeDTO.getId().equals(firstEmployeeDTO.getId())) {
                return true;
            }
        }

        return false;
    }
}
