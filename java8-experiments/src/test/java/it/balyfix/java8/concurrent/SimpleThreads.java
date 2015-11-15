package it.balyfix.java8.concurrent;

public class SimpleThreads
{

    // Display a message, preceded by
    // the name of the current thread
    static void threadMessage(String message)
    {
        String threadName = Thread.currentThread().getName();
        System.out.format("%s: %s%n", threadName, message);
    }
    
    private static Runnable messageLoop()
    {
        return () -> 
        {
            String importantInfo[] = {
                "Mares eat oats",
                "Does eat oats",
                "Little lambs eat ivy",
                "A kid will eat ivy too" };
            try
            {
                for (int i = 0; i < importantInfo.length; i++)
                {
                    // Pause for 4 seconds
                    Thread.sleep(4000);
                    // Print a message
                    threadMessage(importantInfo[i]);
                }
            }
            catch (InterruptedException e)
            {
                threadMessage("I wasn't done!");
            }
            
        };
    }
    
    

    public static void main(String... args) throws InterruptedException
    {
       
        long patience = 1000 * 60 * 60;

        threadMessage("Starting MessageLoop thread");
        long startTime = System.currentTimeMillis();
        Thread t = new Thread(messageLoop());
        t.setName("My thread");
        t.start();

        threadMessage("Waiting for MessageLoop thread to finish");
        
        while (t.isAlive())
        {
            threadMessage("Still waiting...");
            t.join(1000);
            if (((System.currentTimeMillis() - startTime) > patience) && t.isAlive())
            {
                threadMessage("Tired of waiting!");
                t.interrupt();
                t.join();
            }
        }
        threadMessage("Finally!");
    }
}