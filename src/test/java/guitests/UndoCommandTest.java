/** @@author A0142130A **/
package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import guitests.guihandles.TaskCardHandle;
import seedu.taskell.commons.core.Messages;
import seedu.taskell.history.History;
import seedu.taskell.history.HistoryManager;
import seedu.taskell.logic.commands.UndoCommand;
import seedu.taskell.testutil.TestTask;
import seedu.taskell.testutil.TestUtil;

public class UndoCommandTest extends TaskManagerGuiTest {
    
    private static final String UNDO = "undo";
    private History history = HistoryManager.getInstance();
    
    @Test
    public void undoAtInvalidIndex_invalidCommand() {
        history.clear();
        commandBox.runCommand("undo 3");
        assertResultMessage(UndoCommand.MESSAGE_COMMAND_HISTORY_EMPTY);
        
        commandBox.runCommand("add 100 things");
        commandBox.runCommand("delete 1");
        commandBox.runCommand("undo 50");
        assertResultMessage(UndoCommand.MESSAGE_INVALID_INDEX);
        
        commandBox.runCommand("undo -1");
        assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, 
                UndoCommand.MESSAGE_USAGE));
        
        history.clear();
    }
    
    @Test
    public void undoInvalidCommands_nothingToUndo() {
        history.clear();
        
        commandBox.runCommand("clear");
        commandBox.runCommand("find chicken");
        commandBox.runCommand("undo");
        assertResultMessage(UndoCommand.MESSAGE_COMMAND_HISTORY_EMPTY);
        
        history.clear();
    }
    
    @Test
    public void undoAtSpecificIndex_success() {
        history.clear();
        
        TestTask[] currentList = td.getTypicalTasks();
        TestTask taskToAdd = td.holdMeeting;
        
        commandBox.runCommand(taskToAdd.getAddCommand());
        
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);
        int index = currentList.length;
        commandBox.runCommand("edit " + index + " desc: hello");
        
        commandBox.runCommand("delete 2");
        commandBox.runCommand("hist");
        commandBox.runCommand("undo 1");    //referring to edit
        
        //confirm the edited card contains the old data
        TaskCardHandle editedCard = taskListPanel.navigateToTask(taskToAdd.getDescription().description);
        assertMatching(taskToAdd, editedCard);
        
        history.clear();
    }
    
    @Test
    public void undoAdd_success() {
        history.clear();
        
        TestTask[] currentList = td.getTypicalTasks();
        TestTask taskToAdd = td.holdMeeting;
        
        //add one task
        commandBox.runCommand(taskToAdd.getAddCommand());
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);
        
        commandBox.runCommand(UNDO);
        assertDeleteSuccess(currentList.length, currentList);
        
        history.clear();
    }
    
    @Test
    public void undoDelete_success() {
        history.clear();
        
        TestTask[] currentList = td.getTypicalTasks();
        TestTask taskToDelete = currentList[0];
        int target = 1;
        
        //delete a task
        commandBox.runCommand("delete " + target);
        assertDeleteSuccess(target, currentList);
        currentList = TestUtil.removeTaskFromList(currentList, 1);
        
        commandBox.runCommand(UNDO);
        assertAddSuccess(taskToDelete, currentList);
        
        history.clear();
    }
    
    @Test
    public void undoAndRedoAdd_success() {
        history.clear();
        
        TestTask[] currentList = td.getTypicalTasks();
        TestTask taskToAdd = td.holdMeeting;
        
        //add one task
        commandBox.runCommand(taskToAdd.getAddCommand());    
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);
        
        commandBox.runCommand(UNDO);
        assertDeleteSuccess(currentList.length, currentList);
        currentList = TestUtil.removeTaskFromList(currentList, currentList.length);
        
        commandBox.runCommand(UNDO);
        assertAddSuccess(taskToAdd, currentList);
        
        history.clear();
    }
    
    @Test
    public void undoAndRedoDelete_success() {
        history.clear();
        
        TestTask[] currentList = td.getTypicalTasks();
        TestTask taskToDelete = currentList[0];
        int target = 1;
        
        //delete a task
        commandBox.runCommand("delete " + target);
        assertDeleteSuccess(target, currentList);
        currentList = TestUtil.removeTaskFromList(currentList, 1);
        
        commandBox.runCommand(UNDO);
        assertAddSuccess(taskToDelete, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToDelete);
        
        target = currentList.length;
        commandBox.runCommand(UNDO);
        assertDeleteSuccess(target, currentList);
        
        history.clear();
    }
    
    @Test
    public void undoEdit_success() {
        history.clear();
        
        TestTask[] currentList = td.getTypicalTasks();
        TestTask taskToAdd = td.holdMeeting;
        
        //add one task
        commandBox.runCommand(taskToAdd.getAddCommand());
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);
        
        commandBox.runCommand("edit " + currentList.length + " desc: hold chicken");
        commandBox.runCommand(UNDO);
        
        currentList = TestUtil.removeTaskFromList(currentList, currentList.length); //need to remove from 
                                                                                    //list since assertAddSuccess 
                                                                                    //will add the task back in
        assertAddSuccess(taskToAdd, currentList);
        
        history.clear();
    }
    
    @Test
    public void undoDone_success() {
        history.clear();
        
        TestTask[] currentList = td.getTypicalTasks();
        TestTask taskToDelete = currentList[0];
        int target = 1;
        
        commandBox.runCommand("done 1");
        commandBox.runCommand("list");  //to show only incomplete tasks
        assertDeleteSuccess(target, currentList);
        currentList = TestUtil.removeTaskFromList(currentList, 1);
        
        commandBox.runCommand(UNDO);
        commandBox.runCommand("list");  //to show only incomplete tasks
        
        TaskCardHandle newCard = taskListPanel.navigateToTask(taskToDelete.getDescription().description);
        assertMatching(taskToDelete, newCard);
        
        history.clear();
    }
    
    @Test
    public void undoUndone_success() {
        history.clear();
        
        TestTask[] currentList = td.getTypicalTasks();
        
        commandBox.runCommand("done 1");
        commandBox.runCommand("list-done");  //to show only complete tasks
        commandBox.runCommand("undone 1");
        commandBox.runCommand("list");  //to show only incomplete tasks
        
        commandBox.runCommand(UNDO);
        commandBox.runCommand("list");  //to show only incomplete tasks
        
        int target = 1;
        assertDeleteSuccess(target, currentList);   //final list should exclude the task tested
        
        history.clear();
    }
    
    @Test
    public void userDeletedTask_undoRemovesAddHistory() {
        history.clear();
        
        TestTask[] currentList = td.getTypicalTasks();
        TestTask taskToAdd = td.holdMeeting;
        
        commandBox.runCommand(taskToAdd.getAddCommand());
        assertAddSuccess(taskToAdd, currentList);
        
        commandBox.runCommand("hist");    
        String historyText = displayPanel.getText();
        assertTrue(historyText.contains(taskToAdd.getAddCommand().trim()));
        
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);
        commandBox.runCommand("delete " + currentList.length);
        assertDeleteSuccess(currentList.length, currentList);
        
        commandBox.runCommand("hist");
        historyText = displayPanel.getText();
        assertTrue(!historyText.contains(taskToAdd.getAddCommand().trim()));
        
        history.clear();
    }
    
    @Test
    public void userEditedTask_undoRemovesAddHistory() {
        history.clear();
        
        TestTask[] currentList = td.getTypicalTasks();
        TestTask taskToAdd = td.holdMeeting;
        
        commandBox.runCommand(taskToAdd.getAddCommand());
        assertAddSuccess(taskToAdd, currentList);
        
        commandBox.runCommand("hist");    
        String historyText = displayPanel.getText();
        assertTrue(historyText.contains(taskToAdd.getAddCommand().trim()));
        
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);
        String editInput = "edit " + currentList.length + " desc: hello";
        commandBox.runCommand(editInput);
        
        commandBox.runCommand("hist");
        historyText = displayPanel.getText();
        assertTrue(!historyText.contains(taskToAdd.getAddCommand().trim()));
        assertTrue(historyText.contains(editInput));
        
        history.clear();
    }
    
    private void assertAddSuccess(TestTask taskToAdd, TestTask... currentList) {
        //confirm the new card contains the right data
        TaskCardHandle addedCard = taskListPanel.navigateToTask(taskToAdd.getDescription().description);
        assertMatching(taskToAdd, addedCard);

        //confirm the list now contains all previous tasks plus the new task
        TestTask[] expectedList = TestUtil.addTasksToList(currentList, taskToAdd);
        assertTrue(taskListPanel.isListMatching(expectedList));
    }
    
    /**
     * Confirms the undo add result is correct. Means task has been deleted.
     * @param targetIndexOneIndexed e.g. to delete the first task in the list, 1 should be given as the target index.
     * @param currentList A copy of the current list of tasks (before deletion).
     */
    private void assertDeleteSuccess(int targetIndexOneIndexed, final TestTask[] currentList) {
        TestTask[] expectedRemainder = TestUtil.removeTaskFromList(currentList, targetIndexOneIndexed);

        //confirm the list now contains all previous tasks except the deleted task
        assertTrue(taskListPanel.isListMatching(expectedRemainder)); 
    }
    
}
