package uk.ac.osswatch.simal.service;

import java.util.Set;

import uk.ac.osswatch.simal.model.Event;

public interface IEventService {
    public Event findEvent(Integer id);
    public int save(Event event);
    public boolean delete(Event event);
    public boolean update(Event event);
    public Set<Event> findAll();
}
