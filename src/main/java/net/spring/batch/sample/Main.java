package net.spring.batch.sample;

import net.spring.batch.sample.configuration.BatchConfiguration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by amabb on 08/02/16.
 */
public class Main {

    public static void main(String args[]){

        //ApplicationContext context = new ClassPathXmlApplicationContext("spring-batch-context.xml");

        ApplicationContext context = new AnnotationConfigApplicationContext(BatchConfiguration.class);

        JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
        Job job = (Job) context.getBean("examResultJob");

        try {
            JobExecution execution = jobLauncher.run(job, new JobParameters());
            System.out.println("Job Exit Status : "+ execution.getStatus());

        } catch (JobExecutionException e) {
            System.out.println("Job ExamResult failed");
            e.printStackTrace();
        }
    }
}
