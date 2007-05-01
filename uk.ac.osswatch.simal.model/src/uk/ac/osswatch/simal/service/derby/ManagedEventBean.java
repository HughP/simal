/*
* Copyright 2007 University of Oxford
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
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
