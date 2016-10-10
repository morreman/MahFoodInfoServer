# About this project

This project is a simple example of a Java application accessing a SQLite
database. There is ABSOLUTELY NO WAY you should run this in a real project, as
it is really, really insecure. Just don't do it. Learn from it, but that's all.

The later versions (from 1.1.0) also showcases how to expose the database as a
REST API using the Spark framework. As such, Java 8 or later is required.

# How do I build this project?

You can easily build the code directly from within your IDE of choice (I'm
personally rather fond of [Eclipse](http://www.eclipse.org)), using its build
features (in Eclipse, right click the project and select *Run As* ->
*Maven build*, type *package* into the *Goals* field, then *Run*). If you
prefer doing stuff more old school, you can use the command line to build the
project as well. To do so, you need to have Maven installed on your machine.
Navigate to your project location. Then, simply type

    mvn package

in your terminal. Your generated JAR files can be found as
*./target/java-backend-1.1.0.jar* and
*./target/java-backend-1.1.0-with-dependencies.jar*.

# How do I run this project?

The easiest way of running the code is directly from within your IDE of choice,
simply by hitting the *Run* button (in Eclipse, that is). If you prefer running
your code from the command line, navigate to the project location and type

    java -jar target/java-backend-1.1.0-with-dependencies.jar

Don't forget to kill the process when done (using the stop button in the
Eclipse console, or by hitting ctrl-c in the terminal).