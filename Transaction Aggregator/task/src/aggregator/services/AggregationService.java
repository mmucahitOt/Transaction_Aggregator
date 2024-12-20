package aggregator.services;

import aggregator.dtos.TransactionDto;
import aggregator.entities.Transaction;
import aggregator.entities.TransactionTimeStampComparator;
import aggregator.mappers.TransactionMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.naming.LimitExceededException;
import javax.naming.ServiceUnavailableException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class AggregationService {

    private PingService pingService;
    private AccountService accountService;
    private TransactionMapper transactionMapper;

    public AggregationService(PingService pingService, AccountService accountService, TransactionMapper transactionMapper) {
        this.pingService = pingService;
        this.accountService = accountService;
        this.transactionMapper = transactionMapper;
    }
    @Cacheable(cacheNames = "aggregate")
    public List<TransactionDto> getTransactions(String account) throws LimitExceededException, ServiceUnavailableException {
        try {

            CompletableFuture<List<Transaction>> transactionFuture_1 =  this.accountService.getTransactionsOfAccountFromService1(account);
            CompletableFuture<List<Transaction>> transactionFuture_2 = this.accountService.getTransactionsOfAccountFromService2(account);

            List<Transaction> transactions_1 = transactionFuture_1.get();
            List<Transaction> transactions_2 = transactionFuture_2.get();

            return this.combineTransactions(transactions_1, transactions_2).stream().map(this.transactionMapper::toTransactionDto).toList();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String sendPingRequest() {
        return pingService.ping();
    }

    private List<Transaction> combineTransactions(List<Transaction> transactions_1, List<Transaction> transactions_2) {
        List<Transaction> transactions = new ArrayList<>();
        if (transactions_1 !=null) {
            transactions.addAll(transactions_1);
        }
        if (transactions_2 != null) {
            transactions.addAll(transactions_2);
        }
        transactions.sort(new TransactionTimeStampComparator());

        return transactions;
    }
}
