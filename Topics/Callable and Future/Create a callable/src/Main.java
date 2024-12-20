import java.util.Scanner;
import java.util.concurrent.Callable;

class CallableUtil {
    public static Callable<String> getCallable() {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();

        return new Callable<String>() {
            public String call() throws Exception {
                return s;
            }
        };
    }
}