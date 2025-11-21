package com.archtech.store;

import com.archtech.store.model.Employee;
import com.archtech.store.repository.*;
import com.archtech.store.services.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    private EmployeeRepository repository;
    private EmployeeValidationService validationService;
    private EmployeeService service;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(EmployeeRepository.class);
        validationService = Mockito.mock(EmployeeValidationService.class);
        service = new EmployeeService(repository, validationService);
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> fakeEmployees = List.of(
                new Employee("Alice", "alice@example.com", "HR", 50000.0, null, false),
                new Employee("Bob", "bob@example.com", "IT", 70000.0, null, true)
        );

        when(repository.findAll()).thenReturn(fakeEmployees);

        List<Employee> result = service.getAllEmployees();
        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void testGetEmployeeByEmail() {
        Employee emp = new Employee("Alice", "alice@example.com", "HR", 50000.0, null, false);

        when(repository.findByEmail("alice@example.com"))
                .thenReturn(Optional.of(emp));

        Optional<Employee> result = service.getEmployee("alice@example.com");

        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getEmployeeName());
        verify(repository).findByEmail("alice@example.com");
    }

    @Test
    void testCreateEmployee() {
        Employee emp = new Employee("Charlie", "charlie@example.com", "Sales", 60000.0, null, false);

        // Mock validation service to do nothing
        doNothing().when(validationService).validateManagerAssignment(anyLong(), any());
        
        when(repository.save(emp)).thenReturn(emp);

        Employee created = service.createEmployee(emp);

        assertEquals("Charlie", created.getEmployeeName());
        verify(repository).save(emp);
    }
}
