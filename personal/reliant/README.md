### I started developing this project when I was 18 years old. This project is no longer in development. ###

Reliant is a Java cheat that uses JNI to communicate with CS:GO.

### What is this repository for? ###

* This repository is for hosting Reliant's sourcecode.
* Version: build 4

### How do I get set up? ###

* You will need to setup a makefile project with MinGW as your compiler. Make sure to use JDK 1.6 as your Java compiler. You will need both the x86 and x64 versions of MinGW, since the natives support both.
* You should configure your compiler flags to include -Ofast, enable Aero on your PC, have Xbox DVR disabled, and launch with "-Xmx1G -Xms1G -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:MaxGCPauseMillis=32 -XX:G1HeapRegionSize=16M -XX:+AggressiveOpts -XX:+UseStringCache -XX:ParallelGCThreads=20 -XX:ConcGCThreads=5". This way, the overlay will not lag.
* Dependencies include eric-generic, valve-file-parsing, synthetica (cracked), and all of their themes.
* You should always test both the x86 and x64 natives. This means you should have the 32-bit JDK and the 64-bit version.
* To deploy Reliant, export it as a runnable JAR file and include the natives in a folder called "natives". Make sure to include "Djava.library.path=natives" in your VM launch options.

### Contribution guidelines ###

* Try to keep test code in the test sourcecode folder.
* Please keep your code review constructive.
* Please follow the Google C++ style guide (https://google.github.io/styleguide/cppguide.html) when writing native code.
* Favor writing Java code over C++ code.

### Who do I talk to? ###

* Eric McDonald
* Other contributors