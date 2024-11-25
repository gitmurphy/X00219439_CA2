package com.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.todo.ToDoApp.displayTaskList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ToDoAppTest {

    @BeforeEach
    void setUp() {
        // Clear the task list before each test
        ToDoApp.getTaskList().clear();
    }

    @Test
    void testAddTask() {
        // Test the addTask method in ToDoApp
        String testTask = "Wash car";
        ToDoApp.addTask(testTask);

        // Test that the item has been added correctly
        assertEquals(1, ToDoApp.getTaskList().size(), "Task list should have one item after adding a task.");
        assertEquals(testTask, ToDoApp.getTaskList().get(0), "The added task should match the first item in the task list.");
    }

    @Test
    void testDisplayTaskList() {
        // Add tasks using the ToDoApp addTask method
        ToDoApp.addTask("Cook dinner");
        ToDoApp.addTask("Do laundry");

        // Expected output from the displayTaskList method
        String expectedOutput = "Task List:\nCook dinner\nDo laundry";
        System.out.println(displayTaskList());
        System.out.println(expectedOutput);
        assertEquals(expectedOutput, displayTaskList(), "Display task list should return the correct formatted output.");
    }
}