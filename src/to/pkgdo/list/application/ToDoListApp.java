package to.pkgdo.list.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class ToDoListApp {
    private JFrame frame;
    private DefaultListModel<Task> taskModel;
    private JList<Task> taskList;
    private JTextField taskInputField;
    private JButton addButton, deleteButton, markCompleteButton, saveButton, loadButton;

    public ToDoListApp() {
        // Initialize UI Components
        frame = new JFrame("To-Do List Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        taskModel = new DefaultListModel<>();
        taskList = new JList<>(taskModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setCellRenderer(new TaskCellRenderer());

        taskInputField = new JTextField(20);

        addButton = new JButton("Add Task");
        deleteButton = new JButton("Delete Task");
        markCompleteButton = new JButton("Mark Complete");
        saveButton = new JButton("Save Tasks");
        loadButton = new JButton("Load Tasks");

        // Layout Setup
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("New Task:"));
        inputPanel.add(taskInputField);
        inputPanel.add(addButton);

        JPanel actionPanel = new JPanel();
        actionPanel.add(markCompleteButton);
        actionPanel.add(deleteButton);
        actionPanel.add(saveButton);
        actionPanel.add(loadButton);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(taskList), BorderLayout.CENTER);
        frame.add(actionPanel, BorderLayout.SOUTH);

        // Event Listeners
        addButton.addActionListener(new AddTaskAction());
        deleteButton.addActionListener(new DeleteTaskAction());
        markCompleteButton.addActionListener(new MarkCompleteAction());
        saveButton.addActionListener(new SaveTaskAction());
        loadButton.addActionListener(new LoadTaskAction());

        // Display UI
        frame.setVisible(true);
    }

    // Task Class
    static class Task {
        private String title;
        private boolean isCompleted;

        public Task(String title) {
            this.title = title;
            this.isCompleted = false;
        }

        public String getTitle() {
            return title;
        }

        public boolean isCompleted() {
            return isCompleted;
        }

        public void setCompleted(boolean completed) {
            isCompleted = completed;
        }

        @Override
        public String toString() {
            return (isCompleted ? "[✔] " : "[ ] ") + title;
        }
    }

    class AddTaskAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String taskTitle = taskInputField.getText().trim();
            if (!taskTitle.isEmpty()) {
                taskModel.addElement(new Task(taskTitle));
                taskInputField.setText("");
            } else {
                JOptionPane.showMessageDialog(frame, "Task cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Delete Task Action
    class DeleteTaskAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                taskModel.remove(selectedIndex);
            } else {
                JOptionPane.showMessageDialog(frame, "Select a task to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Mark Complete Action
    class MarkCompleteAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                Task task = taskModel.get(selectedIndex);
                task.setCompleted(true);
                taskList.repaint();
            } else {
                JOptionPane.showMessageDialog(frame, "Select a task to mark as complete!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Save Task Action
    class SaveTaskAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.txt"))) {
                for (int i = 0; i < taskModel.size(); i++) {
                    Task task = taskModel.get(i);
                    writer.write(task.isCompleted() + "|" + task.getTitle());
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(frame, "Tasks saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving tasks!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Load Task Action
    class LoadTaskAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try (BufferedReader reader = new BufferedReader(new FileReader("tasks.txt"))) {
                taskModel.clear();
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|", 2);
                    boolean isCompleted = Boolean.parseBoolean(parts[0]);
                    String title = parts[1];
                    Task task = new Task(title);
                    task.setCompleted(isCompleted);
                    taskModel.addElement(task);
                }
                JOptionPane.showMessageDialog(frame, "Tasks loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error loading tasks!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToDoListApp::new);
    }
}
