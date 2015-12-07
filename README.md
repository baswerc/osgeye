# osgeye

OSGEye provides a set of managing and monitoring tools for OSGi enabled platforms. Currently a command line console and JMX is supported.

The latest release is v0.0.2. See the installation guide and user guide to get started.

The following open source projects are used by OSGEye:

JLine - This (awesome) open source library is used as the foundation for the command line console.
SLF4J
TestNG
If you have questions or suggestions please contact me at corey.baswell@gmail.com

OSGEye Installation Guide
OSGEye is a set of management tools for OSGi based platforms. Once installed OSGEye features can be accessed from a command line or JMX.

Installing The OSGEye Bundle
Installing the OSGEye server is accomplished by deploying and starting the OSGEye bundle (org.osgeye-${version}.jar) into your OSGi runtime. Once this bundle starts up it will startup the network server that the command line client can connect to. It will also add OSGEye MBeans for JMX management to the platform's default MBeanServer.

Bundle Requirements
The OSGEye bundle requires a 5.0 or greater JVM and a 4.1 or greater OSGi implementation. The following OSGi framework services must be available for the bundle to properly function (the bundle will startup fine without these but they must be available for the first client request):

ConfigurationAdmin
PackageAdmin
StartLevel
The OSGEye bundle has one external requirement (that may not already be supplied by your system bundle) SLF4J. If your OSGI runtime does not already export the SLF4J api packages be sure to install the slf4j-api-1.5.10.jar and slf4j-simple-1.5.10 bundles before you install the OSGEye bundle (these are part of the OSGEye download distribution).

Installing The Command Line Client
The command line client is a self contained executable jar file org.osgeye.console-${version}.jar. To execute this client run the command:

java -jar org.osgeye.console-${version}.jar

Command Line Client Requirements
The OSGEye command line client requires a 5.0 or greater JVM to run in.

OSGEye provides management tools for your OSGi platform. For installation information and platform requirements see the installation guide. OSGEye currently provides remote access for management through a command line client and JMX.

OSGEye Bundle
The server piece of OSGEye is an OSGi bundle that must be installed into your OSGi runtime. Once installed, OSGEye starts up a network server that remote hosts (like the command line client) can connect to. OSGEye bundle supports the following configuration properties:

host - The network host to accept client requests from. If not specified OSGEye will listen on all interfaces.
port- The network port to accept client requests from. If not specified OSGEye will attempt to use the default port 9999.
user - The user name used to authenticate client requests. If both user and password are specified authentication will be activated and clients will be required to pass these credentials before accessing the server services. This does not secure JMX access. If you have activated remote JMX access in your system you must add security independent of OSGEye.
password - The password used to authenticate client requests. If both user and password are specified authentication will be activated and clients will be required to pass these credentials before accessing the server services. This does not secure JMX access. If you have activated remote JMX access in your system you must add security independent of OSGEye.
The OSGEye bundle uses the configuration interfaces provided by the OSGi specification to receive this configuration. At startup, the bundle registers a ManagedService with the service pid:

org.osgeye.server.osgi-${bundle version}

Use the ConfigurationAdmin to inject this service with configuration values. Every time OSGEye receives updated configuration information it restarts the network server with these new parameters (this means all connected clients will be disconnected).

OSGEye Command Line
OSGEye provides a command line interface for interacting remotely with the server. The command line interface is contained within the executable jar file org.osgeye.console-${version}.jar. The command line interface can be started up by running:

java -jar org.osgeye.console-${version}.jar <host> <port> <user> <password>

This executes the main class org.osgeye.console.OSGEyeConsole. The arguments passed in include:

host - The host name of the server to connect to. This argument is required and if it's not provided the console will prompt you for this.
port - The port of the server to connect to. This argument is optional and if not provided the default port 9999 will be used.
user - The user name used to authenticate with the server. If not provided anonymous access will be attempted. If the user name is provided a password must be provided too.
password - The password used to authenticate with the server. If not provided anonymous success will be attempted.
An example login to the server is:

