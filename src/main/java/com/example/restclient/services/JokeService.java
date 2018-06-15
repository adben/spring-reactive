package com.example.restclient.services;

import com.example.restclient.json.JokeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class JokeService {
    private RestTemplate template;
    private Logger logger = LoggerFactory.getLogger(JokeService.class);

    private WebClient client = WebClient.create("http://api.icndb.com");


    private static final String BASE = "http://api.icndb.com/jokes/random?limitTo=[nerdy]";

    @Autowired
    public JokeService(RestTemplateBuilder builder) {
        template = builder.build();
    }

    public String getJokeSync(String first, String last) {
        String url = String.format("%s&firstName=%s&lastName=%s", BASE, first, last);

        // Synchronous call to get the JSON and convert to an object
        JokeResponse response = template.getForObject(url, JokeResponse.class);
        String joke = response.getValue().getJoke();
        logger.info(joke);
        return joke;
    }

    public Mono<String> getJokeAsync(String first, String last) {
        String path = "/jokes/random?limitTo=[nerdy]&firstName={first}&lastName={last}";
        return client.get()
                .uri(path, first, last)
                .retrieve()
                .bodyToMono(JokeResponse.class)
                .map(jokeResponse -> jokeResponse.getValue().getJoke());
    }

}
