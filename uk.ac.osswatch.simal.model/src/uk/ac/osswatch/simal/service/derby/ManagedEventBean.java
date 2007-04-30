package uk.ac.osswatch.simal.service.derby;

import java.util.HashSet;
import java.util.Set;

import uk.ac.osswatch.simal.model.Event;
import uk.ac.osswatch.simal.service.IEventService;

public class ManagedEventBean implements IEventService {
    Set<Event> events;

    public boolean delete(Event event) {
        // TODO Auto-generated method stub
        return false;
    }

    public Event findEvent(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

    public int save(Event event) {
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean update(Event event) {
        // TODO Auto-generated method stub
        return false;
    }

    public Set<Event> findAll() {
        if (events == null) init();
        return events;
    }

    private void init() {
        events = new HashSet<Event>();

        Event event = new Event(
                "Event 1",
                "This is event 1");
        events.add(event);
        
        event = new Event(
                "Event 2",
                "This is event 2");
        events.add(event);
        
        event = new Event(
                "Event 3",
                "This is event 3");
        events.add(event);
    }

}
