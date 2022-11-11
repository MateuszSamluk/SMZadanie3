package com.example.systemymobilne_zadanie2;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskStorage {
    private static final TaskStorage taskStorage = new TaskStorage();
    private final List<Task> tasks;

    public List<Task> getTasks(){
        return tasks;
    }

    public static TaskStorage getInstance(){
        return taskStorage;
    }
    private TaskStorage(){
        tasks = new ArrayList<>();
        for(int i=0; i<10; i++){
            Task task = new Task();
            task.setName("Pilne zadanie nr " + i);
            if(i%3 == 0){
            task.setDone(true);
            task.setCategory(Category.STUDIES);
            }
            else{
                task.setCategory(Category.HOME);
            }
            tasks.add(task);
        }
    }

    public Task getTask(UUID taskId) {
        Task taskFound = new Task();
        for(Task task : tasks){
            if(task.getId().equals(taskId)) taskFound = task;
        }
        return taskFound;
    }
    public void addTask(Task task){
        tasks.add(task);
    }
}
