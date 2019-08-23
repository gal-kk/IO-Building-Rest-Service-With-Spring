package gk.gk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeResourceAssembler employeeResourceAssembler;

    @GetMapping("employees")
    public Resources<Resource<Employee>> getAll(){
//        List<Resource<Employee>> employees = employeeRepository.findAll().stream()
//                .map(employee -> new Resource<>(employee,
//                        linkTo(methodOn(EmployeeController.class).GetSpecific(employee.getId())).withSelfRel(),
//                        linkTo(methodOn(EmployeeController.class).getAll()).withRel("employees")))
//                .collect(Collectors.toList());

//        List<Resource<Employee>> employees = employeeRepository.findAll().stream()
//                .map(i -> employeeResourceAssembler.toResource(i)).collect(Collectors.toList());

        List<Resource<Employee>> employees = employeeRepository.findAll().stream()
                .map(employeeResourceAssembler::toResource).collect(Collectors.toList());

        return new Resources<>(employees, linkTo(methodOn(EmployeeController.class).getAll()).withSelfRel());
    }

    @PostMapping("employees")
    public Employee addNewEmployee(@RequestBody Employee employee){
        return employeeRepository.save(employee);
    }

    @GetMapping("/employees/{id}")
    public Resource<Employee> GetSpecific(@PathVariable long id){
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

//        return new Resource<>(employee, linkTo(methodOn(EmployeeController.class).GetSpecific(id)).withSelfRel(),
//                                linkTo(methodOn(EmployeeController.class).getAll()).withRel("employees"));
        return employeeResourceAssembler.toResource(employee);
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
