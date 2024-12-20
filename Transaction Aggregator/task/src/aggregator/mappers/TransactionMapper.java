package aggregator.mappers;

import aggregator.dtos.TransactionDto;
import aggregator.entities.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public TransactionDto toTransactionDto(Transaction transaction) {
        return new TransactionDto(transaction.getId(), transaction.getServerId(), transaction.getAccount(), transaction.getAmount(), transaction.getTimestamp());
    }

    public Transaction toTransaction(TransactionDto transactionDto) {
        return new Transaction(transactionDto.getId(), transactionDto.getServerId(), transactionDto.getAccount(), transactionDto.getAmount(), transactionDto.getTimestamp());
    }
}
