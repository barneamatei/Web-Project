package org.launchcode.hello_spring.data;

import org.launchcode.hello_spring.models.EventCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventCategoryRepository extends CrudRepository<EventCategory, Integer> {
}
