First Question:
When the run() method is called directly in the main() function, no new thread is created—the run() method simply executes like a regular method call within the current thread. However, when the start() method is called on the t2 object, which is a Thread instance, it creates a new thread and then internally calls the run() method. This allows the code in run() to execute concurrently in a separate thread.

Second Question:
The reason only one line of "Daemon thread running..." appears in the output is because the daemon thread is the last thread still alive just before the program terminates. If you remove thread.setDaemon(true), the thread becomes a regular (non-daemon) thread, which will keep the program running until it finishes. Daemon threads, on the other hand, do not prevent the JVM from exiting once all user threads have finished. This is somewhat similar to how garbage collection works in some programming languages, where background tasks are terminated when no longer needed.

Third Question:
The thread created here behaves like a normal thread, and the output is a single line: "thread.setDaemon(true)". The syntax used is a lambda expression, which is a shorthand for defining a Runnable without needing to create a separate class. This is a convenient way to define and start a thread with minimal boilerplate code.