package com.plancana.aeroguardian.base.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.queue.name}")
    private  String queue;

    @Value("${rabbitmq.routingkey.name}")
    private  String routing_key;

    @Bean
    public TopicExchange topicExchange (){
        return new TopicExchange(exchange);
    }

    @Bean
    public Queue queue (){
        return  new Queue(queue);
    }

    @Bean
    public Binding binding (){
        return BindingBuilder
                .bind(queue())
                .to(topicExchange())
                .with(routing_key);
    }

    // tells the rabbitMQ to send data as JSON
    @Bean
    public MessageConverter jsonMessageConverter (){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }


}
