# README #

### I started developing this project when I was 13 years old. I have re-written it a couple times over the years. (original link: https://bitbucket.org/eric_ptr/minecraft-forge-lanius) ###

### What is this repository for? ###

* This repository is for hosting Lanius's SRC
* Version: 3.9

### How do I get set up? ###

* This repository is for the developers of Lanius
* Configuration: The configuration files can be found in <your_minecraft_directory>/lanius and <your_minecraft_directory>/config
* Dependencies: forge-1.12.2-14.23.4.2707-mdk
* How to run tests: Compile and run the code in Eclipse/IntelliJ. Also, add "-Dfml.coreMods.load=org.bitbucket.lanius.core.LaniusLoadingPlugin" to your VM launch parameters. Set LaniusLoadingPlugin#nameRegistry#USE_SRGS to false.
* Deployment instructions: run "gradlew build" from the MDK directory. The compiled JAR can be found in build/libs. Put it in <your_minecraft_directory>/mods. Reset LaniusLoadingPlugin#nameRegistry#USE_SRGS to true.

### Contribution guidelines ###

* Writing tests: Please test your code thoroughly
* Code review: Please provide explanations for why you think <idea> and please keep it constructive
* Other guidelines: Please give variables meaningful names. Do not use temporary variable names (example: int i). Also, please use reflection over ASM whenever possible.

### Who do I talk to? ###

* Eric