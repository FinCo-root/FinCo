package com.finCo.hub.service.impls;

import com.finCo.hub.exception.TechnicalException;
import com.finCo.hub.service.NlpService;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.http.HttpClient;

@Service
public class NlpServiceImpl implements NlpService {

    private final static Logger log = LoggerFactory.getLogger(NlpServiceImpl.class);

    private final WebClient webClient;

    @Autowired
    public NlpServiceImpl( WebClient webClient){
        this.webClient = webClient;
    }

    @Override
    public JsonObject getNlp(String serchString) {
        if (serchString == null)
            throw new TechnicalException("Search string is null", HttpStatus.BAD_REQUEST);

        return webClient
                .post()
                .uri("/nlp/predict")
                .retrieve()
                .bodyToMono(JsonObject.class)
                .block();
    }
}
