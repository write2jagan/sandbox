package it.balyfix;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SpringJtaWorker
{

    private static int TX_COUNT = 1;

    private static int POOL_SIZE = 1;

    private static int BATCH_SIZE = 1;

    public static void main(String[] args) throws Exception
    {
        if (args.length >= 1)
        {
            TX_COUNT = Integer.parseInt(args[0]);
        }
        if (args.length >= 2)
        {
            POOL_SIZE = Integer.parseInt(args[1]);
        }
        if (args.length >= 3)
        {
            BATCH_SIZE = Integer.parseInt(args[2]);
        }

        AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("config/beans.xml");
        ctx.registerShutdownHook();
        final UserFacade userFacade = (UserFacade) ctx.getBean("UserFacade");

        Runtime.getRuntime().addShutdownHook(new Chrono());

        ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
        for (int i = 0; i < TX_COUNT; i++)
        {
            Runnable runnable = new Runnable()
            {

                @Override
                public void run()
                {
                    try
                    {
                        userFacade.createUser("some user name", BATCH_SIZE);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            };
            pool.submit(runnable);
        } // for

        pool.shutdown();
        pool.awaitTermination(360, TimeUnit.SECONDS);
        System.exit(0);
    }

    static class Chrono extends Thread
    {

        long before;

        public Chrono()
        {
            before = System.currentTimeMillis();
        }

        @Override
        public void run()
        {
            long after = System.currentTimeMillis();
            long runTime = after - before;
            double perSec = (TX_COUNT / (double) runTime) * 1000;

            System.out.println("transactions: " + TX_COUNT);
            System.out.println("threads: " + POOL_SIZE);
            System.out.println("batch: " + BATCH_SIZE);
            System.out.println("run time: " + runTime + "ms");
            System.out.println(perSec + " tx/s");
        }
    }

}