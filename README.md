# Employee Management System API

A comprehensive RESTful API for managing employees and organizational hierarchy built with Spring Boot. This system supports complex manager-employee relationships, department management, and advanced search capabilities.

## Features

### Core Functionality
- ✅ **Complete CRUD Operations** - Create, Read, Update, Delete employees
- ✅ **Manager-Employee Hierarchy** - Self-referencing relationship with validation
- ✅ **Advanced Search** - Multi-criteria search by name, department, salary, and role
- ✅ **Department Management** - Filter and organize by departments
- ✅ **Pagination Support** - Handle large datasets efficiently
- ✅ **Validation** - Comprehensive input validation and business rule enforcement

### Advanced Features
- ✅ **Circular Reference Prevention** - Validates manager assignments to prevent cycles
- ✅ **Manager Operations** - Promote/demote employees with subordinate checks
- ✅ **Employee Transfer** - Move employees between managers
- ✅ **Hierarchy Visualization** - Get reporting chain and subordinates
- ✅ **Smart Manager Deletion** - Automatic subordinate reassignment
- ✅ **Exception Handling** - Centralized error handling with proper HTTP status codes

## Technology Stack

- **Framework**: Spring Boot 3.5.5
- **Language**: Java 21
- **Database**: SQLite (with H2/PostgreSQL support)
- **Security**: Spring Security
- **ORM**: Spring Data JPA / Hibernate
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito, Spring Test

## Prerequisites

- Java 21 or higher
- Maven 3.6+ (or use included Maven wrapper)

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd spring-user-account-management-api
```

### 2. Build the Project

**Using Maven Wrapper (Windows):**
```powershell
.\mvnw.cmd clean install
```

**Using Maven Wrapper (Linux/Mac):**
```bash
./mvnw clean install
```

**Using Maven:**
```bash
mvn clean install
```

### 3. Run the Application

**Using Maven Wrapper (Windows):**
```powershell
.\mvnw.cmd spring-boot:run
```

**Using Maven Wrapper (Linux/Mac):**
```bash
./mvnw spring-boot:run
```

**Using Maven:**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:9090`

### 4. Database

The application uses SQLite by default. The database file `store.db` will be created automatically in the project root directory.

## API Documentation

### Base URL
```
http://localhost:9090/api/v1/employees
```

### Authentication
The API uses Spring Security. Default credentials are generated at startup (check console output).

---

## API Endpoints

### Employee CRUD Operations

#### Get All Employees (Paginated)
```http
GET /api/v1/employees?page=0&size=20
```

