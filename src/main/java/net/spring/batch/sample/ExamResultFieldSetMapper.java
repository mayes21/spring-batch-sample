package net.spring.batch.sample;


import net.spring.batch.sample.model.ExamResult;
import org.joda.time.LocalDate;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * Created by amabb on 08/02/16.
 */

/**
 * FieldSetMapper is responsible for mapping each field form the input to a domain object
 */

public class ExamResultFieldSetMapper implements FieldSetMapper<ExamResult> {

    @Override
    public ExamResult mapFieldSet(FieldSet fieldSet) throws BindException {
        ExamResult result = new ExamResult();
        result.setStudentName(fieldSet.readString(0));
        result.setDob(new LocalDate(fieldSet.readDate(1,"dd/MM/yyyy")));
        result.setPercentage(fieldSet.readDouble(2));
        return result;
    }

}