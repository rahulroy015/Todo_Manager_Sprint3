import com.TaskCom;
import com.UserCom;
import model.User;
import pojo.Task;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static TaskCom taskDao = new TaskCom();
    private static UserCom userDao = new UserCom();
    private static User currentUser = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("==== Task Manager ====");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("0. Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    loginUser(scanner);
                    break;
                case 2:
                    registerUser(scanner);
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void loginUser(Scanner scanner) {
        System.out.println("==== Login ====");
        System.out.print("Enter email: ");
        String email = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();

        User user = userDao.getUserByEmailAndPassword(email, password);

        if (user != null) {
            System.out.println("Welcome, " + user.getUsername() + "!");
            currentUser = user;
            showTaskMenu(scanner);
        } else {
            System.out.println("Invalid email or password.");
        }
    }

    private static void registerUser(Scanner scanner) {
        System.out.println("==== Register ====");
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter email: ");
        String email = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();

        User user = new User(username, email, password);
        userDao.addUser(user);

        System.out.println("Registration successful! Please login to continue.");
    }

    private static void showTaskMenu(Scanner scanner) {
        while (true) {
            System.out.println("==== Task Menu ====");
            System.out.println("1. Add Task");
            System.out.println("2. List Tasks");
            System.out.println("3. Update Task");
            System.out.println("4. Delete Task");
            System.out.println("0. Logout");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addTask(scanner, currentUser);
                    break;
                case 2:
                    listTasks();
                    break;
                case 3:
                    updateTask(scanner);
                    break;
                case 4:
                    deleteTask(scanner);
                    break;
                case 0:
                    System.out.println("Logged out.");
                    currentUser = null;
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void addTask(Scanner scanner, User user) {
        System.out.println("==== Add Task ====");
        System.out.print("Enter task title: ");
        String title = scanner.next();
        System.out.print("Enter task text: ");
        String text = scanner.next();

        Task task = new Task(title, text, user.getEmail());
        taskDao.addTask(task);

        System.out.println("Task added successfully!");
    }

    private static void listTasks() {
        System.out.println("==== List Tasks ====");

        List<Task> tasks = taskDao.getAllTasksForUser(currentUser.getEmail());

        for (Task task : tasks) {
            System.out.println("Task #" + task.getTaskId() + ": " + task.getTaskTitle() + " - " + task.getTaskText() + " (completed: " + task.isTaskCompleted() + ")");
        }

        if (tasks.isEmpty()) {
            System.out.println("No complete this function");
        }
    }

    private static void completeTask(Scanner scanner) {
        System.out.println("==== Complete Task ====");
        System.out.print("Enter task id: ");
        int taskId = scanner.nextInt();

        Task task = taskDao.getTaskById(taskId);

        if (task == null) {
            System.out.println("Task not found.");
            return;
        }

        if (task.isTaskCompleted()) {
            System.out.println("Task already completed.");
            return;
        }

        task.setTaskCompleted(true);
        taskDao.updateTask(task);

        System.out.println("Task completed successfully!");
    }

    private static void updateTask(Scanner scanner) {
        System.out.println("==== Update Task ====");
        System.out.print("Enter task id: ");
        int taskId = scanner.nextInt();

        Task task = taskDao.getTaskById(taskId);

        if (task == null) {
            System.out.println("Task not found.");
            return;
        }

        System.out.print("Enter new task title (leave blank to keep current title): ");
        String title = scanner.next();
        if (!title.isEmpty()) {
            task.setTaskTitle(title);
        }

        System.out.print("Enter new task text (leave blank to keep current text): ");
        String text = scanner.next();
        if (!text.isEmpty()) {
            task.setTaskText(text);
        }

        taskDao.updateTask(task);

        System.out.println("Task updated successfully!");
    }

    private static void deleteTask(Scanner scanner) {
        System.out.println("==== Delete Task ====");
        System.out.print("Enter task id: ");
        int taskId = scanner.nextInt();

        Task task = taskDao.getTaskById(taskId);

        if (task == null) {
            System.out.println("Task not found.");
            return;
        }

        taskDao.deleteTask(task.getTaskId());

        System.out.println("Task deleted successfully!");
    }


    private static void exit() {
        System.out.println("Exiting program...");
        System.exit(0);
    }
}