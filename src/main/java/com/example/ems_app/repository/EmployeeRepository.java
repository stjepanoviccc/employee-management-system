package com.example.ems_app.repository;

import com.example.ems_app.model.Employee;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    Optional<Employee> findByEmail(String email);

    List<Employee> findAll(Specification<Employee> spec);

    @Query("SELECT e " +
            "FROM Employee e " +
            "WHERE e.salary = (SELECT MAX(e.salary) FROM Employee e)")
    Optional<Employee> findEmployeeByHighestSalary();

    @Query("SELECT e " +
            "FROM Employee e " +
            "WHERE e.salary = (SELECT MIN(e.salary) FROM Employee e)")
    Optional<Employee> findEmployeeByLowestSalary();

    @Query("SELECT e " +
            "FROM Employee e " +
            "WHERE e.salary > :salary")
    List<Employee> findEmployeesWithSalaryGreaterThan(BigDecimal salary);

    @Query("SELECT e " +
            "FROM Employee e " +
            "WHERE e.salary < :salary")
    List<Employee> findEmployeesWithSalaryLessThan(BigDecimal salary);

    @Query("SELECT e " +
            "FROM Employee e " +
            "WHERE e.salary BETWEEN :salaryFrom AND :salaryTo")
    List<Employee> findEmployeesBySalaryRange(BigDecimal salaryFrom, BigDecimal salaryTo);

    @Query("SELECT e " +
            "FROM Employee e " +
            "WHERE e.position = :position " +
            "AND e.salary > :salary")
    List<Employee> findEmployeesByPositionAndSalaryGreaterThan(String position, BigDecimal salary);

    @Query("SELECT e " +
            "FROM Employee e " +
            "WHERE e.position = :position " +
            "AND e.salary < :salary")
    List<Employee> findEmployeesByPositionAndSalaryLessThan(String position, BigDecimal salary);

}
