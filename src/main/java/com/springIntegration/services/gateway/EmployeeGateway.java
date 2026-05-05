package com.springIntegration.services.gateway;

import com.springIntegration.services.models.Employee;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway
public interface EmployeeGateway {

    /**
     * Service activator for the Get employee call
     * @param name
     * @return
     */
    @Gateway(requestChannel = "request-emp-name-channel")
    public String getEmployeeName(String name);

    /**
     * Post call for hire employee
     * @param employee
     * @return
     */
    @Gateway(requestChannel = "request-hire-emp-channel")
    public Message<Employee> hireEmployee(Employee employee);

    /**
     *
     * @param status
     * @return
     */
    @Gateway(requestChannel = "emp-status-channel")
    String processEmployeeStatus(String status);
}
