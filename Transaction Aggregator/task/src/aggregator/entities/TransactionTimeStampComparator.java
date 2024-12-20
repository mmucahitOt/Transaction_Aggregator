package aggregator.entities;

import java.time.LocalDateTime;
import java.util.Comparator;

public class TransactionTimeStampComparator implements Comparator<Transaction> {
    @Override
    public int compare(Transaction o1, Transaction o2) {
        LocalDateTime dateTime = LocalDateTime.parse(o1.getTimestamp());
        LocalDateTime dateTime2 = LocalDateTime.parse(o2.getTimestamp());
        if (dateTime.isAfter(dateTime2)) {
            return -1;
        } else if (dateTime.isBefore(dateTime2)) {
            return 1;
        }
        return 0;
    }
}
