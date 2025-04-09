package tqs_hw1.meals.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = RedisConfig.class)
@TestPropertySource(properties = {
    "spring.data.redis.host=testhost",
    "spring.data.redis.port=1234"
})
class RedisConfigTest {

    @MockBean
    private RedisConnectionFactory connectionFactory;
    
    @Autowired
    private RedisConfig redisConfig;

    @Test
    void testRedisConnectionFactory() {
        // Use the autowired bean with injected properties
        RedisConnectionFactory factory = redisConfig.redisConnectionFactory();
        assertNotNull(factory);
    }
    
    @Test
    void testRedisTemplate() {
        // Use the autowired bean
        var template = redisConfig.redisTemplate(connectionFactory);
        
        assertNotNull(template);
        assertEquals(connectionFactory, template.getConnectionFactory());
        assertNotNull(template.getKeySerializer());
        assertNotNull(template.getValueSerializer());
    }
}