package seedu.taskell.logic.commands;

import java.util.HashSet;
import java.util.Set;

import seedu.taskell.commons.exceptions.IllegalValueException;
import seedu.taskell.model.tag.Tag;
import seedu.taskell.model.tag.UniqueTagList;
import seedu.taskell.model.task.*;

/**
 * Adds a task to the task manager.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to the task manager. "
            + "Parameters: NAME p/TASK_TYPE p/TASK_DATE e/START_TIME e/END_TIME a/TASK_PRIORITY [t/TAG]...\n"
            + "Example: " + COMMAND_WORD
            + " John Doe p/EVENT p/1-1-2015 e/12.30AM e/12.45AM a/311, Clementi Ave 2, #02-25 t/friends t/owesMoney";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the task manager";

    private final Task toAdd;

    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */

    public AddCommand(String description, String taskType, String taskDate, String startTime, String endTime, String taskPriority, Set<String> tags)
            throws IllegalValueException {
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        
        switch (taskType) {
        case Task.FLOATING_TASK: 
            this.toAdd = new FloatingTask(
                    new Description(description), 
                    Task.FLOATING_TASK, 
                    new TaskDate(TaskDate.DEFAULT_DATE), 
                    new TaskTime(TaskTime.DEFAULT_START_TIME), 
                    new TaskTime(TaskTime.DEFAULT_END_TIME), 
                    new TaskPriority(taskPriority),
                    new UniqueTagList(tagSet));
            break;
        case Task.DEADLINE_TASK:
            this.toAdd = new DeadlineTask(
                    new Description(description),
                    Task.DEADLINE_TASK,
                    new TaskDate(taskDate),
                    new TaskTime(TaskTime.DEFAULT_START_TIME),
                    new TaskTime(endTime),
                    new TaskPriority(taskPriority),
                    new UniqueTagList(tagSet));
            break;
        case Task.EVENT_TASK:
            this.toAdd = new EventTask(
                    new Description(description),
                    Task.EVENT_TASK,
                    new TaskDate(taskDate),
                    new TaskTime(startTime),
                    new TaskTime(endTime),
                    new TaskPriority(taskPriority),
                    new UniqueTagList(tagSet));
            break;
        default:
            toAdd = null;
        }
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        try {
            model.addTask(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            return new CommandResult(MESSAGE_DUPLICATE_TASK);
        }

    }

}
