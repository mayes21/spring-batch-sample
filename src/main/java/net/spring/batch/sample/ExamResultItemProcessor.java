package net.spring.batch.sample;

import net.spring.batch.sample.model.ExamResult;
import org.springframework.batch.item.ItemProcessor;

/**
 * Created by amabb on 08/02/16.
 *
 * ItemProcessor is Optional, and called after item read but before item write. It gives us the opportunity to perform a business logic on each item.In our case, for example,
 * we will filter out all the items whose percentage is less than 60.So final result will only have records with percentage >= 60.
 */
public class ExamResultItemProcessor implements ItemProcessor<ExamResult, ExamResult> {


    public ExamResult process(ExamResult examResult) throws Exception {
        System.out.println("Processing result :"+examResult);

        /*
         * Only return results which are equal or more than 60%
         *
         */
        if(examResult.getPercentage() < 60){
            return null;
        }

        return examResult;
    }
}

