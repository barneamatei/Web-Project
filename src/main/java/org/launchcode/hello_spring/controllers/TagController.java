package org.launchcode.hello_spring.controllers;

import jakarta.validation.Valid;
import org.launchcode.hello_spring.data.TagRepository;
import org.launchcode.hello_spring.models.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("tags")
public class TagController {

    @Autowired
    private TagRepository tagRepository;

    @GetMapping
    public String displayAllTags(Model model) {
        model.addAttribute("title", "All Tags");
        model.addAttribute("tags", tagRepository.findAll());
        return "tags/index";
    }

    @GetMapping("create")
    public String renderCreateTagForm(Model model) {
        model.addAttribute("title", "Create Tag");
        model.addAttribute(new Tag());
        return "tags/create";
    }

    @PostMapping("create")
    public String createTag(@ModelAttribute @Valid Tag newTag, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Create Tag");
            return "tags/create";
        }
        tagRepository.save(newTag);
        return "redirect:/tags";
    }

    @GetMapping("delete")
    public String renderDeleteTagForm(Model model) {
        model.addAttribute("title", "Delete Tags");
        model.addAttribute("tags", tagRepository.findAll());
        return "tags/delete";
    }

    @PostMapping("delete")
    public String processDeleteTagForm(@RequestParam(required = false) int[] tagIds) {
        if (tagIds != null) {
            for (int id : tagIds) {
                tagRepository.deleteById(id);
            }
        }
        return "redirect:/tags";
    }
}
