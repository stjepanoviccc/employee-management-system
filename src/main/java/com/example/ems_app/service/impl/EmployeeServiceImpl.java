package com.example.ems_app.service.impl;

import com.example.ems_app.dto.EmployeeCriteriaDTO;
import com.example.ems_app.dto.EmployeeDTO;
import com.example.ems_app.exception.BadRequestException;
import com.example.ems_app.exception.NotFoundException;
import com.example.ems_app.model.Employee;
import com.example.ems_app.repository.EmployeeRepository;
import com.example.ems_app.repository.specs.EmployeeSpec;
import com.example.ems_app.service.EmployeeProducerService;
import com.example.ems_app.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.ems_app.dto.EmployeeDTO.convertToDto;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeProducerService employeeProducerService;

    @Override
    public List<EmployeeDTO> findAll() {
        return employeeRepository.findAll().stream()
                .map(EmployeeDTO::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> findAllWithOptionalFields(EmployeeCriteriaDTO employeeCriteriaDTO) {
        Specification<Employee> spec = EmployeeSpec.findEmployeesWithAllOptionalFields(
                employeeCriteriaDTO.getFirstName(), employeeCriteriaDTO.getLastName(), employeeCriteriaDTO.getEmail(),
                employeeCriteriaDTO.getPosition(), employeeCriteriaDTO.getSalaryFrom(), employeeCriteriaDTO.getSalaryTo(),
                employeeCriteriaDTO.getDepartment()
        );

        return employeeRepository.findAll(spec).stream()
                .map(EmployeeDTO::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> findAllByPositionsAndDepartmentsAndSalaryRangeWithOptionalFields(String positions, String departments,
                                                                                              BigDecimal salaryFrom, BigDecimal salaryTo) {
        List<String> positionsList = new ArrayList<>();
        List<String> departmentsList = new ArrayList<>();

        if (positions != null && !positions.isEmpty()) {
            positionsList = Arrays.stream(positions.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
        }

        if(departments != null && !departments.isEmpty()) {
            departmentsList = Arrays.stream(departments.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
        }

        Specification<Employee> spec = EmployeeSpec.findEmployeesByPositionsAndDepartmentsAndSalaryRangeWithOptionalFields(
                positionsList, departmentsList, salaryFrom, salaryTo
        );

        return employeeRepository.findAll(spec).stream()
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
    public List<EmployeeDTO> findEmployeesWithSalaryGreaterThan(BigDecimal salary) {
        return employeeRepository.findEmployeesWithSalaryGreaterThan(salary).stream()
                .map(EmployeeDTO::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> findEmployeesWithSalaryLessThan(BigDecimal salary) {
        return employeeRepository.findEmployeesWithSalaryLessThan(salary).stream()
                .map(EmployeeDTO::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> findEmployeesBySalaryRange(BigDecimal salaryFrom, BigDecimal salaryTo) {
        return employeeRepository.findEmployeesBySalaryRange(salaryFrom, salaryTo).stream()
                .map(EmployeeDTO::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> findEmployeesByPositionAndSalaryGreaterThan(String position, BigDecimal salary) {
        return employeeRepository.findEmployeesByPositionAndSalaryGreaterThan(position, salary).stream()
                .map(EmployeeDTO::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> findEmployeesByPositionAndSalaryLessThan(String position, BigDecimal salary) {
        return employeeRepository.findEmployeesByPositionAndSalaryLessThan(position, salary).stream()
                .map(EmployeeDTO::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO findEmployeeByHighestSalary() {
        Employee employee = employeeRepository.findEmployeeByHighestSalary().orElseThrow(() ->
                new NotFoundException("Employee with highest salary not found"));
        return convertToDto(employee);
    }

    @Override
    public EmployeeDTO findEmployeeByLowestSalary() {
        Employee employee = employeeRepository.findEmployeeByLowestSalary().orElseThrow(() ->
                new NotFoundException("Employee with lowest salary not found"));
        return convertToDto(employee);
    }

    @Override
    public EmployeeDTO create(EmployeeDTO employeeDTO) {
        if(employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        EmployeeDTO savedEmployeeDTO = convertToDto(employeeRepository.save(employeeDTO.convertToModel()));
        employeeProducerService.produceMessage(savedEmployeeDTO);
        return savedEmployeeDTO;
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
