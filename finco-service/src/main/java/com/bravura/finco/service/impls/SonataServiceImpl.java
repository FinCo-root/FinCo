package com.bravura.finco.service.impls;

import com.bravura.finco.constant.SonataServiceType;
import com.bravura.finco.exception.TechnicalException;
import com.bravura.finco.model.FincoResponse;
import com.bravura.finco.model.GetAccountDetailsResponse;
import com.bravura.finco.model.GetClientResponse;
import com.bravura.finco.service.SonataService;
import com.bravura.finco.utils.ConvertObjectToJson;
import com.bravura.finco.utils.JsonFlatner;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Optional;

@Service
public class SonataServiceImpl implements SonataService {

    @Qualifier("sbs")
    @Autowired
    private final WebClient webClient;

    public SonataServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public FincoResponse callSonataProduct(FincoResponse fincoResponse) {
        /*  calling distribution details service */
        if (fincoResponse.getNlpResponse().getSER().equals(SonataServiceType.CLIENT.getCode())) {
            val body = "{ \"callerDetails\": { \"username\": \"admin\", \"country\": \"IN\", \"language\": \"EN\" }, \"client\": { \"id\": " + fincoResponse.getNlpResponse().getID() + " }, \"includeClientDetails\": true}";
            Optional<GetClientResponse> clientResponse = Optional.ofNullable(this.webClient
                    .post()
                    .uri("/clientService/getClient")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(body))
                    .retrieve()
                    .bodyToMono(GetClientResponse.class)
                    .block());
            Map<String, Object> flattenClientResponse = getStringObjectMap
                    (clientResponse.orElseThrow(() -> new TechnicalException("client response is null")));
            return JsonFlatner.getDataResponse(fincoResponse,flattenClientResponse);
        }
        if (fincoResponse.getNlpResponse().getSER().equals(SonataServiceType.ACCOUNT.getCode())) {
            val body = "{ \"callerDetails\": { \"username\": \"admin\", \"country\": \"IN\", \"language\": \"EN\" }, \"accountIdentifier\": { \"accountNumber\": { \"accountNo\": " + fincoResponse.getNlpResponse().getID() + "} }}";
            Optional<GetAccountDetailsResponse> accountDetailsResponse = Optional.ofNullable(this.webClient
                    .post()
                    .uri("/accountService/getAccount")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(body))
                    .retrieve()
                    .bodyToMono(GetAccountDetailsResponse.class)
                    .block());
            Map<String, Object> flattenClientResponse = getStringObjectMap
                    (accountDetailsResponse.orElseThrow(() -> new TechnicalException("client response is null")));
            return JsonFlatner.getDataResponse(fincoResponse,flattenClientResponse);
        }
        return null;
    }

    /* Utility methods  */
    private static Map<String, Object> getStringObjectMap(Object response) {
        return JsonFlatner
                .mapToFlat(
                        ConvertObjectToJson.convertToJson(response
                        ));
    }
}
