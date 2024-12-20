package aggregator;


import aggregator.dtos.TransactionDto;
import aggregator.entities.Transaction;
import aggregator.services.AggregationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.LimitExceededException;
import javax.naming.ServiceUnavailableException;
import java.util.List;

@RestController
public class AggregationController {
    AggregationService aggregationService;

    AggregationController(AggregationService aggregationService) {
        this.aggregationService = aggregationService;
    }

    @GetMapping("/aggregate")
    public ResponseEntity<List<?>> aggregate(@RequestParam(name = "account") String account) {
        try {
            List<TransactionDto> transactions = this.aggregationService.getTransactions(account);

            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(transactions);
        } catch (LimitExceededException e) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        } catch (ServiceUnavailableException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }
}
