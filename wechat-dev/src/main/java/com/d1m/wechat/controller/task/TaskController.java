package com.d1m.wechat.controller.task;

import com.alibaba.fastjson.JSONObject;
import com.d1m.wechat.controller.BaseController;
import com.d1m.wechat.dto.TaskDto;

import com.d1m.wechat.service.TaskService;

import com.d1m.wechat.service.impl.RefreshAccessTokenJob;
import com.d1m.wechat.util.Message;
import com.d1m.wechat.wxsdk.core.thread.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定时任务控制台
 */
@Controller
@RequestMapping("/task")
public class TaskController extends BaseController {
    private static Logger log = LoggerFactory.getLogger(TaskController.class);
    @Autowired
    private TaskService taskService;
    @Autowired
    private ThreadPoolTaskExecutor callerRunsExecutor;

    @RequestMapping(value = "runNow/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject runNow(@PathVariable Integer taskId,
                             HttpServletRequest request, HttpServletResponse response) {
        try {
            TaskDto task = taskService.getTaskById(taskId);
            taskService.runAJobNow(task);
            return representation(Message.SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }

    @RequestMapping(value = "pause/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject pause(@PathVariable Integer taskId,
                             HttpServletRequest request, HttpServletResponse response) {
        try {
            TaskDto task = taskService.getTaskById(taskId);
            taskService.pauseJob(task);
            return representation(Message.SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }

    @RequestMapping(value = "resume/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject resume(@PathVariable Integer taskId,
                            HttpServletRequest request, HttpServletResponse response) {
        try {
            TaskDto task = taskService.getTaskById(taskId);
            taskService.resumeJob(task);
            return representation(Message.SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }

    @RequestMapping(value = "changeStatus/{taskId}/{cmd}", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject changeStatus(@PathVariable Integer taskId,@PathVariable String cmd,
                           HttpServletRequest request, HttpServletResponse response) {
        try {
            taskService.changeStatus(taskId,cmd);
            return representation(Message.SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }

    @RequestMapping(value = "updateCron/{taskId}", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateCron(@PathVariable Integer taskId, @RequestBody(required = false) JSONObject jsonObject,
                                   HttpServletRequest request, HttpServletResponse response) {
        try {
            taskService.updateCron(taskId,jsonObject.getString("cron"));
            return representation(Message.SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }

    @RequestMapping(value = "runningJob", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject runningJob(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<TaskDto> list = taskService.getRunningJob();
            return representation(Message.SUCCESS,list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }

    @RequestMapping(value = "allJob", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject allJob(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<TaskDto> list = taskService.getAllJob();
            return representation(Message.SUCCESS,list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }

    @RequestMapping(value = "threadStatus", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject threadStatus(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String,Object> data = new HashMap<String,Object>();
            data.put("CurrentPoolSize",callerRunsExecutor.getPoolSize());
            data.put("CorePoolSize",callerRunsExecutor.getCorePoolSize());
            data.put("MaxPoolSize",callerRunsExecutor.getMaxPoolSize());
            data.put("ActiveCount",callerRunsExecutor.getActiveCount());
            return representation(Message.SUCCESS,data);
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }

    @RequestMapping(value = "updateThread", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateThread(@RequestBody(required = false) JSONObject jsonObject,
                                 HttpServletRequest request, HttpServletResponse response) {
        try {
            if(jsonObject!=null){
                Integer corePoolSize = jsonObject.getInteger("CorePoolSize");
                if(corePoolSize!=null){
                    callerRunsExecutor.setCorePoolSize(corePoolSize);
                }
                Integer maxPoolSize = jsonObject.getInteger("MaxPoolSize");
                if(maxPoolSize!=null){
                    callerRunsExecutor.setMaxPoolSize(maxPoolSize);
                }
                Integer queueCapacity = jsonObject.getInteger("QueueCapacity");
                if(queueCapacity!=null){
                    callerRunsExecutor.setQueueCapacity(queueCapacity);
                }
            }
            return representation(Message.SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }

    @RequestMapping(value = "setAccessToken", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject setAccessToken(@RequestBody(required = false) JSONObject jsonObject,
                                   HttpServletRequest request, HttpServletResponse response) {
        try {
            if(jsonObject!=null){
                Integer wechatId = jsonObject.getInteger("wechatId");
                String tokenStr = jsonObject.getString("accessToken");
                Integer expired = jsonObject.getInteger("expired");
                if(expired==null){
                    expired = 7200;
                }
                RefreshAccessTokenJob.accessTokenMap.put(wechatId,new AccessToken(tokenStr,expired));
            }
            return representation(Message.SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }
}