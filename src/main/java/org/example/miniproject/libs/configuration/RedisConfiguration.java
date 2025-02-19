package org.example.miniproject.libs.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

//    @Autowired
//    RedisConnectionFactory redisConnectionFactory;
//
//    @Autowired
//    StringRedisTemplate stringRedisTemplate;
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate()
//    {
//        RedisTemplate<String, Object> template = new RedisTemplate();
//        template.setConnectionFactory(redisConnectionFactory);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setHashValueSerializer(new GenericToStringSerializer<Object>(Object.class));
//        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
//        return template;
//    }
}
