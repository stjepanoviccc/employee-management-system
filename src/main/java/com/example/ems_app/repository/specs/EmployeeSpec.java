package com.example.ems_app.repository.specs;

import com.example.ems_app.model.Employee;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public interface EmployeeSpec {

    static Specification<Employee> findEmployeesWithAllOptionalFields (
            String firstName, String lastName, String email,
            String position, BigDecimal salaryFrom, BigDecimal salaryTo, String department
    ) {
        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(!firstName.isEmpty()) {
                predicates.add(cb.equal(root.get("firstName"), firstName));
            }

            if(!lastName.isEmpty()) {
                predicates.add(cb.equal(root.get("lastName"), lastName));
            }

            if(!email.isEmpty()) {
                predicates.add(cb.equal(root.get("email"), email));
            }

            if(!position.isEmpty()) {
                predicates.add(cb.equal(root.get("position"), position));
            }

            if (salaryFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("salary"), salaryFrom));
            }

            if (salaryTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("salary"), salaryTo));
            }

            if (!department.isEmpty()) {
                predicates.add(cb.equal(root.get("department"), department));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }

    static Specification<Employee> findEmployeesByPositionsAndDepartmentsAndSalaryRangeWithOptionalFields (
            List<String> positions, List<String> departments, BigDecimal salaryFrom, BigDecimal salaryTo
            ) {
        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!positions.isEmpty()) {
                List<Predicate> positionsPredicates = new ArrayList<>();
                for(String position : positions) {
                    positionsPredicates.add(cb.equal(root.get("position"), position));
                }
                predicates.add(cb.or(positionsPredicates.toArray(new Predicate[0])));
            }

            if (!departments.isEmpty()) {
                List<Predicate> departmentsPredicates = new ArrayList<>();
                for(String department : departments) {
                    departmentsPredicates.add(cb.equal(root.get("department"), department));
                }
                predicates.add(cb.or(departmentsPredicates.toArray(new Predicate[0])));
            }

            if (salaryFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("salary"), salaryFrom));
            }

            if (salaryTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("salary"), salaryTo));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }
}