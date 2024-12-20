package aggregator.services;


import com.sun.net.httpserver.Headers;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PingService {

    public String ping() {
        RestTemplate restTemplate = new RestTemplate();

        Headers headers = new Headers();
        headers.set("Content-Type", "text/plain");

        String PING_URL = "http://localhost:8889/ping";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(PING_URL, String.class);

        return responseEntity.getBody();
    }
}
