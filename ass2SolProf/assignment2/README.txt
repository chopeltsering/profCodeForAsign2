Bryan Waite 100647326
Tariq Zaid 100679266

In addition to the source code, two executable jars, Client.jar and
Server.java have been included.  Since javac has a terrible
understanding of non default class paths and packages, building the
source from the command line is not recommended, and information about
how to do this is not provided.  As long as you run the jars with
"java -jar JARNAME" followed by any arguments you want to pass to the
programs, you will be fine

First, make sure the provided server.config and handler.config files are in the
same folder as the Server.jar. Then start the server by running "java -jar Server.jar".

After this, you can start the client. You have two choices here. Either run with
a configuration file such as the client.config I've provided, like this
"java -jar Client.jar -c client.config", or provide a name, ip and port on the command
line like this "java -jar Client.jar -name Bryan -ip 127.0.0.1 -port 8080".

If you want to change the protocol from Object streams to HTTP, you'll
have to edit the server.config file. Uncomment the HttpAcceptor line
and comment the ObjectAcceptor out.  Then stop the server and start it
back up.

The lamport numbers, and chat timestamps are located in the chat.log file.
