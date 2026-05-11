package com.springIntegration.services.models;

import com.springIntegration.services.enums.Department;
import lombok.Data;

@Data
public class Employee {
    private int employeeId;
    private String employeeName;
    private String employeeStatus;
    private Department employeeDepartment;
}