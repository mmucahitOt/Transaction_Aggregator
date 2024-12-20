package aggregator.services;

import aggregator.entities.Transaction;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.naming.LimitExceededException;
import javax.naming.ServiceUnavailableException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Component
@EnableAsync
public class AccountService {
//    public List<Transaction> getTransactionsOfAccount(String account) throws LimitExceededException, ServiceUnavailableException {
//        String AccountUrl_URL_1 = "http://localhost:8888/transactions";
//        String AccountUrl_URL_2 = "http://localhost:8889/transactions";
//
//        CompletableFuture<List<Transaction>> transactionFuture_1 = this.sendTransactionsRequest(account, AccountUrl_URL_1);
//        CompletableFuture<List<Transaction>> transactionFuture_2 = this.sendTransactionsRequest(account, AccountUrl_URL_2);
//
//        List<Transaction> transactions_1 = null;
//        List<Transaction> transactions_2 = null;
//
//        try {
//            transactions_1 = transactionFuture_1.get();
//            transactions_2 = transactionFuture_2.get();
//
//        } catch (ExecutionException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        List<Transaction> transactions = new ArrayList<>();
//        if (transactions_1 !=null) {
//            transactions.addAll(transactions_1);
//        }
//        if (transactions_2 != null) {
//            transactions.addAll(transactions_2);
//        }
//        transactions.sort(new TransactionTimeStampComparator());
//
//        return transactions;
//    }

    @Async
    public CompletableFuture<List<Transaction>> getTransactionsOfAccountFromService1(String account) throws LimitExceededException, ServiceUnavailableException {
        String AccountUrl_URL_1 = "http://localhost:8888/transactions";

        return CompletableFuture.completedFuture(getTransactions(account, AccountUrl_URL_1));
    }


    @Async
    public CompletableFuture<List<Transaction>> getTransactionsOfAccountFromService2(String account) throws LimitExceededException, ServiceUnavailableException {
        String AccountUrl_URL_2 = "http://localhost:8889/transactions";

        return CompletableFuture.completedFuture(getTransactions(account, AccountUrl_URL_2));
    }

    private List<Transaction> getTransactions(String account, String accountUrl_URL) throws LimitExceededException, ServiceUnavailableException {
        List<Transaction> transactions = this.sendTransactionsRequest(account, accountUrl_URL);

        return transactions;
    }

    protected List<Transaction> sendTransactionsRequest(String account, String url) throws LimitExceededException, ServiceUnavailableException {
        ResponseEntity<List<Transaction>> responseEntity = null;
        for (int numberOfRequests = 0; numberOfRequests < 5;) {
            try {
                responseEntity = this.sendGetTransactionRequest(account, url);
                if (responseEntity.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS || responseEntity.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                    numberOfRequests = numberOfRequests + 1;
                } else {
                    return responseEntity.getBody();
                }
            } catch (Exception e) {
                numberOfRequests = numberOfRequests + 1;
            }
        }
        assert responseEntity != null;
        if (responseEntity.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
            throw new ServiceUnavailableException("SERVICE UNAVAILABLE");
        } else if (responseEntity.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
            throw new LimitExceededException("TOO MANY REQUESTS");
        }
        return responseEntity.getBody();
    }

    protected ResponseEntity<List<Transaction>> sendGetTransactionRequest(String account, String url) {

        RestTemplate restTemplate = new RestTemplate();

        URI uri = UriComponentsBuilder.fromUriString(url)
                .queryParam("account", account)
                .build()
                .toUri();

        return restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<Transaction>>() {
        });
    }
}
