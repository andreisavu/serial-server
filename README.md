
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

