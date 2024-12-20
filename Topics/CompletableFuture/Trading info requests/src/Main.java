import java.util.concurrent.*;

public class Main {
    public static CompletableFuture<Object> getFirstInfo() {
        var apple = CompletableFuture.supplyAsync(() -> requestInfo("AAPL"));
        var microsoft = CompletableFuture.supplyAsync(() -> requestInfo("MSFT"));
        var google = CompletableFuture.supplyAsync(() -> requestInfo("GOOG"));

        // edit code above
        CompletableFuture<Object> firstAssetFuture = CompletableFuture.anyOf(apple, microsoft, google);
        return firstAssetFuture;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var firstAssetFuture = getFirstInfo();
        System.out.println(firstAssetFuture.get());
        System.out.println("Is first asset is done: " + firstAssetFuture.isDone());
    }

    private static final int AAPL_VALUE = 500;
    private static final int MSFT_VALUE = 1000;
    private static final int GOOG_VALUE = 1500;

    public static String requestInfo(String asset) {
        var time = switch (asset) {
            case "AAPL" -> AAPL_VALUE;
            case "MSFT" -> MSFT_VALUE;
            case "GOOG" -> GOOG_VALUE;
            default -> 0;
        };

        try {
            Thread.sleep(time);
            return "Info about: " + asset;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}