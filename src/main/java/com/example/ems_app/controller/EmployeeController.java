package com.example.ems_app.controller;

import com.example.ems_app.dto.EmployeeCriteriaDTO;
import com.example.ems_app.dto.EmployeeDTO;
import com.example.ems_app.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.findAll());
    };

    @GetMapping("/findAllWithOptionalFields")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployeesWithAllOptionalFields(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) BigDecimal salaryFrom,
            @RequestParam(required = false) BigDecimal salaryTo,
            @RequestParam(required = false) String department
    ) {
        EmployeeCriteriaDTO employeeCriteriaDTO = new EmployeeCriteriaDTO(firstName, lastName, email, position, salaryFrom, salaryTo, department);
        return ResponseEntity.ok(employeeService.findAllWithOptionalFields(employeeCriteriaDTO));
    }

    @GetMapping("/findAllByPositionsAndDepartmentsAndSalaryRangeWithOptionalFields")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployeesByPositionsAndDepartmentsAndSalaryRangeWithOptionalFields(
            @RequestParam(required = false) String positions,
            @RequestParam(required = false) String departments,
            @RequestParam(required = false) BigDecimal salaryFrom,
            @RequestParam(required = false) BigDecimal salaryTo
    ) {
        return ResponseEntity.ok(employeeService.findAllByPositionsAndDepartmentsAndSalaryRangeWithOptionalFields(
                positions, departments, salaryFrom, salaryTo));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }

    @GetMapping("/salaryGreaterThan/{salary}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesWithSalaryGreaterThan(@PathVariable BigDecimal salary) {
        List<EmployeeDTO> employeesDTO = employeeService.findEmployeesWithSalaryGreaterThan(salary);
        return ResponseEntity.ok(employeesDTO);
    }

    @GetMapping("/salaryLessThan/{salary}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesWithSalaryLessThan(@PathVariable BigDecimal salary) {
        List<EmployeeDTO> employeesDTO = employeeService.findEmployeesWithSalaryLessThan(salary);
        return ResponseEntity.ok(employeesDTO);
    }

    @GetMapping("/salaryRange")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesBySalaryRange(
            @RequestParam BigDecimal salaryFrom,
            @RequestParam BigDecimal salaryTo) {
        List<EmployeeDTO> employeesDTO = employeeService.findEmployeesBySalaryRange(salaryFrom, salaryTo);
        return ResponseEntity.ok(employeesDTO);
    }

    @GetMapping("/positionAndSalaryGreaterThan")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByPositionAndSalaryGreaterThan(
            @RequestParam String position,
            @RequestParam BigDecimal salary) {
        List<EmployeeDTO> employeesDTO = employeeService.findEmployeesByPositionAndSalaryGreaterThan(position, salary);
        return ResponseEntity.ok(employeesDTO);
    }

    @GetMapping("/positionAndSalaryLessThan")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByPositionAndSalaryLessThan(
            @RequestParam String position,
            @RequestParam BigDecimal salary) {
        List<EmployeeDTO> employeesDTO = employeeService.findEmployeesByPositionAndSalaryLessThan(position, salary);
        return ResponseEntity.ok(employeesDTO);
    }

    @GetMapping("/highestSalary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> getEmployeeWithHighestSalary() {
        EmployeeDTO employeeDTO = employeeService.findEmployeeByHighestSalary();
        return ResponseEntity.ok(employeeDTO);
    }

    @GetMapping("/lowestSalary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> getEmployeeWithLowestSalary() {
        EmployeeDTO employeeDTO = employeeService.findEmployeeByLowestSalary();
        return ResponseEntity.ok(employeeDTO);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.status(CREATED).body(employeeService.create(employeeDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.update(id, employeeDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
