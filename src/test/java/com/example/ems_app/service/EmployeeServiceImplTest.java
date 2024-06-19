package com.example.ems_app.service;

import com.example.ems_app.dto.EmployeeCriteriaDTO;
import com.example.ems_app.dto.EmployeeDTO;
import com.example.ems_app.exception.BadRequestException;
import com.example.ems_app.exception.NotFoundException;
import com.example.ems_app.model.Employee;
import com.example.ems_app.repository.EmployeeRepository;
import com.example.ems_app.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.ems_app.dto.EmployeeDTO.convertToDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {


    private final Employee employee = createEmployee(1L);
    private final List<Employee> employeesList = Arrays.asList(createEmployee(1L), createEmployee(2L));
    private final BigDecimal salaryFrom = BigDecimal.valueOf(5000);
    private final BigDecimal salaryTo = BigDecimal.valueOf(20000);
    private final EmployeeCriteriaDTO employeeCriteriaDTO = new EmployeeCriteriaDTO(
            employee.getFirstName(), employee.getLastName(), employee.getEmail(), employee.getPosition(),
            salaryFrom, salaryTo, employee.getDepartment()
    );

    private Employee createEmployee(Long id) {
        return Employee.builder()
                .id(id)
                .firstName("Andrej")
                .lastName("Stjepanovic")
                .email("andrej@gmail.com")
                .salary(BigDecimal.valueOf(10000))
                .position("Software Developer")
                .department("Dep A")
                .build();
    }

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeProducerService employeeProducerService;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void shouldReturnAllEmployees_whenFindAll() {
        when(employeeRepository.findAll()).thenReturn(employeesList);

        List<EmployeeDTO> employeesDTOList = employeeService.findAll();

        assertEmployeesList(employeesDTOList, employeesList);

        verify(employeeRepository).findAll();
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void shouldReturnEmployees_whenFindAllByOptionalFields() {
        when(employeeRepository.findAll(any(Specification.class))).thenReturn(employeesList);

        List<EmployeeDTO> employeesDTOList = employeeService.findAllWithOptionalFields(employeeCriteriaDTO);

        assertEmployeesList(employeesDTOList, employeesList);

        verify(employeeRepository).findAll(any(Specification.class));
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void shouldReturnEmployees_whenFindAllByPositionsAndDepartmentsAndSalaryRangeWithOptionalFields() {
        when(employeeRepository.findAll(any(Specification.class))).thenReturn(employeesList);

        List<EmployeeDTO> employeesDTOList = employeeService.findAllByPositionsAndDepartmentsAndSalaryRangeWithOptionalFields(
                "Software Developer, HR", "Dep A, Dep B",
                BigDecimal.valueOf(5000), BigDecimal.valueOf(20000));

        assertEmployeesList(employeesDTOList, employeesList);

        verify(employeeRepository).findAll(any(Specification.class));
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void shouldReturnEmployee_whenFindById_ifEmployeeExists() {
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        EmployeeDTO employeeDTO = employeeService.findById(employee.getId());

        assertEmployees(employeeDTO, employee);

        verify(employeeRepository).findById(employee.getId());
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenFindById_ifEmployeeDoesNotExist() {
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> employeeService.findById(employee.getId()));

        assertEquals(String.format("Employee with id %s not found", employee.getId()), exception.getMessage());

        verify(employeeRepository).findById(employee.getId());
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void shouldReturnEmployee_whenFindByEmail_ifEmployeeExists() {
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));

        EmployeeDTO employeeDTO = employeeService.findByEmail(employee.getEmail());

        assertEmployees(employeeDTO, employee);

        verify(employeeRepository).findByEmail(employee.getEmail());
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenFindByEmail_ifEmployeeDoesNotExist() {
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> employeeService.findByEmail(employee.getEmail()));

        assertEquals(String.format("Employee with email %s not found", employee.getEmail()), exception.getMessage());

        verify(employeeRepository).findByEmail(employee.getEmail());
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void shouldReturnEmployees_whenFindEmployeesWithSalaryGreaterThan() {
        when(employeeRepository.findEmployeesWithSalaryGreaterThan(salaryFrom)).thenReturn(employeesList);

        List<EmployeeDTO> employeesDTOList = employeeService.findEmployeesWithSalaryGreaterThan(salaryFrom);

        assertEmployeesList(employeesDTOList, employeesList);

        verify(employeeRepository).findEmployeesWithSalaryGreaterThan(salaryFrom);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void shouldReturnEmployees_whenFindEmployeesWithSalaryLessThan() {
        when(employeeRepository.findEmployeesWithSalaryLessThan(salaryTo)).thenReturn(employeesList);

        List<EmployeeDTO> employeesDTOList = employeeService.findEmployeesWithSalaryLessThan(salaryTo);

        assertEmployeesList(employeesDTOList, employeesList);

        verify(employeeRepository).findEmployeesWithSalaryLessThan(salaryTo);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void shouldReturnEmployees_whenFindEmployeesBySalaryRange() {
        when(employeeRepository.findEmployeesBySalaryRange(salaryFrom, salaryTo)).thenReturn(employeesList);

        List<EmployeeDTO> employeesDTOList = employeeService.findEmployeesBySalaryRange(salaryFrom, salaryTo);

        assertEmployeesList(employeesDTOList, employeesList);

        verify(employeeRepository).findEmployeesBySalaryRange(salaryFrom, salaryTo);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void shouldReturnEmployees_whenFindEmployeesByPositionAndSalaryGreaterThan() {
        when(employeeRepository.findEmployeesByPositionAndSalaryGreaterThan(employee.getPosition(), salaryFrom)).thenReturn(employeesList);

        List<EmployeeDTO> employeesDTOList = employeeService.findEmployeesByPositionAndSalaryGreaterThan(employee.getPosition(), salaryFrom);

        assertEmployeesList(employeesDTOList, employeesList);

        verify(employeeRepository).findEmployeesByPositionAndSalaryGreaterThan(employee.getPosition(), salaryFrom);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void shouldReturnEmployees_whenFindEmployeesByPositionAndSalaryLessThan() {
        when(employeeRepository.findEmployeesByPositionAndSalaryLessThan(employee.getPosition(), salaryTo)).thenReturn(employeesList);

        List<EmployeeDTO> employeesDTOList = employeeService.findEmployeesByPositionAndSalaryLessThan(employee.getPosition(), salaryTo);

        assertEmployeesList(employeesDTOList, employeesList);

        verify(employeeRepository).findEmployeesByPositionAndSalaryLessThan(employee.getPosition(), salaryTo);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void shouldReturnEmployee_whenFindEmployeeByHighestSalary_ifEmployeeExists() {
        when(employeeRepository.findEmployeeByHighestSalary()).thenReturn(Optional.of(employee));

        EmployeeDTO employeeDTO = employeeService.findEmployeeByHighestSalary();

        assertEmployees(employeeDTO, employee);

        verify(employeeRepository).findEmployeeByHighestSalary();
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenFindEmployeeByHighestSalary_ifEmployeeDoesNotExist() {
        when(employeeRepository.findEmployeeByHighestSalary()).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> employeeService.findEmployeeByHighestSalary());

        assertEquals("Employee with highest salary not found", exception.getMessage());

        verify(employeeRepository).findEmployeeByHighestSalary();
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void shouldReturnEmployee_whenFindEmployeeByLowestSalary_ifEmployeeExists() {
        when(employeeRepository.findEmployeeByLowestSalary()).thenReturn(Optional.of(employee));

        EmployeeDTO employeeDTO = employeeService.findEmployeeByLowestSalary();

        assertEmployees(employeeDTO, employee);

        verify(employeeRepository).findEmployeeByLowestSalary();
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenFindEmployeeByLowestSalary_ifEmployeeDoesNotExist() {
        when(employeeRepository.findEmployeeByLowestSalary()).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> employeeService.findEmployeeByLowestSalary());

        assertEquals("Employee with lowest salary not found", exception.getMessage());

        verify(employeeRepository).findEmployeeByLowestSalary();
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void shouldCreateEmployee_whenCreateEmployee_ifEmployeeDoesNotExist() {
        when(employeeRepository.existsByEmail(employee.getEmail())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDTO createdEmployeeDTO = employeeService.create(convertToDto(employee));

        assertEmployees(createdEmployeeDTO, employee);

        verify(employeeRepository).existsByEmail(employee.getEmail());
        verify(employeeRepository).save(any(Employee.class));
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void shouldThrowBadRequestException_whenCreateEmployee_ifEmployeeExists() {
        when(employeeRepository.existsByEmail(employee.getEmail())).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> employeeService.create(convertToDto(employee)));

        assertEquals("Email already exists", exception.getMessage());

        verify(employeeRepository).existsByEmail(employee.getEmail());
        verify(employeeRepository, never()).save(any(Employee.class));
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void shouldUpdateEmployee_whenUpdateEmployee_ifEmployeeExistsAndEmailIsNotTaken() {
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(employeeRepository.existsByEmail(employee.getEmail())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDTO updatedEmployeeDTO = employeeService.update(employee.getId(), convertToDto(employee));

        assertEmployees(updatedEmployeeDTO, employee);

        verify(employeeRepository).findById(employee.getId());
        verify(employeeRepository).existsByEmail(employee.getEmail());
        verify(employeeRepository).save(any(Employee.class));
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    public void shouldThrowBadRequestException_whenUpdateEmployee_ifEmployeeExistsAndEmailIsTaken() {
        Employee anotherEmployee = new Employee(900L, "A", "A", "A", "HR", salaryFrom, "Dep A");
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(employeeRepository.existsByEmail(employee.getEmail())).thenReturn(true);
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(anotherEmployee));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> employeeService.update(employee.getId(), convertToDto(employee)));

        assertEquals("The email you are trying to enter is already taken by another user.", exception.getMessage());

        verify(employeeRepository).findById(employee.getId());
        verify(employeeRepository).existsByEmail(employee.getEmail());
        verify(employeeRepository).findByEmail(employee.getEmail());
        verify(employeeRepository, never()).save(any(Employee.class));
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenUpdateEmployee_ifEmployeeDoesNotExist() {
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> employeeService.update(employee.getId(), convertToDto(employee)));

        assertEquals(String.format("Employee with id %s not found", employee.getId()), exception.getMessage());

        verify(employeeRepository).findById(employee.getId());
        verify(employeeRepository, never()).save(any(Employee.class));
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void shouldDeleteEmployee_whenDeleteEmployee_ifEmployeeExists() {
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        employeeService.delete(employee.getId());

        verify(employeeRepository).findById(employee.getId());
        verify(employeeRepository).deleteById(employee.getId());
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenDeleteEmployee_ifEmployeeDoesNotExist() {
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> employeeService.delete(employee.getId()));

        assertEquals(String.format("Employee with id %s not found", employee.getId()), exception.getMessage());

        verify(employeeRepository).findById(employee.getId());
        verify(employeeRepository, never()).deleteById(anyLong());
        verifyNoMoreInteractions(employeeRepository);
    }

    // helping
    void assertEmployees(EmployeeDTO employeeDTO, Employee employee) {
        assertNotNull(employeeDTO);
        assertEquals(employeeDTO.getId(), employee.getId());
        assertEquals(employeeDTO.getEmail(), employee.getEmail());
        assertEquals(employeeDTO.getFirstName(), employee.getFirstName());
        assertEquals(employeeDTO.getLastName(), employee.getLastName());
        assertEquals(employeeDTO.getPosition(), employee.getPosition());
        assertEquals(employeeDTO.getDepartment(), employee.getDepartment());
        assertEquals(employeeDTO.getSalary(), employee.getSalary());
    }

    void assertEmployeesList(List<EmployeeDTO> employeesDTOList, List<Employee> employeesList) {
        assertNotNull(employeesDTOList);
        assert(employeesDTOList.size() == employeesList.size());
    }
}
