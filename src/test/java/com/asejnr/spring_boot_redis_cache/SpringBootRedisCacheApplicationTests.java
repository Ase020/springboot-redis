package com.asejnr.spring_boot_redis_cache;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class SpringBootRedisCacheApplicationTests {

    @Test
    void contextLoads() {}
}
