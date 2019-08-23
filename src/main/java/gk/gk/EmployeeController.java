package gk.gk;

import gk.gk.Exception.EmployeeNotFoundException;
import gk.gk.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
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
    public ResponseEntity<?> addNewEmployee(@RequestBody Employee employee) throws URISyntaxException {
        Resource resource = employeeResourceAssembler.toResource(employeeRepository.save(employee));
        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
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
    public ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable long id) throws URISyntaxException {
        Employee employee =  employeeRepository.findById(id).map(i ->
                {i.setName(newEmployee.getName());
                i.setRole(newEmployee.getRole());
                return employeeRepository.save(i);
        })
                .orElseGet(() ->
                        {newEmployee.setId(id);
                        return employeeRepository.save(newEmployee);}
        );
        Resource resource = employeeResourceAssembler.toResource(employee);
        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);

    }

    @DeleteMapping("/employees/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable Long id) {

        employeeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
