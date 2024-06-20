package com.example.ems_app.service.impl;

import com.example.ems_app.dto.EmployeeDTO;
import com.example.ems_app.service.EmployeeConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static com.example.ems_app.config.RabbitMQConfig.EMPLOYEE_QUEUE_NAME;

@Service
@RequiredArgsConstructor
public class EmployeeConsumerServiceImpl implements EmployeeConsumerService {

    private final Logger logger = Logger.getLogger(EmployeeConsumerServiceImpl.class.getName());

    @Override
    @RabbitListener(queues = EMPLOYEE_QUEUE_NAME)
    public void consumeMessage(EmployeeDTO employeeDTO) {
        logger.info("Received message: " + employeeDTO);
    }
}
