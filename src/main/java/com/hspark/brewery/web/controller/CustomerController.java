package com.hspark.brewery.web.controller;

import java.util.List;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hspark.brewery.domain.Customer;
import com.hspark.brewery.repositories.CustomerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/customers")
@Controller
public class CustomerController {
	
	private final CustomerRepository customerRepository;
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
	@GetMapping
	public String processFindFormReturnMany(Customer customer, BindingResult result, Model model) {
        // find customers by name
        //ToDO: Add Service
        List<Customer> customers = customerRepository.findAllByCustomerNameLike("%" + customer.getCustomerName() + "%");
        if (customers.isEmpty()) {
            // no customers found
            result.rejectValue("customerName", "notFound", "not found");
            return "customers/findCustomers";
        } else if (customers.size() == 1) {
            // 1 customer found
            customer = customers.get(0);
            return "redirect:/customers/" + customer.getId();
        } else {
            // multiple customers found
            model.addAttribute("selections", customers);
            return "customers/customerList";
        }
	}

}
