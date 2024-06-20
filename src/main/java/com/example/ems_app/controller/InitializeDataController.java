package com.example.ems_app.controller;

import com.example.ems_app.dto.EmployeeDTO;
import com.example.ems_app.model.Employee;
import com.example.ems_app.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@Tag(name="init")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/init")
public class InitializeDataController {

    private final EmployeeService employeeService;

    @Operation(summary = "INITIALIZE DATA - ADD 5 EMPLOYEES IF EMPLOYEES TABLE IS EMPTY")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Data init successfully"),
            @ApiResponse(responseCode = "400", description = "Data init failed because employees table is not empty")
    })
    @GetMapping
    public ResponseEntity<List<Employee>> dataInit() {
        return ResponseEntity.status(CREATED).body(employeeService.initData());
    };
}
