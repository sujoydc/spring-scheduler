package com.scheduler.task;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**  Cron expression details: 
     
        +-------------------- second (0 - 59)
        |  +----------------- minute (0 - 59)
        |  |  +-------------- hour (0 - 23)
        |  |  |  +----------- day of month (1 - 31)
        |  |  |  |  +-------- month (1 - 12)
        |  |  |  |  |  +----- day of week (0 - 6) (Sunday=0 or 7)
        |  |  |  |  |  |  +-- year [optional]
        |  |  |  |  |  |  |
        *  *  *  *  *  *  * command to be executed 

     */


@Component
public class ScheduleTask {
    
    @Autowired
    private TaskScheduler taskScheduler;
    
    private Map<String, ScheduledFuture<?>> scheduledFutureMap;

    public void loadPredefinedTasks(){
        //TODO: Some database call to load but for this POC I would hardcode
        scheduledFutureMap = new HashMap<>();
        String cronPattern = "*/%s * * * * *";
        for(int i = 1; i <= 3; i++){
            String jobCode = i + ""; // stupid way to convert to String as my jobCode is String
            ScheduledFuture<?> tempFuture = taskScheduler.schedule(() -> myScheduledMethod(jobCode),
                createCronTrigger(String.format(cronPattern, jobCode)));
            scheduledFutureMap.put(jobCode, tempFuture);
        }

    }

    public void myScheduledMethod(String jobCode) {
        System.out.println(String.format("This is scheduled for jobCode:%s",jobCode));
    }

    private CronTrigger createCronTrigger(String cronPattern) {
        System.out.println(String.format("createCronTrigger():%s", cronPattern));
        try {
            return new CronTrigger(cronPattern);
        } catch (IllegalArgumentException |  ParseException e) {
            throw new IllegalArgumentException("Invalid cron expression: " + cronPattern, e);
        }
        
    }
    
    public void cancelScheduledMethod(String jobCode) {
        System.out.println(String.format("cancelScheduledMethod() for jobCode:%s", jobCode));
        if (scheduledFutureMap != null && scheduledFutureMap.get(jobCode) != null) {
            scheduledFutureMap.get(jobCode).cancel(true);
        }
    }
    
    public void reschedule(String cronPattern, String jobCode) {
        System.out.println(String.format("reschedule() called for jobCode:%s and pattern:%s", jobCode, cronPattern));
        cancelScheduledMethod(jobCode);

        if(scheduledFutureMap == null){
            scheduledFutureMap = new HashMap<>();
        }

        if(scheduledFutureMap.get(jobCode) != null){
            ScheduledFuture<?> tempFuture = scheduledFutureMap.get(jobCode);
            tempFuture = taskScheduler.schedule(() -> myScheduledMethod(jobCode),
                createCronTrigger(cronPattern));
            scheduledFutureMap.put(jobCode, tempFuture);      
        }else{
            ScheduledFuture<?> tempFuture = taskScheduler.schedule(() -> myScheduledMethod(jobCode),
                createCronTrigger(cronPattern));
            scheduledFutureMap.put(jobCode, tempFuture);    
        }

        System.out.println(String.format("Reschedule() finished for jobCode:%s and pattern:%s", jobCode, cronPattern));        
    }

}
