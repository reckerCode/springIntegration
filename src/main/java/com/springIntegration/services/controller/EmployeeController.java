package com.springIntegration.services.controller;

import com.springIntegration.services.gateway.EmployeeGateway;
import com.springIntegration.services.models.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/integrate")
public class EmployeeController {
    @Autowired
    public EmployeeGateway employeeGateway;

    /**
     * Here the below controller method called the gateway method and the message goes to the request channel of the
     * gateway to the input channel of the service and then finally return the message as the output.
     *
     * @param name
     * @return name
     */
    @GetMapping(value = "{name}")
    public String getEmployeeNameFromService(@PathVariable("name") String name) {
        return employeeGateway.getEmployeeName(name);
    }

    @PostMapping("/hireEmployee")
    public Employee HireEmployee(@RequestBody Employee employee) {
        Message<Employee> replyMessage = employeeGateway.hireEmployee(employee);
        return replyMessage.getPayload();
    }

    /**
     * A transformer takes a message from a channel and created a new message containing converted payload to message
     * structure.
     */

    @GetMapping(value = "/processEmployeeStatus/{status}")
    public String processEmployeeStatus (@PathVariable("status") String status) {
        return employeeGateway.processEmployeeStatus(status);
    }
}
