package com.codeup.weywotspringblog.controllers;

import com.codeup.weywotspringblog.models.Coffee;
import com.codeup.weywotspringblog.models.Customer;
import com.codeup.weywotspringblog.models.Supplier;
import com.codeup.weywotspringblog.repositories.CoffeeRepository;
import com.codeup.weywotspringblog.repositories.CustomerRepository;
import com.codeup.weywotspringblog.repositories.SupplierRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/coffee")
public class CoffeeController {

    private final CoffeeRepository coffeeDao;
    private final SupplierRepository suppliersDao;

    private final CustomerRepository customerDao;

    public CoffeeController(CoffeeRepository coffeeDao, SupplierRepository suppliersDao, CustomerRepository customerDao){
        this.coffeeDao = coffeeDao;
        this.suppliersDao = suppliersDao;
        this.customerDao = customerDao;
    }

    @GetMapping
    public String coffee(){
        return "coffee";
    }

    @GetMapping("/{roast}")
    public String roast(@PathVariable String roast, Model model){
        Coffee selection = new Coffee(roast, "Cool Beans");
        Coffee selection2 = new Coffee(roast, "Coffee Bros");
        selection.setOrigin("Ethiopia");
        selection2.setOrigin("Vietnam");
        List<Coffee> selections = new ArrayList<>(List.of(selection, selection2));
        model.addAttribute("selections", selections);
        return "coffee";
    }

    @GetMapping("/all-coffees")
    public String allCoffees(Model model){
        List<Coffee> coffees = coffeeDao.findAll();
        model.addAttribute("coffees", coffees);
        return "all-coffees";
    }

    @GetMapping("/new")
    public String addCoffeeForm(Model model){
        List<Supplier> suppliers = suppliersDao.findAll();
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("coffee", new Coffee());
        return "create-coffee";
    }

    @PostMapping("/new")
    public String addCoffee(@ModelAttribute Coffee coffee){
        coffeeDao.save(coffee);
        return "redirect:/coffee/all-coffees";
    }

    @PostMapping
    public String signUp(@RequestParam(name="email") String email, Model model){
        model.addAttribute("email", email);
        return "coffee";
    }

    @GetMapping("/suppliers")
    public String showSuppliersForm(Model model){
        List<Supplier> suppliers = suppliersDao.findAll();
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("supplier", new Supplier());
        return "suppliers";
    }

    @PostMapping("/suppliers")
    public String insertSupplier(@ModelAttribute Supplier supplier){
        suppliersDao.save(supplier);
        return "redirect:/coffee/suppliers";
    }


    @GetMapping("/customer-registration")
    public String showRegistrationForm(Model model){
        model.addAttribute("customer", new Customer());
        return "customer-registration";
    }

//    @PostMapping("/customer/new")
//     public String registerCustomer(@RequestParam(name = "name") String name, @RequestParam(name = "email") String email){
//        customerDao.save(new Customer(name, email));
//        return "redirect:/coffee";
//    }


    @PostMapping("/customer/new")
    public String registerCustomer(@ModelAttribute Customer customer){
        customerDao.save(customer);
        return "redirect:/coffee";
    }


    @PostMapping("/customer/{customerId}/favorite/{coffeeId}")
    public String favoriteCoffee(@PathVariable long customerId, @PathVariable long coffeeId){
        Customer customer = customerDao.findById(customerId);
        List<Coffee> favorites = customer.getCoffeeList();
        favorites.add(coffeeDao.findById(coffeeId));
        customer.setCoffeeList(favorites);
        customerDao.save(customer);
        return "redirect:/coffee";
    }




}//End of CoffeeController
