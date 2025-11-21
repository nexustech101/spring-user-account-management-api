package com.archtech.store.exception;

public class CircularManagerReferenceException extends RuntimeException {
    public CircularManagerReferenceException(String message) {
        super(message);
    }

    public CircularManagerReferenceException(Long employeeId, Long managerId) {
        super("Circular manager reference detected: Employee " + employeeId + 
              " cannot have manager " + managerId + " (creates a cycle in the hierarchy)");
    }
}
