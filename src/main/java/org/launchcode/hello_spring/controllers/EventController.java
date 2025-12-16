package org.launchcode.hello_spring.controllers;

import jakarta.validation.Valid;
import org.launchcode.hello_spring.data.EventCategoryRepository;
import org.launchcode.hello_spring.data.EventRepository;
import org.launchcode.hello_spring.data.TagRepository;
import org.launchcode.hello_spring.models.Event;
import org.launchcode.hello_spring.models.EventCategory;
import org.launchcode.hello_spring.models.Tag;
import org.launchcode.hello_spring.models.dto.EventTagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @Autowired
    private TagRepository tagRepository;

    @GetMapping
    public String displayAllEvents(@RequestParam(required = false) Integer categoryId, Model model){
        if(categoryId==null) {
            model.addAttribute("title", "All Events");
            model.addAttribute("events", eventRepository.findAll());
        } else{
            Optional< EventCategory> result = eventCategoryRepository.findById(categoryId);
            if(result.isEmpty()) {
                model.addAttribute("title", "Invalid Category ID : "+categoryId);
            }else {
                EventCategory eventCategory = result.get();
                model.addAttribute("title", "Events in category : " + eventCategory.getName());
                model.addAttribute("events", eventCategory.getEvents());
            }
        }
        return "events/index";
    }

    @GetMapping("create")
    public String renderCreateEventForm(Model model){
        model.addAttribute("title","Create Event");
        model.addAttribute(new Event());
        model.addAttribute("categories", eventCategoryRepository.findAll());
        return "events/create";
    }

    @PostMapping("create")
    public String createEvent(@ModelAttribute @Valid Event newEvent, Errors errors, Model model){

        if(errors.hasErrors()){
            model.addAttribute("title","Create Event");
            return "events/create";
        }
        eventRepository.save(newEvent);
        return "redirect:/events";
    }



    @GetMapping("delete")
    public String deleteEventForm(Model model){
        model.addAttribute("title","Delete Event");
        model.addAttribute("events", eventRepository.findAll());
        return "events/delete";
    }

    @PostMapping("delete")
    public String processDeleteEventsForm(@RequestParam(required = false) int[] eventIds){
        if(eventIds != null) {
            for (int id : eventIds) {
                eventRepository.deleteById(id);
            }
        }
        return "redirect:/events";
    }

    @GetMapping("detail")
    public String displayEventDetails(Model model, @RequestParam int eventId){
        Optional<Event> result = eventRepository.findById(eventId);

        if(result.isEmpty()){
            model.addAttribute("title", "Invalid Event ID : "+eventId);
        } else{
            Event event = result.get();
            model.addAttribute("title", event.getName() + "Details");
            model.addAttribute("event", event);
            model.addAttribute("tags", event.getTags());
        }
        return "events/detail";
    }

    @GetMapping("addTag")
    public String DisplayAddTagFrom(@RequestParam Integer eventId, Model model){
        Optional<Event> result = eventRepository.findById(eventId);
        Event event = result.get();
        model.addAttribute("title", "Add Tag to: " + event.getName());
        model.addAttribute("tags", tagRepository.findAll());
        EventTagDTO eventTag =new EventTagDTO();
        eventTag.setEvent(event);
        model.addAttribute("eventtag", eventTag);
        model.addAttribute("eventTag",new EventTagDTO());
        return "events/addTag";
    }

    @PostMapping("addTag")
    public String processAddTagForm(@RequestParam("eventId") Integer eventId, @ModelAttribute @Valid EventTagDTO eventTagDTO, Errors errors, Model model) {
        if (errors.hasErrors()) {
            Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Invalid event ID"));
            Tag tag = eventTagDTO.getTag();
            if (!event.getTags().contains(tag)) {
                event.addTag(tag);
                eventRepository.save(event);
            }
            return "redirect:/events/detail?eventId=" + event.getId();
        }
        return "redirect:/events/addTag";
    }


}
