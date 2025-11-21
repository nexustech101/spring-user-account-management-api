package com.archtech.store.exception;

public class InvalidManagerAssignmentException extends RuntimeException {
    public InvalidManagerAssignmentException(String message) {
        super(message);
    }

    public InvalidManagerAssignmentException(Long managerId) {
        super("Invalid manager assignment: Employee " + managerId + 
              " is not designated as a manager (isManager=false)");
    }
}
