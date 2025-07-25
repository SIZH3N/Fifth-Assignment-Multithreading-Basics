public class StartVsRun {
    static class MyRunnable implements Runnable {
        public void run() {
            System.out.println("Running in: " + Thread.currentThread().getName());
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new MyRunnable(), "Thread-1");
        System.out.println("Calling run()");
        t1.run();
        Thread.sleep(100);

        Thread t2 = new Thread(new MyRunnable(), "Thread-2");
        System.out.println("Calling start()");
        t2.start();
    }
}
