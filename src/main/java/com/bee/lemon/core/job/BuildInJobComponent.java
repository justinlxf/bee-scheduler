package com.bee.lemon.core.job;

import com.alibaba.fastjson.JSONObject;
import com.bee.lemon.core.SpringApplicationContext;
import com.bee.lemon.service.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Calendar;

/**
 * @author weiwei
 *         用于清除历史任务记录
 */
public class BuildInJobComponent extends JobComponent {
    @Override
    public String getName() {
        return "BuildInJobComponent";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getAuthor() {
        return "weiwei";
    }

    @Override
    public String getDescription() {
        return "用于执行内置任务";
    }

    @Override
    public String getParamTemplate() {

        StringBuilder t = new StringBuilder();
        t.append("{\r");
        t.append("	\"task\":\"\"\r");
        t.append("}");
        return t.toString();
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();
        JobExecutionContextHelper.appendExecLog(context, "开始执行任务 -> " + jobDetail.getKey());
        try {
            JSONObject taskParam = getTaskParam(context);
            JobExecutionContextHelper.appendExecLog(context, "任务参数 -> " + taskParam.toString());
            if (StringUtils.equals("clear_task_history", taskParam.getString("task"))) {
                // 保留最近5天任务记录
                int keepDays = taskParam.getInteger("keep_days");
                Calendar date_point = DateUtils.truncate(Calendar.getInstance(), Calendar.DAY_OF_MONTH);
                date_point.set(Calendar.DAY_OF_MONTH, date_point.get(Calendar.DAY_OF_MONTH) - keepDays);

                TaskService taskService = SpringApplicationContext.getBean(TaskService.class);
                int result = taskService.clearHistoryBefore(date_point.getTime());
                JobExecutionContextHelper.appendExecLog(context, "执行完成 -> " + "清除历史任务记录完毕，已成功清除 " + result + " 条记录");

            }
        } catch (Exception e) {
            JobExecutionException jobExecutionException = new JobExecutionException(e);
            throw jobExecutionException;
        }

    }

}