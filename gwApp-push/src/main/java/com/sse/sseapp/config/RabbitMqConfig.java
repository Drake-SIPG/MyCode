package com.sse.sseapp.config;

//import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static com.sse.sseapp.constants.QueueConstants.BULLETIN_TOPIC_NAME;
import static com.sse.sseapp.constants.QueueConstants.TOPIC_QUOTATION;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/4/21 9:42 hanjian 创建
 */
@Configuration
//@EnableRabbit
public class RabbitMqConfig {
//    @Bean
//    public Queue quotationQueue() {
//        return new Queue(TOPIC_QUOTATION);
//    }
//
//    @Bean
//    public Queue bulletinQueue() {
//        return new Queue(BULLETIN_TOPIC_NAME);
//    }
}
