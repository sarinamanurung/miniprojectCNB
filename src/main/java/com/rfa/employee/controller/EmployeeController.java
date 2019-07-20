package com.rfa.employee.controller;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rfa.employee.domain.EmployeeDomain;
import com.rfa.employee.exception.DataNotFoundException;
import com.rfa.employee.repository.EmployeeRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1")
@Api(tags = "Employee")
public class EmployeeController {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@GetMapping("/emp")
	public List<EmployeeDomain> GetAllEmployees(){
		return employeeRepository.findAll();
	}
	
	@GetMapping("emp/{id}")
	public ResponseEntity<EmployeeDomain> GetEmployeeById(@PathVariable(value = "id") Long employeeId) throws DataNotFoundException{
		
		EmployeeDomain employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new DataNotFoundException("Employee not found for this id :: " + employeeId));
		
		return ResponseEntity.ok().body(employee);
	}
	
	@GetMapping("emp/{name}/{dob}")
	public ResponseEntity<List<EmployeeDomain>> GetEmployeeByName(@PathVariable(value = "name") String employeeName, @PathVariable(value = "dob") Date dob){
		
		EmployeeDomain employees = new EmployeeDomain();
		
		employees.setName(employeeName);
		employees.setDob(dob);
		
		Example<EmployeeDomain> employeesExample = Example.of(employees);
		
		List<EmployeeDomain> employeesReturn = employeeRepository.findAll(employeesExample);
		
		return ResponseEntity.ok().body(employeesReturn);
	}
	
	@PostMapping("/emp")
	public EmployeeDomain InsertEmployee(@Valid @RequestBody EmployeeDomain employee) {
		return employeeRepository.save(employee);
	}
	
	@PutMapping("/emp/{id}")
	public ResponseEntity<EmployeeDomain> UpdateEmployee(@Valid @RequestBody EmployeeDomain employeeRequest, @PathVariable(value = "id") Long employeeId) throws DataNotFoundException {
		
		 EmployeeDomain employee = employeeRepository.findById(employeeId)
			        .orElseThrow(() -> new DataNotFoundException("Employee not found for this id :: " + employeeId));
		
			employee.setAddress(employeeRequest.getAddress());
			employee.setAge(employeeRequest.getAge());
			employee.setName(employeeRequest.getName());
			employee.setDob(employeeRequest.getDob());
			employee.setPhone(employeeRequest.getPhone());
			employee.setSalary(employeeRequest.getSalary());
			employee.setId_div(employeeRequest.getId_div());
			
			final EmployeeDomain updateEmployee = employeeRepository.save(employee);

		
		return ResponseEntity.ok().body(updateEmployee);
	}
	
	@ApiOperation(
			value = " delet dat",
			notes = "Return data with JSON", 
			tags ="Data Manipulation API"
			)
    @DeleteMapping("/emp/{id}")
    public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId)
         throws DataNotFoundException {
        EmployeeDomain employee = employeeRepository.findById(employeeId)
       .orElseThrow(() -> new DataNotFoundException("Employee not found for this id :: " + employeeId));

        employeeRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
	
}
