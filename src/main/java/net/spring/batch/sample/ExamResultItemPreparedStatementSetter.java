package net.spring.batch.sample;

import net.spring.batch.sample.model.ExamResult;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by amabb on 16/02/16.
 */
public class ExamResultItemPreparedStatementSetter implements ItemPreparedStatementSetter<ExamResult> {
    @Override
    public void setValues(ExamResult result, PreparedStatement ps) throws SQLException {
        ps.setString(1, result.getStudentName());
        ps.setDate(2, new java.sql.Date(result.getDob().toDate().getTime()));
        ps.setDouble(3, result.getPercentage());
    }
}
