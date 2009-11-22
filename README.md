
Cross-platform serial to tcp/ip proxy server
============================================

The server relays bytes back and forth from a connection to a hardware port to an internet socket connection. This is especially useful for software like Flash which has an easy time making socket connections and a hard time making serial connections.

Usage
-----

Most of the time you will run the server this way:

        $ java -jar serial-server.jar --serial-port /dev/ttyUSB0 --serial-speed 9600 --listen-port 9000

By running the application with no params you will get a list of available ports.

        $ java -jar serial-server.jar -?
        Nov 22, 2009 11:12:42 AM serialserver.Main main
        INFO: Parsing command line arguments.
        Option                                  Description
        ------                                  -----------
        -?, -h, --help                          show help 
        --debug                                 display debug info
        --listen-port <Integer>                 server listen port
        --serial-port                           serial port
        --serial-speed <Integer>                serial port speed
        Nov 22, 2009 11:12:42 AM serialserver.Main main
        SEVERE: Expecting serial port as a command line argument.
        Nov 22, 2009 11:12:42 AM serialserver.Main listAvailablePorts
        INFO: Listing all available serial ports
        Experimental:  JNI_OnLoad called.
        Stable Library
        =========================================
        Native lib Version = RXTX-2.1-7
        Java lib Version   = RXTX-2.1-7
        /dev/ttyUSB1 null
        /dev/ttyS1 null
        /dev/ttyS0 null


Installation
------------

You can download a ready to use application from the [download section](http://github.com/andreisavu/serial-server/downloads). In order to make it work you need to install some files used for java serial communication.  

Java Comm Classes Installation
------------------------------

You can download a recent rxtx version from [here (rxtx-2.1.7)](http://cloud.github.com/downloads/andreisavu/serial-server/rxtx-2.1-7-bins-r2.zip). Follow the install instructions from the archive. 

If you want to use sun java comm classes you can downloaded them from [here](http://cloud.github.com/downloads/andreisavu/serial-server/commapi-win32.zip). 

In order to install it follow the steps bellow:
1. comm.jar or RXTXcomm.jar goes in c:\...jre\lib\ext\
2. win32com.dll or rxtxSerial.dll goes in c:\...jre\bin\
3. javax.comm.properties goes in c:\...jre\lib\ 

Have fun!

