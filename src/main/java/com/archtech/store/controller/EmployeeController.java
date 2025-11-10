package com.archtech.store.controller;

import com.archtech.store.model.*;
import com.archtech.store.services.*;
import com.archtech.store.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;


@RestController
@CrossOrigin(origins = {"http://localhost:5501", "/**"})
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    
}
