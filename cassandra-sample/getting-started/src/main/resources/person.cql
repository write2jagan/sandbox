CREATE KEYSPACE mykeyspace WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

CREATE TABLE person (
   id varchar,
   name varchar,
   age int,
   credit_change_date timeuuid,
   PRIMARY KEY (id)
);