**Query Parameters:**
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 20) - Page size

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "employeeName": "John Doe",
      "email": "john.doe@company.com",
      "dept": "Engineering",
      "salary": 75000.0,
      "isManager": true,
      "numSubordinates": 3,
      "manager": null,
      "createdDate": "2024-01-15T10:30:00",
      "updatedDate": "2024-01-15T10:30:00"
    }
  ],
  "pageable": {...},
  "totalElements": 50,
  "totalPages": 3
}
```

#### Get Employee by ID
```http
GET /api/v1/employees/id/{id}
```

**Response:** `200 OK` with employee details or `404 NOT FOUND`

#### Get Employee by Email
```http
GET /api/v1/employees/email/{email}
```

**Response:** `200 OK` with employee details or `404 NOT FOUND`

#### Create New Employee
```http
POST /api/v1/employees
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Jane Smith",
  "email": "jane.smith@company.com",
  "dept": "Marketing",
  "salary": 65000.0,
  "managerId": 1,
  "isManager": false
}
```

**Response:** `201 CREATED` with created employee details

**Validation Rules:**
- `name` - Required, not blank
- `email` - Required, valid email format
- `dept` - Required
- `salary` - Optional, must be zero or positive
- `managerId` - Optional, must be a valid manager ID
- `isManager` - Optional, defaults to false

#### Update Employee
```http
PUT /api/v1/employees/{id}
Content-Type: application/json
```

**Request Body:** Same as create

**Response:** `200 OK` with updated employee or `404 NOT FOUND`

#### Delete Employee
```http
DELETE /api/v1/employees/{id}
```

**Response:** `204 NO CONTENT` or `400 BAD REQUEST` if employee is a manager

**Note:** Use the manager deletion endpoint for managers.

#### Delete Manager
```http
DELETE /api/v1/employees/manager/{id}
```

**Response:** `204 NO CONTENT`

**Behavior:** Automatically reassigns subordinates to another manager with the lowest subordinate count.

---

### Search & Filter Operations

#### Multi-Criteria Search
```http
GET /api/v1/employees/search?name=john&dept=Engineering&minSalary=50000&maxSalary=100000&isManager=true
```

**Query Parameters (all optional):**
- `name` - Search by name (partial match, case-insensitive)
- `dept` - Filter by department (exact match)
- `minSalary` - Minimum salary
- `maxSalary` - Maximum salary
- `isManager` - Filter by manager status (true/false)

**Response:** `200 OK` with array of matching employees

#### Get Employees by Department
```http
GET /api/v1/employees/department/{dept}
```

**Response:** `200 OK` with array of employees in the department

#### Get All Departments
```http
GET /api/v1/employees/departments
```

**Response:**
```json
["Engineering", "Marketing", "Sales", "HR", "Finance"]
```

#### Get All Managers
```http
GET /api/v1/employees/managers
```

**Response:** `200 OK` with array of all managers

---

### Hierarchy Operations

#### Get Subordinates
```http
GET /api/v1/employees/manager/{id}/subordinates
```

**Response:** `200 OK` with array of direct reports

**Example Response:**
```json
[
  {
    "id": 5,
    "employeeName": "Alice Johnson",
    "email": "alice.j@company.com",
    "dept": "Engineering",
    "salary": 68000.0,
    "isManager": false,
    "numSubordinates": 0,
    "manager": {
      "id": 1,
      "name": "John Doe",
      "email": "john.doe@company.com",
      "dept": "Engineering"
    }
  }
]
```

#### Get Reporting Hierarchy
```http
GET /api/v1/employees/{id}/hierarchy
```

**Response:** `200 OK` with array representing the chain of command from employee to top-level manager

**Example Response:**
```json
[
  {
    "id": 5,
    "employeeName": "Alice Johnson",
    "manager": {...}
  },
  {
    "id": 1,
    "employeeName": "John Doe",
    "manager": {...}
  },
  {
    "id": 100,
    "employeeName": "CEO",
    "manager": null
  }
]
```

---

### Management Operations

#### Promote Employee to Manager
```http
POST /api/v1/employees/{id}/promote
```

**Response:** `200 OK` with updated employee

**Requirements:**
- Employee must not already be a manager

#### Demote Manager to Employee
```http
POST /api/v1/employees/{id}/demote
```

**Response:** `200 OK` with updated employee or `400 BAD REQUEST` if manager has subordinates

**Requirements:**
- Manager must not have any subordinates
- Subordinates must be reassigned first

#### Transfer Employee to Different Manager
```http
PUT /api/v1/employees/{id}/transfer?newManagerId={managerId}
```

**Query Parameters:**
- `newManagerId` - ID of the new manager (optional, null removes manager)

**Response:** `200 OK` with updated employee

**Validation:**
- New manager must exist and be designated as a manager
- Cannot create circular references

---

## Error Responses

All error responses follow this format:

```json
{
  "status": 400,
  "message": "Circular manager reference detected: Employee 5 cannot have manager 10 (creates a cycle in the hierarchy)",
  "timestamp": "2024-01-15T10:30:00",
  "path": "uri=/api/v1/employees/5"
}
```

### Validation Error Response

```json
{
  "status": 400,
  "message": "Validation failed",
  "timestamp": "2024-01-15T10:30:00",
  "path": "uri=/api/v1/employees",
  "fieldErrors": {
    "email": "Email must be valid",
    "name": "Employee name is required"
  }
}
```

### HTTP Status Codes

- `200 OK` - Successful GET/PUT request
- `201 CREATED` - Successful POST request
- `204 NO CONTENT` - Successful DELETE request
- `400 BAD REQUEST` - Validation error or business rule violation
- `404 NOT FOUND` - Resource not found
- `500 INTERNAL SERVER ERROR` - Unexpected server error

---

## Business Rules

### Manager Assignment
1. Only employees with `isManager=true` can be assigned as managers
2. No circular references allowed (A manages B, B cannot manage A directly or indirectly)
3. Employee cannot be their own manager
4. Manager must exist in the database

### Manager Deletion
1. When deleting a manager, subordinates are automatically reassigned
2. Replacement manager is chosen based on lowest subordinate count
3. If no other managers exist, subordinates are assigned to null (no manager)

### Promotion/Demotion
1. Cannot promote an employee who is already a manager
2. Cannot demote a manager who has subordinates
3. Must reassign subordinates before demotion

### Data Validation
1. Email must be valid format and unique
2. Employee name is required
3. Department is required
4. Salary must be zero or positive (if provided)

---

## Project Structure

```
src/
├── main/
│   ├── java/com/archtech/store/
│   │   ├── config/           # Configuration classes
│   │   ├── controller/       # REST controllers
│   │   │   └── EmployeeController.java
│   │   ├── dto/              # Data Transfer Objects
│   │   │   ├── EmployeeRequest.java
│   │   │   ├── EmployeeResponse.java
│   │   │   └── ManagerSummary.java
│   │   ├── exception/        # Custom exceptions & handlers
│   │   │   ├── EmployeeNotFoundException.java
│   │   │   ├── CircularManagerReferenceException.java
│   │   │   ├── InvalidManagerAssignmentException.java
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── model/            # JPA entities
│   │   │   └── Employee.java
│   │   ├── repository/       # Data access layer
│   │   │   └── EmployeeRepository.java
│   │   ├── services/         # Business logic
│   │   │   ├── EmployeeService.java
│   │   │   └── EmployeeValidationService.java
│   │   └── StoreApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/archtech/store/
        ├── EmployeeServiceTest.java
        ├── EmployeeControllerTest.java
        └── EmployeeRepositoryTest.java
