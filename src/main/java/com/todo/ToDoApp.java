import java.util.ArrayList;
import java.util.Scanner;

public class ToDoApp {
    private static ArrayList<String> taskList = new ArrayList<>();
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            displayTaskList();
            System.out.println("\nOptions:\n1. Add Task\n2. Exit");
            System.out.println("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice) {
                case 1:
                    addTask(scanner);
                    break;
                case 2:
                    running = false;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    private static void displayTaskList() {
        if (taskList.isEmpty()) {
            System.out.println("\n Task List is empty.");
        } else {
            System.out.println("\nTask List:");
            for (String task : taskList) {
                System.out.println(task);
            }
        }
    }

    private static void addTask(Scanner scanner) {
        System.out.println("Enter a new task: ");
        String task = scanner.nextLine();
        taskList.add(task);
        System.out.println("Task added: " + task);
    }
}
