package com.assn2.assignment2.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.assn2.assignment2.models.User;
import com.assn2.assignment2.models.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class UserController {
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/users/view")
    public String getAllUsers(Model model) {
        System.out.println("Getting all users");

        List<User> users = userRepo.findAll();
        model.addAttribute("us", users);
        return "users/rectangle";
    }

    @PostMapping("/users/add")
    public String addUser(@RequestParam Map<String, String> newuser, HttpServletResponse response){
        System.out.println("ADD user");
        String newName = newuser.get("name");
        String newColor = newuser.get("color");
        int newWidth = Integer.parseInt(newuser.get("width"));
        int newHeight = Integer.parseInt(newuser.get("height"));

        userRepo.save(new User(newName, newColor, newWidth, newHeight));
        response.setStatus(201);
        return "redirect:/users/view";
    }
    
    @GetMapping("/users/detail/{id}")
    public String getUserDetail(@PathVariable("id") int id, Model model) {
        System.out.println("Getting user details for ID: " + id);
    
        User user = userRepo.findById(id).orElse(null);
        if (user == null) {
            return "redirect:/users/view";
        }
    
        model.addAttribute("user", user);
        return "users/userInfo";
    }
    
    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable int id) {
        userRepo.deleteById(id);
        return "redirect:/users/view"; // Redirect to user list after deletion
    }
 
    @PostMapping("/users/edit/{id}")
    public String editUser(@PathVariable int id, @RequestParam Map<String, String> updatedUser) {
        System.out.println("Updating user details for ID: " + id);

        User user = userRepo.findById(id).orElse(null);
        if (user == null) {
            return "redirect:/users/view";
        }

        // Update user details
        user.setName(updatedUser.get("name"));
        user.setColor(updatedUser.get("color"));
        user.setWidth(Integer.parseInt(updatedUser.get("width")));
        user.setHeight(Integer.parseInt(updatedUser.get("height")));

        userRepo.save(user);
        return "redirect:/users/detail/" + id;
    }
}


