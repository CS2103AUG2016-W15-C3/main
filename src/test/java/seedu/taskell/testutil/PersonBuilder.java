package seedu.taskell.testutil;

import seedu.taskell.commons.exceptions.IllegalValueException;
import seedu.taskell.model.tag.Tag;
import seedu.taskell.model.task.*;

/**
 *
 */
public class PersonBuilder {

    private TestPerson person;

    public PersonBuilder() {
        this.person = new TestPerson();
    }

    public PersonBuilder withDescription(String description) throws IllegalValueException {
        this.person.setDescription(new Description(description));
        return this;
    }

    public PersonBuilder withTags(String ... tags) throws IllegalValueException {
        for (String tag: tags) {
            person.getTags().add(new Tag(tag));
        }
        return this;
    }

    public PersonBuilder withTaskPriority(String taskPriority) throws IllegalValueException {
        this.person.setTaskPriority(new TaskPriority(taskPriority));
        return this;
    }

    public PersonBuilder withTaskDate(String taskDate) throws IllegalValueException {
        this.person.setPhone(new TaskDate(taskDate));
        return this;
    }

    public PersonBuilder withEmail(String email) throws IllegalValueException {
        this.person.setEmail(new TaskTime(email));
        return this;
    }

    public TestPerson build() {
        return this.person;
    }

}
