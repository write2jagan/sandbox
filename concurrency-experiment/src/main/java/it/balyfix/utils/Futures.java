package it.balyfix.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
 public class Futures {

        public static <T> CompletableFuture<T> toCompletable(Future<T> future, Executor executor) {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    return future.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }, executor);
        }
        
        
        public static <T> CompletableFuture<T> toCompletable(Future<T> future) {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    return future.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            },  ForkJoinPool.commonPool() );
        }
        
    }