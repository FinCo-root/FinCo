package com.bravura.finco.controller;

import com.bravura.finco.model.FincoResponse;
import com.bravura.finco.service.NLPService;
import com.bravura.finco.utils.JsonFlatner;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping(value="/api",produces="application/json")
public class SearchController {

    private final NLPService nlpService;
    public SearchController(NLPService nlpService) {
        this.nlpService = nlpService;
    }

    @PostMapping("/search")
    public FincoResponse postFromFlask(@RequestBody String text){
        return nlpService.getNlp(text);
    }

    @PostMapping("/flatMapToJson")
    public Object mapToString(@RequestBody String jsonObject){
        return JsonFlatner.mapToFlat(jsonObject);
    }
}

