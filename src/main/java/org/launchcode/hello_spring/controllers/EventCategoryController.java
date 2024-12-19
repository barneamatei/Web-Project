package org.launchcode.hello_spring.controllers;

import jakarta.validation.Valid;
import org.launchcode.hello_spring.data.EventCategoryRepository;
import org.launchcode.hello_spring.models.EventCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("eventCategories")
public class EventCategoryController {

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @GetMapping
    public String displayAllCategories(Model model) {
        model.addAttribute("title", "All Event Categories");
        model.addAttribute("categories", eventCategoryRepository.findAll());
        return "eventCategories/index";
    }

    @GetMapping("create")
    public String renderCreateCategoryForm(Model model) {
        model.addAttribute("title", "Create Event Category");
        model.addAttribute(new EventCategory());
        return "eventCategories/create";
    }

    @PostMapping("create")
    public String createCategory(@ModelAttribute @Valid EventCategory newCategory, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Create Event Category");
            return "eventCategories/create";
        }

        eventCategoryRepository.save(newCategory);
        return "redirect:/eventCategories";
    }

    @GetMapping("delete")
    public String renderDeleteCategoryForm(Model model) {
        model.addAttribute("title", "Delete Event Categories");
        model.addAttribute("categories", eventCategoryRepository.findAll());
        return "eventCategories/delete";
    }

    @PostMapping("delete")
    public String processDeleteCategoryForm(@RequestParam(required = false) int[] categoryIds) {
        if (categoryIds != null) {
            for (int id : categoryIds) {
                eventCategoryRepository.deleteById(id);
            }
        }
        return "redirect:/eventCategories";
    }
}
