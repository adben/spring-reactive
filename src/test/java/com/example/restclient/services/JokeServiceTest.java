package com.example.restclient.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JokeServiceTest {

    private Logger logger = LoggerFactory.getLogger(JokeServiceTest.class);
    @Autowired
    private JokeService service;

    @Test
    public void getJokeSync() {
        String joke = service.getJokeSync("Adolfo", "Benedetti");
        logger.info(joke);
        assertTrue(joke.contains("Adolfo") || joke.contains("Benedetti"));
    }

    @Test
    public void getJokeAsync() {
        String joke = service.getJokeAsync("Adolfo", "Benedetti")
                .doOnNext(logger::info)
                .block(Duration.ofSeconds(2));
        assertTrue(joke.contains("Adolfo") || joke.contains("Benedetti"));
    }

    @Test
    public void useStepVerifier() {
        StepVerifier.create(
                service.getJokeAsync("Adolfo", "Benedetti")
                        .doOnNext(logger::info))
                .assertNext(joke -> {
                    assertTrue(joke.contains("Adolfo") || joke.contains("Benedetti"));
                })

                .verifyComplete();
    }

}