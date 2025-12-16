package com.epita.airlineapi.controller;

import com.epita.airlineapi.model.Employee;
import com.epita.airlineapi.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Employee>> getEmployees() {
        List<Employee> employees = employeeService.getEmployees();
        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(employees);
    }

    // GET ONE (Using Employee Number)
    @GetMapping("/{employeeNumber}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long employeeNumber) {
        // GlobalExceptionHandler catches NoSuchElementException -> 404
        return ResponseEntity.ok(employeeService.getEmployeeByNumber(employeeNumber));
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        // GlobalExceptionHandler catches IllegalStateException (Duplicate Number/Email) -> 400
        Employee createdEmployee = employeeService.createEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    // UPDATE
    @PutMapping("/{employeeNumber}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long employeeNumber,
            @RequestBody Employee employee) {

        // Returns the full updated JSON object
        Employee updatedEmployee = employeeService.updateEmployee(employeeNumber, employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    // DELETE
    @DeleteMapping("/{employeeNumber}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long employeeNumber) {
        employeeService.deleteEmployee(employeeNumber);
        return ResponseEntity.noContent().build();
    }
}