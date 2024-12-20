class CounterThread extends Thread {

    @Override
    public void run() {
        long counter = 0;

        while (true) {
            counter++;
        }
    }
}