baswerc$ java -jar org.osgeye.console-0.0.1.jar localhost 9999 cbass yoyo    
OSGEye Console v0.0.1
Loading bundles. This may take a second or two...
Bundles are loaded. At anytime enter help for a list of available commands.

cbass@localhost:9999$ 
Once logged in you can execute the help command to see what other commands are available.

cbass@localhost:9999$ help

OSGEye Console, v0.0.2
usage: <command> <options>
Type 'help <command>' for help on a specific command.

Available commands:
    
    Descrptions
        bundles         Prints details on matching bundles.
        configs         Prints configurations.
        exports         Displays the exported package wiring details for matching bundles.
        framework       Prints the current state of the OSGi framework.
        graph           Generates DOT based graphs.
        imports         Displays the imported package wiring details for matching bundles.
        ls              List the matching bundle or service names.
        manifest        Prints the manifest file for matching bundles.
        notifications   Prints OSGi notifications to the console.
        packages        Displays the wiring details for matching packages.
        services        List the matching service interfaces.
        states          List each bundle under its bundle state.
    
    Actions
        install         Installs a bundle on the remote server.
        refreshpacks    Forces the update (replacement) or removal of packages exported by the specified bundles.
        resolve         Resolves the specified bundles that are currently in the INSTALLED state.
        set             Sets OSGi framework and bundle properties.
        start           Starts the matching bundles.
        stop            Stops the matching bundles.
        uninstall       Uninstalls all matching bundles (after confirmation).
    
    Diagnosis
        canresolve      Attempts to determine if a manifest file can resolve against the current system.
        missing         Displays packages for bundles that are not wired.
        unresolved      Attempts to diagnose why bundles in the INSTALLED state weren't resolved.
    
    Miscellaneous
        clear           Clears the screen.
        exit            Exits from the console.
        help            Prints help for all commands.
        reload          Reloads all state stored by the console from the server.
Tab Completion
The first thing the client does after login is to load the current state of the server. Having the full state of the server on the client allows tab completion to be enabled on most of the available commands. This works similar to tab completion in a bash shell where anytime you press tab OSGEye will attempt to complete as much of the remaining operation as it can.

For example, the bundles command prints out details about bundles in the system. It takes as its first argument the bundle name pattern to determine which bundles to print out. At the command prompt if you type the character b and press the tab button the console will automatically fill out the bundles command since that's the only command that starts with b. If you then type org and press tab you might get something like:

cbass@localhost:9999$ bundles org.

