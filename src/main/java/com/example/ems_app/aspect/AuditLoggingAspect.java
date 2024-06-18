package com.example.ems_app.aspect;

import com.example.ems_app.dto.EmployeeDTO;
import com.example.ems_app.model.AuditLog;
import com.example.ems_app.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditLoggingAspect {

    private static final Logger logger = LogManager.getLogger(AuditLoggingAspect.class);
    private final AuditLogRepository auditLogRepository;

    @AfterReturning(pointcut = "execution(* com.example.ems_app.service.EmployeeService.create(..)) && args(employeeDTO)",
            returning = "createdEmployeeDTO")
    public void logEmployeeCreationSuccess(EmployeeDTO employeeDTO, EmployeeDTO createdEmployeeDTO) {
        logAuditEntry("CREATE", employeeDTO.getId(), LocalDateTime.now(), true, null);
    }

    @AfterThrowing(pointcut = "execution(* com.example.ems_app.service.EmployeeService.create(..)) && args(employeeDTO)",
            throwing = "exception")
    public void logEmployeeCreationFailure(EmployeeDTO employeeDTO, Exception exception) {
        logAuditEntry("CREATE", employeeDTO.getId(), LocalDateTime.now(), false, exception.getMessage());
    }

    @AfterReturning(pointcut = "execution(* com.example.ems_app.service.EmployeeService.update(..)) && args(id, employeeDTO)",
            returning = "updatedEmployeeDTO", argNames = "id,employeeDTO,updatedEmployeeDTO")
    public void logEmployeeUpdateSuccess(Long id, EmployeeDTO employeeDTO, EmployeeDTO updatedEmployeeDTO) {
        logAuditEntry("UPDATE", id, LocalDateTime.now(), true, null);
    }

    @AfterThrowing(pointcut = "execution(* com.example.ems_app.service.EmployeeService.update(..)) && args(id, employeeDTO)",
            throwing = "exception", argNames = "id,employeeDTO,exception")
    public void logEmployeeUpdateFailure(Long id, EmployeeDTO employeeDTO, Exception exception) {
        logAuditEntry("UPDATE", id, LocalDateTime.now(), false, exception.getMessage());
    }

    @AfterReturning(pointcut = "execution(* com.example.ems_app.service.EmployeeService.delete(..)) && args(id)")
    public void logEmployeeDeletionSuccess(Long id) {
        logAuditEntry("DELETE", id, LocalDateTime.now(), true, null);
    }

    @AfterThrowing(pointcut = "execution(* com.example.ems_app.service.EmployeeService.delete(..)) && args(id)", throwing = "exception")
    public void logEmployeeDeletionFailure(Long id, Exception exception) {
        logAuditEntry("DELETE", id, LocalDateTime.now(), false, exception.getMessage());
    }

    private void logAuditEntry(String action, Long entityId, LocalDateTime timestamp, boolean success, String errorMessage) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        if (success) {
            logger.info(String.format("Action [%s] on Employee with id [%d] performed by User [%s] at [%s] was successful",
                    action, entityId, username, timestamp));
        } else {
            logger.error(String.format("Action [%s] on Employee with id [%d] performed by User [%s] at [%s] failed with error: %s",
                    action, entityId, username, timestamp, errorMessage));
        }

        AuditLog auditLog = new AuditLog();
        auditLog.setAction(action);
        auditLog.setEntity_id(entityId);
        auditLog.setUsername(username);
        auditLog.setCreated_at(timestamp);
        auditLog.setSuccess(success);
        auditLog.setErrorMessage(errorMessage);

        auditLogRepository.save(auditLog);
    }
}
