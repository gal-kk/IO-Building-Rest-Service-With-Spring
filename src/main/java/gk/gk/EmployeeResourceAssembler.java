package gk.gk;


import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@Component
public class EmployeeResourceAssembler implements ResourceAssembler<Employee, Resource<Employee>> {
    @Override
    public Resource<Employee> toResource(Employee employee) {
        return new Resource<>(employee, linkTo(methodOn(EmployeeController.class).getAll()).withRel("employees"),
                linkTo(methodOn(EmployeeController.class).GetSpecific(employee.getId())).withSelfRel());
    }
}
