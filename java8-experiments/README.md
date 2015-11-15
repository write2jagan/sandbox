My own experimental project to familiarize java 8.

I Compared old style programming with new style using lambdas expression with third-party framework like Spring

Then a play to create a DSL. 
The first thing to do in a project that in need to understand is model, with out that is impossible, IMHO, hope that the project is successful.
In DSL world Semantic Model as very close  with Domain Models and that should be designed around the purpose of the DSL.

For more deeper analysis of the problem can be found in chapter 11 on Martin Fowler's DSL book.

In the same book is describe 4 approach different approaches for creating Internal DSLs
* Method Chaining
* Functional Sequence
* Nested Functions
* Lambda Expressions/Closures

I implement Method chain (my favorite) and with Lambda Expressions.
The second one need to be refactoring cause has to much nested method and turn into 
"pyramid of doom" 


An interesting feature in concurrent API is Completable feature. In google Guava library and Spring
framework this concept was already present but is NOT bad to have it in java Platform. 
