/** @@author A0142130A **/
package seedu.taskell.logic.commands;

import seedu.taskell.commons.core.EventsCenter;
import seedu.taskell.commons.events.ui.DisplayListChangedEvent;

/** Lists a list of previous commands available for Undo operation
 * */
public class ViewHistoryCommand extends Command {
    
    public static final String COMMAND_WORD_1 = "history";
    public static final String COMMAND_WORD_2 = "hist";

    public static final String MESSAGE_SUCCESS = "Listed all commands available for undo.";
    public static final String MESSAGE_UNSUCCESSFUL = "Error displaying history.";
    
    private static ViewHistoryCommand self;
    
    public ViewHistoryCommand() {}
    
    public static ViewHistoryCommand getInstance() {
        if (self == null) {
            self = new ViewHistoryCommand();
        }
        
        return self;
    }

    @Override
    public CommandResult execute() {
        try {
            indicateDisplayListChanged();
        } catch (Exception e) {
            return new CommandResult(MESSAGE_UNSUCCESSFUL);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }

    public void indicateDisplayListChanged() {
        EventsCenter.getInstance().post(
                new DisplayListChangedEvent(history.getListCommandText()));
    }

}
