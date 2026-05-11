package com.springIntegration.services.mapper;


import com.springIntegration.services.dto.EmployeeDTO;
import com.springIntegration.services.models.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeMapper {
    EmployeeMapper MAPPER = Mappers.getMapper(EmployeeMapper.class);

    EmployeeDTO convertToEmployeeDTO(Employee employee);

    Employee converToEmployee(EmployeeDTO sourceCode);

}
