package com.scheduler.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.scheduler.task.ScheduleTask;

@RestController
@RequestMapping("/v1")
public class SchedulerController {

    @Autowired
    private ScheduleTask scheduleTask;


    @GetMapping("/ping")
    public String ping(){
        return "Default path is working!";
    }


    @GetMapping("/status")
    @ResponseBody
    public String checkStatus(){
        return "Scheduler is working!";
    }

    @PostMapping(value = "/job/{jobCode}")
    @ResponseBody
    public String changeSchedule(@PathVariable String jobCode, 
                                 @RequestBody String expression){
        System.out.println(String.format("Request received for Job:%s with new cron expression:'%s' ", jobCode, expression));                                     
        scheduleTask.reschedule(expression, jobCode);
        return String.format("Schedule of Job:%s is changed to '%s' ", jobCode, expression);
    }
    
}
