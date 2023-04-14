package com.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.scheduler.task.ScheduleTask;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableScheduling
public class Bootstrap {

	@Autowired
    private ScheduleTask scheduleTask;

	public static void main(String[] args) {
		SpringApplication.run(Bootstrap.class, args);
		System.out.println("Scheduler Application Bootstrapped...");
	}

	@PostConstruct
	public void startUpTask(){
		scheduleTask.loadPredefinedTasks();
	}
}