```

---

## Testing

### Run All Tests

```bash
.\mvnw.cmd test
```

### Run Specific Test Class

```bash
.\mvnw.cmd test "-Dtest=EmployeeServiceTest"
```

### Test Coverage

- **EmployeeServiceTest**: Unit tests for business logic
- **EmployeeControllerTest**: Integration tests for REST endpoints
- **EmployeeRepositoryTest**: Data access layer tests

---

## Configuration

### Application Properties

`src/main/resources/application.properties`

```properties
# Application name
spring.application.name=store

# Server port
server.port=9090

# Database configuration (SQLite)
spring.datasource.url=jdbc:sqlite:store.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update

# JPA logging (optional)
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Changing Database

#### PostgreSQL
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/employee_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

#### MySQL
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/employee_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

---

## Development

### Code Style
- Follow Java naming conventions
- Use meaningful variable and method names
- Add JavaDoc comments for public methods
- Keep methods focused and single-purpose

### Adding New Features

1. **Model Changes**: Update `Employee.java` entity
2. **Repository**: Add query methods to `EmployeeRepository.java`
3. **Service**: Implement business logic in `EmployeeService.java`
4. **Controller**: Add REST endpoints in `EmployeeController.java`
5. **Tests**: Write unit and integration tests
6. **Documentation**: Update this README

---

## Troubleshooting

### Port Already in Use
If port 9090 is already in use, change it in `application.properties`:
```properties
server.port=8080
```

### Database Lock Issues
SQLite database locks can occur with concurrent access. Consider using PostgreSQL or MySQL for production.

### Authentication Issues
Check console output for generated security password:
```
Using generated security password: abc123-def456-...
```

Use username `user` and the generated password for API calls.

---

## Future Enhancements

- [ ] OpenAPI/Swagger documentation
- [ ] Role-based access control (RBAC)
- [ ] Employee performance reviews
- [ ] Salary history tracking
- [ ] Bulk operations (import/export)
- [ ] Email notifications
- [ ] Audit logging
- [ ] Department hierarchy
- [ ] Employee skills and certifications

---

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## Contact

For questions or support, please open an issue in the repository.

---

## Acknowledgments

- Spring Boot framework and community
- Hibernate ORM
- SQLite database
- Maven build tool

---

**Built with ❤️ using Spring Boot**
