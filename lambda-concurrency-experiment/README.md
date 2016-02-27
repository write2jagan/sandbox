My own experimental project to familiarize "new" concurrency API introduced with Java8.
After is see number of method of CompletableFuture I'll start to think that was better to play a little with it and
try new framework/tools that port "reactive" ideas on jvm.

Also addeed a list of general purpose patterns used in concurrency. I retrieve that from 
`Pattern-Oriented Software Architecture, Patterns for Concurrent and Networked Objects, Volume 2`


Before that Jdk introduced CompletableFuture there was 
https://code.google.com/p/guava-libraries/wiki/ListenableFutureExplained from guava library and in Scala languanges there was onComplete method on Feature

The concept behind CompletableFuture can be generalized from a single value to stream of data using reactive programming instead RxJava introduced a Observable pattern for a stream of event or a sequence. 

In both cases the concept between Stream and Observable are very similar. for example map,filter,reduce ecc.ecc.
A good definition from of RXJava can be that ReactiveX Observables, on the other hand, are intended for composing flows and sequences of asynchronous data.

Obviously there are a lot of Reactor or similar Reactive Streams solution, I try to follow real world 
tendencies and try go ahead.
