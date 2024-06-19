package com.example.ems_app.controller;

import com.example.ems_app.config.SecurityConfiguration;
import com.example.ems_app.dto.EmployeeCriteriaDTO;
import com.example.ems_app.dto.EmployeeDTO;
import com.example.ems_app.model.Employee;
import com.example.ems_app.service.impl.AuthServiceImpl;
import com.example.ems_app.service.impl.EmployeeServiceImpl;
import com.example.ems_app.service.impl.JwtServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.ems_app.dto.EmployeeDTO.convertToDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@Import(SecurityConfiguration.class)
public class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final EmployeeDTO employeeDTO = convertToDto(createEmployee(1L));
    private final List<EmployeeDTO> employeesDTOList = Arrays.asList(convertToDto(createEmployee(1L)), convertToDto(createEmployee(2L)));
    private final BigDecimal salaryFrom = BigDecimal.valueOf(500);
    private final BigDecimal salaryTo = BigDecimal.valueOf(20000);
    private final EmployeeCriteriaDTO employeeCriteriaDTO = new EmployeeCriteriaDTO(
            employeeDTO.getFirstName(), employeeDTO.getLastName(), employeeDTO.getEmail(), employeeDTO.getPosition(),
            salaryFrom, salaryTo, employeeDTO.getDepartment());
    private final String baseUrl = "/api/v1/employees";

    private Employee createEmployee(Long id) {
        return Employee.builder()
                .id(id)
                .firstName("Andrej")
                .lastName("Stj")
                .position("Software Developer")
                .email("andrej@gmail.com")
                .salary(BigDecimal.valueOf(1000))
                .department("Dep A")
                .build();
    }

    @MockBean
    private EmployeeServiceImpl employeeService;

    @MockBean
    private JwtServiceImpl jwtService;

    @MockBean
    private AuthServiceImpl authService;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @Test
    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    public void testGetAllEmployees() throws Exception {
        when(employeeService.findAll()).thenReturn(employeesDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    public void testGetAllEmployeesWithAllOptionalFields() throws Exception {
        when(employeeService.findAllWithOptionalFields(any(EmployeeCriteriaDTO.class))).thenReturn(employeesDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/findAllWithOptionalFields", baseUrl))
                        .param("firstName", employeeCriteriaDTO.getFirstName())
                        .param("lastName", employeeCriteriaDTO.getLastName())
                        .param("email", employeeCriteriaDTO.getEmail())
                        .param("position", employeeDTO.getPosition())
                        .param("salaryFrom", salaryFrom.toString())
                        .param("salaryTo", salaryTo.toString())
                        .param("department", employeeCriteriaDTO.getDepartment()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));

        verify(employeeService, times(1)).findAllWithOptionalFields(any(EmployeeCriteriaDTO.class));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    public void testGetAllEmployeesByPositionsAndDepartmentsAndSalaryRangeWithOptionalFields() throws Exception {
        when(employeeService.findAllByPositionsAndDepartmentsAndSalaryRangeWithOptionalFields(
                anyString(), anyString(), any(BigDecimal.class), any(BigDecimal.class)
        )).thenReturn(employeesDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/findAllByPositionsAndDepartmentsAndSalaryRangeWithOptionalFields", baseUrl))
                        .param("positions", employeeDTO.getPosition())
                        .param("salaryFrom", salaryFrom.toString())
                        .param("salaryTo", salaryTo.toString())
                        .param("departments", employeeCriteriaDTO.getDepartment()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));;

        verify(employeeService, times(1)).findAllByPositionsAndDepartmentsAndSalaryRangeWithOptionalFields(
                anyString(), anyString(), any(BigDecimal.class), any(BigDecimal.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    public void testGetEmployeeById() throws Exception {
        when(employeeService.findById(employeeDTO.getId())).thenReturn(employeeDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.get(String.format("%s/{id}", baseUrl), employeeDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(employeeDTO.getId()));

        verify(employeeService, times(1)).findById(employeeDTO.getId());
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    public void testGetEmployeesWithSalaryGreaterThan() throws Exception {
        when(employeeService.findEmployeesWithSalaryGreaterThan(employeeDTO.getSalary())).thenReturn(employeesDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/salaryGreaterThan/{salary}", baseUrl), employeeDTO.getSalary()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));;

        verify(employeeService, times(1)).findEmployeesWithSalaryGreaterThan(employeeDTO.getSalary());
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    public void testGetEmployeesWithSalaryLessThan() throws Exception {
        when(employeeService.findEmployeesWithSalaryLessThan(employeeDTO.getSalary())).thenReturn(employeesDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/salaryLessThan/{salary}", baseUrl), employeeDTO.getSalary()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));;

        verify(employeeService, times(1)).findEmployeesWithSalaryLessThan(employeeDTO.getSalary());
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    public void testGetEmployeesBySalaryRange() throws Exception {
        when(employeeService.findEmployeesBySalaryRange(employeeCriteriaDTO.getSalaryFrom(), employeeCriteriaDTO.getSalaryTo())).thenReturn(employeesDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/salaryRange", baseUrl))
                .param("salaryFrom", employeeCriteriaDTO.getSalaryFrom().toString())
                .param("salaryTo", employeeCriteriaDTO.getSalaryTo().toString()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));;

        verify(employeeService, times(1)).findEmployeesBySalaryRange(employeeCriteriaDTO.getSalaryFrom(), employeeCriteriaDTO.getSalaryTo());
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    public void testGetEmployeesByPositionAndSalaryGreaterThan() throws Exception {
        when(employeeService.findEmployeesByPositionAndSalaryGreaterThan(employeeDTO.getPosition(), employeeDTO.getSalary())).thenReturn(employeesDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/positionAndSalaryGreaterThan", baseUrl))
                .param("position", employeeDTO.getPosition())
                .param("salary", employeeDTO.getSalary().toString()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));;

        verify(employeeService, times(1)).findEmployeesByPositionAndSalaryGreaterThan(employeeDTO.getPosition(), employeeDTO.getSalary());
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    public void testGetEmployeesByPositionAndSalaryLessThan() throws Exception {
        when(employeeService.findEmployeesByPositionAndSalaryLessThan(employeeDTO.getPosition(), employeeDTO.getSalary())).thenReturn(employeesDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/positionAndSalaryLessThan", baseUrl))
                        .param("position", employeeDTO.getPosition())
                        .param("salary", employeeDTO.getSalary().toString()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));;

        verify(employeeService, times(1)).findEmployeesByPositionAndSalaryLessThan(employeeDTO.getPosition(), employeeDTO.getSalary());
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    public void testGetEmployeeWithHighestSalary() throws Exception {
        when(employeeService.findEmployeeByHighestSalary()).thenReturn(employeeDTO);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/highestSalary", baseUrl)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(employeeDTO.getId()));

        verify(employeeService, times(1)).findEmployeeByHighestSalary();
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    public void testGetEmployeeWithLowestSalary() throws Exception {
        when(employeeService.findEmployeeByLowestSalary()).thenReturn(employeeDTO);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("%s/lowestSalary", baseUrl)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(employeeDTO.getId()));

        verify(employeeService, times(1)).findEmployeeByLowestSalary();
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    public void testCreateEmployee() throws Exception {
        when(employeeService.create(any(EmployeeDTO.class))).thenReturn(employeeDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(employeeDTO.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(employeeDTO.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(employeeDTO.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.position").value(employeeDTO.getPosition()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.department").value(employeeDTO.getDepartment()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(employeeDTO.getSalary()));

        verify(employeeService, times(1)).create(employeeDTO);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    public void testUpdateEmployee() throws Exception {
        EmployeeDTO updatedEmployeeDTO = new EmployeeDTO(0L, "Name", "Surname", "HR", "email@gmail.com",
                employeeDTO.getSalary(), "Dep C");
        when(employeeService.update(anyLong(), any(EmployeeDTO.class))).thenReturn(updatedEmployeeDTO);

        mockMvc.perform(MockMvcRequestBuilders.put(String.format("%s/{id}", baseUrl), employeeDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployeeDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(updatedEmployeeDTO.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(updatedEmployeeDTO.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(updatedEmployeeDTO.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.position").value(updatedEmployeeDTO.getPosition()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.department").value(updatedEmployeeDTO.getDepartment()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(updatedEmployeeDTO.getSalary()));

        verify(employeeService, times(1)).update(anyLong(), any(EmployeeDTO.class));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    public void testDeleteEmployee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(String.format("%s/{id}", baseUrl), employeeDTO.getId()))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).delete(employeeDTO.getId());
    }

}