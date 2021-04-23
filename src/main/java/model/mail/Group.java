package model.mail;

import java.util.ArrayList;
import java.util.List;

/**
 * Group of victims
 *
 * @author Olivier Liechti
 */
public class Group {
    private final List<Person> members = new ArrayList<>();

    /**
     * Add a Person to the group
     *
     * @param person
     */
    public void addMember(Person person){
        members.add(person);
    }

    /**
     * Get members
     *
     * @return Members of the group
     */
    public List<Person> getMembers() {
        return new ArrayList<>(members);
    }
}
