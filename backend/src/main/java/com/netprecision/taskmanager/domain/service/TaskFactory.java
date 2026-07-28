package com.netprecision.taskmanager.domain.service;

import com.netprecision.taskmanager.domain.model.Task;
import com.netprecision.taskmanager.domain.model.TaskTitle;
import org.springframework.stereotype.Component;

@Component
public class TaskFactory {

    public Task create(String title, String description) {
        return new Task(null, new TaskTitle(title), description, false);
    }

    public Task restore(Long id, String title, String description, boolean completed) {
        return new Task(id, new TaskTitle(title), description, completed);
    }
}
