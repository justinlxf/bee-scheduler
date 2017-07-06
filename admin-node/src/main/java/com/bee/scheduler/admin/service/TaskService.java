package com.bee.scheduler.admin.service;


import com.bee.scheduler.admin.model.ExecutingTask;
import com.bee.scheduler.admin.model.Pageable;
import com.bee.scheduler.admin.model.Task;
import com.bee.scheduler.admin.model.TaskHistory;

import java.util.Date;
import java.util.List;

/**
 * @author weiwei
 */
public interface TaskService {
    Task getTask(String schedulerName, String name, String group);

    Pageable<Task> queryTask(String schedulerName, String name, String group, String state, int page);

    int queryTaskCount(String schedulerName, String name, String group, String state);

    List<ExecutingTask> queryExcutingTask(String schedulerName);

    int insertTaskHistory(TaskHistory taskHistory);

    int insertTaskHistories(List<TaskHistory> taskHistoryList);

    TaskHistory getTaskHistory(String fireId);

    Pageable<TaskHistory> queryTaskHistory(String schedulerName, String fireId, String taskName, String taskGroup, String state, Integer triggerType, Long beginTime, Long endTime, Integer page);

    List<String> getTaskHistoryGroups(String schedulerName);

    int clearHistoryBefore(String schedulerName, Date date);
}
