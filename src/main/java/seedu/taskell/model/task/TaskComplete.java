package seedu.taskell.model.task;

import java.util.Objects;

public class TaskComplete {
    public static final String FINISHED = "finished";
    public static final String INCOMPLETE = "incomplete";
    public final String taskStatus;
    
    public TaskComplete(String taskStatus) {
        this.taskStatus = taskStatus;
    }
    
    public String taskStatus() {
        return taskStatus;
    }
    
    public static boolean isValidTaskComplete(String TaskToValidate) {
        return TaskToValidate.equals(FINISHED)  || TaskToValidate.equals(INCOMPLETE);           
    }
    
    @Override
    public String toString() {
        return taskStatus;
    }
    
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskComplete // instanceof handles nulls
                        && this.taskStatus.equals(((TaskComplete)other).taskStatus)); // state check
    }
    @Override
    public int hashCode() {
        return taskStatus.hashCode();
    }
}
