package com.bravura.finco.service;

import com.bravura.finco.model.NLPResponse;

public interface SonataService {
    < T extends Object> T callSonataProduct(NLPResponse nlpResponse);
}
