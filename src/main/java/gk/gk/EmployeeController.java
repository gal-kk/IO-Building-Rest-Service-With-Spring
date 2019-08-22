package gk.gk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("employees")
    public List<Employee> getAll(){
        return employeeRepository.findAll();
    }

    @PostMapping("employees")
    public Employee addNewEmployee(@RequestBody Employee employee){
        return employeeRepository.save(employee);
    }

    @GetMapping("/employees/{id}")
    public Resource<Employee> GetSpecific(@PathVariable long id){
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return new Resource<>(employee,
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder.methodOn(EmployeeController.class).GetSpecific(id)).withSelfRel(),
        ControllerLinkBuilder.linkTo(
                ControllerLinkBuilder.methodOn(EmployeeController.class).getAll()).withRel("employees"));
    }

    @PutMapping("/employees/{id}")
    public Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable long id){
        return employeeRepository.findById(id).map(employee ->
                {employee.setName(newEmployee.getName());
                employee.setRole(newEmployee.getRole());
                return employeeRepository.save(employee);
        })
                .orElseGet(() ->
                        {newEmployee.setId(id);
                        return employeeRepository.save(newEmployee);}
        );
    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id) {
        employeeRepository.deleteById(id);
    }
}
