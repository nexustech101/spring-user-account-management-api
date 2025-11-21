package com.archtech.store;

import com.archtech.store.dto.EmployeeResponse;
import com.archtech.store.model.Employee;
import com.archtech.store.services.EmployeeService;
import com.archtech.store.controller.EmployeeController;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService service;

    @Test
    @WithMockUser
    void testGetAllEmployees() throws Exception {
        List<Employee> employees = List.of(
                new Employee("Alice", "alice@example.com", "HR", 50000.0, null, false),
                new Employee("Bob", "bob@example.com", "IT", 70000.0, null, true)
        );

        Page<Employee> page = new PageImpl<>(employees);
        when(service.getAllEmployeesPaged(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    @WithMockUser
    void testGetEmployeeById() throws Exception {
        Employee employee = new Employee("Alice", "alice@example.com", "HR", 50000.0, null, false);

        when(service.getEmployee(1L)).thenReturn(Optional.of(employee));

        mockMvc.perform(get("/api/v1/employees/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeName").value("Alice"));
    }

    @Test
    @WithMockUser
    void testGetEmployeeByIdNotFound() throws Exception {
        when(service.getEmployee(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/employees/id/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testCreateEmployee() throws Exception {
        Employee emp = new Employee("Charlie", "charlie@example.com", "Sales", 60000.0, null, false);

        when(service.createEmployee(any(Employee.class))).thenReturn(emp);

        String jsonBody = """
                {
                    "name": "Charlie",
                    "email": "charlie@example.com",
                    "dept": "Sales",
                    "salary": 60000.0,
                    "isManager": false
                }
                """;

        mockMvc.perform(post("/api/v1/employees")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeName").value("Charlie"));
    }

    @Test
    @WithMockUser
    void testUpdateEmployee() throws Exception {
        Employee updated = new Employee("Dan", "dan@example.com", "IT", 65000.0, null, true);

        when(service.updateEmployee(eq(1L), any(Employee.class))).thenReturn(updated);

        String jsonBody = """
                {
                    "name": "Dan",
                    "email": "dan@example.com",
                    "dept": "IT",
                    "salary": 65000.0,
                    "isManager": true
                }
                """;

        mockMvc.perform(put("/api/v1/employees/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeName").value("Dan"));
    }
}