org.eclipse.equinox.cm   org.eclipse.osgi         org.osgeye
cbass@localhost:9999$ bundles org.    
The first thing that happened was that the console supplied a . after the org because all bundles in the system that start with org also start with org. It then prints out as options the three bundles in the system that start with org. that you have to choose from. If you narrow down the name more by typing {{{org.eclipse}} and pressing tab you will get:

cbass@localhost:9999$ bundles org.eclipse.

org.eclipse.equinox.cm   org.eclipse.osgi
cbass@localhost:9999$ bundles org.eclipse.
Tab completion works in almost all command scenarios for the console. If your ever unsure what to type next just press tab and see what happens.

Special thanks to the awesome JLine project that makes the tab completion possible.

Output To File
Many of the commands that print back status can optionally have their output written to a file instead of displayed on the console. This is accomplished by placing a > on the end of the command and the path to the file to write to. For example the manifest command prints out the manifest file for matching bundles. If you wanted to print the manifest contents to a file instead of the screen do:

cbass@localhost:9999$ manifest org.osgeye > osgeye.mf      
This puts the contents of the org.osgeye bundle's manifest file into the osgeye.mf file. Unless an absolute path is specified the path will be relative to the directory that console was started in. Once you type the > tab completion will be enabled for viewing what directories and files. Read the help details on each command to see which ones have this feature enabled (almost all the commands in the Description category supports this).

Commands
The console commands are broken up into the following categories:

Descriptions - Commands that are read only that report back the state of the system.
Actions - Commands that attempt to change the state of the system.
Diagnosis - Commands that attempt to determine problems with the system.
Misc - Commands that don't really fall into any of the above categories.
Description Commands
bundles
configs
exports
framework
graph
imports
ls
manifest
notifications
packages
services
states
Action Commands
install
refreshpacks
resolve
set
start
stop
uninstall
Diagnosis Commands
canresolve
missing
unresolved
Miscellaneous Commands
clear
exit
help
reload
OSGEye JMX
When the OSGEye bundle starts up it gets a reference to platform's default MBeanServer (by calling this method) and adds MBeans for monitoring and managing your OSGi platform. OSGEye does not add any type of connectors to this MBeanServer for remote access. Providing remote, secure access to the MBeanServer must be done independently of OSGEye.

OSGEye adds all of its MBeans under the org.osgeye domain. If you connect to a OSGEye managed runtime with JConsole you should see something like:


 
MBeans are provided for the following OSGi types:

The Framework
Bundles
Services
Configuration
Framework MBean
The framework MBean has two readable and writable attributes:

InitialBundleStartLevel? - The start level assigned to newly installed bundles.
StartLevel? - The currently start level of the framework.

 
Bundle MBeans
Every bundle installed in the OSGi runtime will get an associated MBean. The name of the MBean will be the bundle's symbolic name followed by the bundle's version number. Each bundle MBean has the following read only attributes:

ExportedPackages? - A list of packages and version numbers that this bundle exports.
Fragment - Boolean indicator is this bundle is a fragment or not.
Fragments - A list of bundles that are attached to this host bundle.
Headers - A map of strings containing the headers found in the bundle's manifest (see getHeaders()).
Hosts - A list of bundles this fragment bundle is attached to.
Id - The bundle id (see getBundleId()).
ImportedPackages? - A list of packages and version ranges that this bundle imports.
LastModified? - The date this bundle was last modified (see getLastModified()).
Location - The install location of the bundle (see getLocation()).
StartLevel? - The bundle's start level (see getBundleStartLevel()).
State - The bundle's current state. One of:
INSTALLED
RESOLVED
STARTING
ACTIVE
STOPPING
SymbolicName? - The bundle's symbolic name (see getSymbolicName()).
Version - The bundle's version.

 
Each bundle MBean provides the following operations:

start() - Attempts to start the bundle if the bundle is not currently active (see start).
start(int options) - Attempts to start the bundle with the provided options if the bundle is not currently active (see start(int)).
stop() - Attempts to stop the bundle if the bundle is currently active (see stop()).
stop(int optiona) - Attempts to stop the bundle with the provided options if the bundle is currently active (see stop(int)).
resolve() - Attempts to resolve the bundle if the bundle is currently not resolved (see resolveBundles()).
update() - Updates the bundle from either the update bundle location from the bundle's manifest (if available) or the bundle's original location (see update()).
update(String bundleJarLocation) - Updates the bundle from the given path that points to jar file on the server side (see update(InputStream)).
refreshPackages() - Forces an update on packages exported by the given bundle (see refreshPackages()).
uninstall() - Uninstalls the bundle (see uninstall).
viewManifest() - Returns the string content of the manifest file.

 
Service MBeans
Every service registered in the OSGi runtime will get an associated MBean. Underneath the Services folder of the OSGEye domain will be a list of all the interfaces and classes that services are registered with in the system. Underneath each interface or class will be a list of bundles that have registered services with the interface or class. Underneath the bundle name will be a list of the services registered with the service id used as the MBean name.

Every service MBean will have the following attributes:

Bundle - The bundle symbolic name that registered this service.
Id - The service ID.
Interface - The interface or class name this service is registered with.
Pid - The service pid used for configuration.
Ranking - The service ranking.

 
Every service MBean will have the following operations:

description() - The service description.

 
