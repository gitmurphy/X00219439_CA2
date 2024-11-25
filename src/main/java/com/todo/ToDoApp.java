package com.todo;

import java.util.ArrayList;
import java.util.Scanner;

public class ToDoApp {
    private static ArrayList<String> taskList = new ArrayList<>();
    public static void main(String[] args) {
        System.out.println("Welcome to the To Do App!");

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println(displayTaskList());
            System.out.println("\nOptions:\n1. Add Task\n2. Exit");
            System.out.println("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice) {
                case 1:
                    System.out.print("Enter a new task: ");
                    String task = scanner.nextLine();
                    addTask(task);
                    break;
                case 2:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    public static ArrayList<String> getTaskList() {
        return taskList;
    }

    public static String displayTaskList() {
        if (taskList.isEmpty()) {
            return "Task List is empty.";
        } else {
            StringBuilder output = new StringBuilder("Task List:");
            for (String task : taskList) {
                output.append("\n").append(task);
            }
            return output.toString();
        }
    }

    public static void addTask(String task) {
        if (task == null || task.trim().isEmpty()) {
            System.out.println("Task cannot be empty.");
        } else {
            taskList.add(task);
            System.out.println("Task added: " + task);
        }
    }

    public static void clearTaskList() {
        taskList.clear();
    }
}
