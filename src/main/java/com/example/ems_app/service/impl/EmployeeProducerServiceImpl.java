package com.example.ems_app.service.impl;

import com.example.ems_app.dto.EmployeeDTO;
import com.example.ems_app.service.EmployeeProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import static com.example.ems_app.config.RabbitMQConfig.EMPLOYEE_QUEUE_NAME;

@Service
@RequiredArgsConstructor
public class EmployeeProducerServiceImpl implements EmployeeProducerService {

    private final AmqpTemplate rabbitTemplate;

    @Override
    public void produceMessage(EmployeeDTO employeeDTO) {
        rabbitTemplate.convertAndSend(EMPLOYEE_QUEUE_NAME, employeeDTO);
    }
}
