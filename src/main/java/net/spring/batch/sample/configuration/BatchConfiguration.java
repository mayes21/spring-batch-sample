package net.spring.batch.sample.configuration;

import net.spring.batch.sample.ExamResultItemProcessor;
import net.spring.batch.sample.model.ExamResult;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Created by amabb on 16/02/16.
 */

@Configuration
@EnableBatchProcessing
@PropertySource(value = { "classpath:context-datasource.xml" })
public class BatchConfiguration {

    @Autowired
    private Environment environment;

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Bean
    public ItemReader<ExamResult> reader() {
        FlatFileItemReader<ExamResult> reader = new FlatFileItemReader<ExamResult>();
        reader.setResource(new ClassPathResource("ExamResult.txt"));
        reader.setLineMapper(new DefaultLineMapper<ExamResult>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "studentName", "dob", "percentage" });
                setDelimiter("|");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<ExamResult>() {{
                setTargetType(ExamResult.class);
            }});
        }});
        return reader;
    }

    @Bean
    public ItemProcessor<ExamResult, ExamResult> processor() {
        return new ExamResultItemProcessor();
    }

    @Bean
    public ItemWriter<ExamResult> writer(DataSource dataSource) {
        JdbcBatchItemWriter<ExamResult> writer = new JdbcBatchItemWriter<ExamResult>();
        writer.setSql("insert into EXAM_RESULT(STUDENT_NAME, DOB, PERCENTAGE) values (?, ?, ?)");
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<ExamResult>());
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public Job examResultJob() {
        return jobs.get("examResultJob")
                .start(step1())
                .build();
    }

    @Bean
    public Step step1( ) {
        return steps.get("step1")
                .<ExamResult, ExamResult> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer(dataSource()))
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
        return dataSource;
    }
}