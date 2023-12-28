package taskflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import taskflow.mapper.TagsMapper;

@SpringBootApplication
//@ComponentScan(basePackages = "taskflow.mapper")
public class TaskFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskFlowApplication.class, args);
    }

//    @Bean
//    public TagsMapper tagsMapper() {
//        return TagsMapper.INSTANCE;
//    }

}
