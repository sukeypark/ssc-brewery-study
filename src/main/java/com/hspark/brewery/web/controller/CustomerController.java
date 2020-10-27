package com.hspark.brewery.web.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hspark.brewery.domain.Customer;
import com.hspark.brewery.repositories.CustomerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/customers")
@Controller
public class CustomerController {
	
	// TODO: add service
	private final CustomerRepository customerRepository;
	
	@PreAuthorize("hasAuthority('customer.read')")
	@RequestMapping("/find")
	public String findCustomers(Model model) {
		model.addAttribute("customer", Customer.builder().build());
		return "customers/findCustomers";
	}
	
	@PreAuthorize("hasAuthority('customer.read')")
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
	
	@PreAuthorize("hasAuthority('customer.read')")
	@GetMapping("/{customerId}")
	public ModelAndView showCustomer(@PathVariable UUID customerId) {
		ModelAndView mav = new ModelAndView("customers/customerDetails");
		//TODO: add service
		mav.addObject(customerRepository.findById(customerId).get());
		return mav;
	}
	
	@PreAuthorize("hasAuthority('customer.create')")
	@GetMapping("/new")
	public String initCreationFrom(Model model) {
		model.addAttribute("customer", Customer.builder().build());
		return "customers/createCustomer";
	}
	
	@PreAuthorize("hasAuthority('customer.create')")
	@PostMapping("/new")
	public String processCreationForm(Customer customer) {
		Customer newCustomer = Customer.builder()
				.customerName(customer.getCustomerName())
				.build();
		
		Customer savedCustomer = customerRepository.save(newCustomer);
		return "redirect:/customers/" + savedCustomer.getId();
	}
	
	@PreAuthorize("hasAuthority('customer.update')")
	@GetMapping("/{customerId}/edit")
	public String initUpdateCustomerForm(@PathVariable UUID customerId, Model model) {
		if(customerRepository.findById(customerId).isPresent()) {
			model.addAttribute("customer", customerRepository.findById(customerId).get());
		}
		
		return "customers/createOrUpdateCustomer";
	}
	
	@PreAuthorize("hasAuthority('customer.update')")
	@PostMapping("/{beerId}/edit")
	public String processUpdateForm(@Valid Customer customer, BindingResult result) {
		if (result.hasErrors()) {
			return "beers/createOrUpdateCustomer";
		} else {
			// TODO: add service
			Customer savedCustomer = customerRepository.save(customer);
			return "redirect:/customers/" + savedCustomer.getId();
		}
	}

}
