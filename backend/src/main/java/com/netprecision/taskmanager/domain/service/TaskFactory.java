package com.netprecision.taskmanager.domain.service;

import com.netprecision.taskmanager.domain.model.Task;
import com.netprecision.taskmanager.domain.model.TaskTitle;
import org.springframework.stereotype.Component;

@Component
public class TaskFactory {

    public Task create(Long userId, String title, String description) {
        return new Task(null, userId, new TaskTitle(title), description, false);
    }

    public Task restore(Long id, Long userId, String title, String description, boolean completed) {
        return new Task(id, userId, new TaskTitle(title), description, completed);
    }
}
