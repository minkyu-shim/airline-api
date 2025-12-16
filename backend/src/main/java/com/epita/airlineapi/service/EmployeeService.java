package com.epita.airlineapi.service;

import com.epita.airlineapi.model.Employee;
import com.epita.airlineapi.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // GET ALL
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    // GET ONE (By Employee Number)
    public Employee getEmployeeByNumber(Long employeeNumber) {
        return employeeRepository.findByEmployeeNumber(employeeNumber)
                .orElseThrow(() -> new NoSuchElementException(
                        "Employee with number " + employeeNumber + " not found"
                ));
    }

    // CREATE
    public Employee createEmployee(Employee employee) {
        // 1.Check Employee Number Uniqueness
        if (employeeRepository.existsByEmployeeNumber(employee.getEmployeeNumber())) {
            throw new IllegalStateException("Employee number " + employee.getEmployeeNumber() + " already exists");
        }

        // 2.Check Email Uniqueness (Inherited from User)
        if (employee.getEmail() != null && employeeRepository.existsByEmail(employee.getEmail())) {
            throw new IllegalStateException("Email " + employee.getEmail() + " is already in use");
        }

        return employeeRepository.save(employee);
    }

    // UPDATE
    @Transactional
    public Employee updateEmployee(Long oldEmployeeNumber, Employee newDetails) {
        // 1.Retrieve existing employee
        Employee existingEmployee = getEmployeeByNumber(oldEmployeeNumber);

        // 2.Update Employee Number
        Long newNumber = newDetails.getEmployeeNumber();
        if (newNumber != null && !newNumber.equals(oldEmployeeNumber)) {
            if (employeeRepository.existsByEmployeeNumber(newNumber)) {
                throw new IllegalStateException("Employee number " + newNumber + " already taken");
            }
            existingEmployee.setEmployeeNumber(newNumber);
        }

        // 3.Update Email (Inherited)
        if (newDetails.getEmail() != null && !newDetails.getEmail().equals(existingEmployee.getEmail())) {
            if (employeeRepository.existsByEmail(newDetails.getEmail())) {
                throw new IllegalStateException("Email " + newDetails.getEmail() + " already in use");
            }
            existingEmployee.setEmail(newDetails.getEmail());
        }

        // 4.Update specific Employee fields
        if (newDetails.getProfession() != null) existingEmployee.setProfession(newDetails.getProfession());
        if (newDetails.getTitle() != null) existingEmployee.setTitle(newDetails.getTitle());

        // 5.Update inherited User fields
        if (newDetails.getFirstName() != null) existingEmployee.setFirstName(newDetails.getFirstName());
        if (newDetails.getLastName() != null) existingEmployee.setLastName(newDetails.getLastName());
        if (newDetails.getAddress() != null) existingEmployee.setAddress(newDetails.getAddress());
        if (newDetails.getPhoneNumber() != null) existingEmployee.setPhoneNumber(newDetails.getPhoneNumber());
        if (newDetails.getBirthDate() != null) existingEmployee.setBirthDate(newDetails.getBirthDate());

        return existingEmployee;
    }

    // DELETE
    @Transactional // Required for deleteBy... derived query
    public void deleteEmployee(Long employeeNumber) {
        if (!employeeRepository.existsByEmployeeNumber(employeeNumber)) {
            throw new NoSuchElementException("Employee with number " + employeeNumber + " does not exist");
        }
        employeeRepository.deleteByEmployeeNumber(employeeNumber);
    }
}