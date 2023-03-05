package com;


import pojo.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskCom {
    private static int taskCount = 0;
    private static List<Task> tasks = new ArrayList<>();

    public int addTask(Task task) {
        task.setTaskId(++taskCount);
        tasks.add(task);
        return taskCount;
    }

    public void updateTask(Task task) {
        for (Task t : tasks) {
            if (t.getTaskId() == task.getTaskId()) {
                t.setTaskTitle(task.getTaskTitle());
                t.setTaskText(task.getTaskText());
                t.setAssignedTo(task.getAssignedTo());
                t.setTaskCompleted(task.isTaskCompleted());
                break;
            }
        }
    }

    public void deleteTask(int taskId) {
        tasks.removeIf(t -> t.getTaskId() == taskId);
    }

    public Task getTaskById(int taskId) {
        for (Task t : tasks) {
            if (t.getTaskId() == taskId) {
                return t;
            }
        }
        return null;
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public List<Task> getTasksByUser(String email) {
        List<Task> userTasks = new ArrayList<>();
        for (Task t : tasks) {
            if (t.getAssignedTo().equals(email)) {
                userTasks.add(t);
            }
        }
        return userTasks;
    }
    public List<Task> getAllTasksForUser(String userEmail) {
        List<Task> tasks = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/gl2023",
                "root","root");
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM tasks WHERE assigned_to = ?")) {
            statement.setString(1, userEmail);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Task task = new Task(
                            resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getString("text"),
                            resultSet.getString("assigned_to"),
                            resultSet.getBoolean("completed")
                    );
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }




}
