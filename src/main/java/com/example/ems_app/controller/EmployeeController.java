package com.example.ems_app.controller;

import com.example.ems_app.dto.EmployeeCriteriaDTO;
import com.example.ems_app.dto.EmployeeDTO;
import com.example.ems_app.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@Tag(name="employees")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    private final String UNAUTHORIZED = "Unauthorized";
    private final String SUCCESS_RETRIEVED_LIST = "Successfully retrieved list of employees";
    private final String EMPLOYEE_NOT_FOUND = "Employee not found";

    private final EmployeeService employeeService;

    @Operation(summary = "Get all employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESS_RETRIEVED_LIST),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED),
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.findAll());
    };

    @Operation(summary = "Get all employees where you filter by any field you want and fields are optional... You won't be able to execute this query from swagger-ui because of internal problem with sending empty params, so please use cURL i provided and adjust it however you want.",
            description = "Use the provided cURL command to execute this query(if you want to use spacebar, type %20 instead):\n\n"
                    + "```\n"
                    + "curl -X 'GET' \\\n"
                    + "  'http://localhost:8080/api/v1/employees/findAllWithOptionalFields?firstName=&lastName=&email=&position=Software%20Developer&salaryFrom=2000&salaryTo=100000&department=Dep%20A' \\\n"
                    + "  -H 'accept: */*' \\\n"
                    + "  -H 'Authorization: Bearer YOUR_ACCESS_TOKEN'\n"
                    + "```\n"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESS_RETRIEVED_LIST),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED),
    })
    @GetMapping("/findAllWithOptionalFields")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployeesWithAllOptionalFields(
            @Parameter(description = "First name of the employee")
            @RequestParam(required = false) String firstName,
            @Parameter(description = "Last name of the employee")
            @RequestParam(required = false) String lastName,
            @Parameter(description = "Email of the employee")
            @RequestParam(required = false) String email,
            @Parameter(description = "Position of the employee", example = "Software Developer")
            @RequestParam(required = false) String position,
            @Parameter(description = "Minimum salary of the employee", example = "2000")
            @RequestParam(required = false) BigDecimal salaryFrom,
            @Parameter(description = "Maximum salary of the employee", example = "100000")
            @RequestParam(required = false) BigDecimal salaryTo,
            @Parameter(description = "Department of the employee", example = "Dep A")
            @RequestParam(required = false) String department
    ) {
        EmployeeCriteriaDTO employeeCriteriaDTO = new EmployeeCriteriaDTO(
                firstName, lastName, email, position, salaryFrom, salaryTo, department
        );
        return ResponseEntity.ok(employeeService.findAllWithOptionalFields(employeeCriteriaDTO));
    }

    @Operation(summary = "Get all employees where you filter by positions, departments, and salary range " +
            "with optional fields ... Split positions and departments with ','")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESS_RETRIEVED_LIST),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED),
    })
    @GetMapping("/findAllByPositionsAndDepartmentsAndSalaryRangeWithOptionalFields")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployeesByPositionsAndDepartmentsAndSalaryRangeWithOptionalFields(
            @Parameter(description = "Comma-separated list of positions", example = "Software Developer,HR")
            @RequestParam(required = false) String positions,
            @Parameter(description = "Comma-separated list of departments", example = "Dep A,Dep B")
            @RequestParam(required = false) String departments,
            @Parameter(description = "Minimum salary", example = "2000")
            @RequestParam(required = false) BigDecimal salaryFrom,
            @Parameter(description = "Maximum salary", example = "100000")
            @RequestParam(required = false) BigDecimal salaryTo
    ) {
        return ResponseEntity.ok(employeeService.findAllByPositionsAndDepartmentsAndSalaryRangeWithOptionalFields(
                positions, departments, salaryFrom, salaryTo));
    }

    @Operation(summary = "Get employee by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved employee by ID"),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = EMPLOYEE_NOT_FOUND)
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<EmployeeDTO> getEmployeeById(
            @Parameter(description = "ID of the employee to retrieve", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }

    @Operation(summary = "Get employees with salary greater than given value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESS_RETRIEVED_LIST),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED),
    })
    @GetMapping("/salaryGreaterThan/{salary}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesWithSalaryGreaterThan(
            @Parameter(description = "Minimum salary value", example = "10000")
            @PathVariable BigDecimal salary) {
        List<EmployeeDTO> employeesDTO = employeeService.findEmployeesWithSalaryGreaterThan(salary);
        return ResponseEntity.ok(employeesDTO);
    }

    @Operation(summary = "Get employees with salary less than given value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESS_RETRIEVED_LIST),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED),
    })
    @GetMapping("/salaryLessThan/{salary}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesWithSalaryLessThan(
            @Parameter(description = "Maximum salary value", example = "100000")
            @PathVariable BigDecimal salary) {
        List<EmployeeDTO> employeesDTO = employeeService.findEmployeesWithSalaryLessThan(salary);
        return ResponseEntity.ok(employeesDTO);
    }

    @Operation(summary = "Get employees within salary range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESS_RETRIEVED_LIST),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED),
    })
    @GetMapping("/salaryRange")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesBySalaryRange(
            @Parameter(description = "Minimum salary value", example = "10000")
            @RequestParam BigDecimal salaryFrom,
            @Parameter(description = "Maximum salary value", example = "30000")
            @RequestParam BigDecimal salaryTo) {
        List<EmployeeDTO> employeesDTO = employeeService.findEmployeesBySalaryRange(salaryFrom, salaryTo);
        return ResponseEntity.ok(employeesDTO);
    }

    @Operation(summary = "Get employees by position and salary greater than given value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESS_RETRIEVED_LIST),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED)
    })
    @GetMapping("/positionAndSalaryGreaterThan")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByPositionAndSalaryGreaterThan(
            @Parameter(description = "Position of the employee", example = "Software Developer")
            @RequestParam String position,
            @Parameter(description = "Minimum salary value", example = "50000")
            @RequestParam BigDecimal salary) {
        List<EmployeeDTO> employeesDTO = employeeService.findEmployeesByPositionAndSalaryGreaterThan(position, salary);
        return ResponseEntity.ok(employeesDTO);
    }

    @Operation(summary = "Get employees by position and salary less than given value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESS_RETRIEVED_LIST),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED)
    })
    @GetMapping("/positionAndSalaryLessThan")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByPositionAndSalaryLessThan(
            @Parameter(description = "Position of the employee", example = "Software Developer")
            @RequestParam String position,
            @Parameter(description = "Maximum salary value", example = "100000")
            @RequestParam BigDecimal salary) {
        List<EmployeeDTO> employeesDTO = employeeService.findEmployeesByPositionAndSalaryLessThan(position, salary);
        return ResponseEntity.ok(employeesDTO);
    }

    @Operation(summary = "Get employee with highest salary")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESS_RETRIEVED_LIST),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = EMPLOYEE_NOT_FOUND)
    })
    @GetMapping("/highestSalary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> getEmployeeWithHighestSalary() {
        EmployeeDTO employeeDTO = employeeService.findEmployeeByHighestSalary();
        return ResponseEntity.ok(employeeDTO);
    }

    @Operation(summary = "Get employee with lowest salary")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESS_RETRIEVED_LIST),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = EMPLOYEE_NOT_FOUND)
    })
    @GetMapping("/lowestSalary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> getEmployeeWithLowestSalary() {
        EmployeeDTO employeeDTO = employeeService.findEmployeeByLowestSalary();
        return ResponseEntity.ok(employeeDTO);
    }

    @Operation(summary = "Create a new employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee successfully created"),
            @ApiResponse(responseCode = "400", description = "Employee with this email already exists"),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED),
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> createEmployee(
            @Parameter(description = "Employee details to create")
            @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.status(CREATED).body(employeeService.create(employeeDTO));
    }

    @Operation(summary = "Update an existing employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee successfully updated"),
            @ApiResponse(responseCode = "400", description = "The email you are trying to enter is already taken by another user"),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = EMPLOYEE_NOT_FOUND)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @Parameter(description = "ID of the employee to update", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Employee details to update")
            @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.update(id, employeeDTO));
    }

    @Operation(summary = "Delete an employee by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee successfully deleted"),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = EMPLOYEE_NOT_FOUND)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEmployee(
            @Parameter(description = "ID of the employee to delete", example = "1")
            @PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
