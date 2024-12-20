import java.util.*;
import java.util.concurrent.*;


class FutureUtils {

    public static int howManyIsDone(List<Future> items) {
        return items.stream()
                .map(future -> future.isDone() ? 1 : 0) // Map to 1 if done, otherwise 0
                .reduce(0, Integer::sum); // Sum up all the 1s
    }

}