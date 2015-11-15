Globlal Transaction or XA.


During integration phase can be happen that we need to span multiple resource in the same transaction.
A tipical example is when your read a Jms message from a queue, trasform message data in a bo, and save that on Database.

This work can be do  with application container like Geromimo, Weblogic, Webphere ecc.ecc but can be do outside Java EE container 
simple using a JTA transaction provider.

An XA transaction involves a coordinating transaction manager, with one or more databases (or other resources, like JMS) all involved in a single global transaction. 
Non-XA transactions have no transaction coordinator, and a single resource is doing all its transaction work itself 
(this is sometimes called local transactions). 

XA transactions come from the X/Open group specification on distributed, global transactions. JTA includes the X/Open XA spec, in modified form.
The Transaction Manager coordinates all of this through a protocol called Two Phase Commit (2PC). 
This protocol also has to be supported by the individual resources.

On the wire we can find different solution that can be use to support jta
here a simple list:

* JOTOM
* Geronimo (Karaf is shipped with this)
* Atomikos
* Bitronix

The key components of XA functionality from user point of view includes:

* XA data sources
XA data source is an extension of connection pool data  where there is an instance for each resorce.
Typically a middleware resource provide out of the box this extension.
XA data sources produce XA connections.
* XA connections
An XA connection instance corresponds to a single DB session, although the session can be used in sequence by multiple logical connection instances, as with pooled connection instances.
You will typically get an XA connection instance from an XA data source instance in your middle-tier software. You can get multiple XA connection instances from a single XA data source instance if the distributed transaction will involve multiple sessions in the same database.
* XA resources
This is used by a transaction manager in coordinating the transaction branches of a distributed transaction.
there is a one-to-one correlation between XAResource instances and Middleware sessions.
In a typical scenario, the middle-tier component will hand off XAResource instances to the transaction manager, for use in coordinating distributed transactions.
* Transaction IDs
identify transaction branches. Each ID includes a transaction branch ID component and a distributed transaction ID component

In The Java Transaction API consists of three elements: a high-level application
transaction demarcation interface, a high-level transaction manager interface intended
for an application server, and a standard Java mapping of the X/Open XA protocol
intended for a transactional resource manager. This chapter specifies each of these
elements in details.







resource from:
http://docs.oracle.com/cd/E11882_01/java.112/e16548/xadistra.htm#JJDBC28845
