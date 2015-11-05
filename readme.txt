sirius
Personal Assistant using Voce.
https://github.com/shuvojitNITW/sirius

This file contains basic installation and usage info.

------------------------------------
Package contents
------------------------------------
lib - directory containing compiled Voce, FreeTTS, and Sphinx4 libraries
src - directory containing the Sirius source code written in Java Java
readme.txt - you're reading it
vocabulary.txt - a list of the words Voce can recognize (via Sphinx4)


------------------------------------
List of dependencies
------------------------------------
1. FreeTTS (version 1.2.1 included in this package) - freetts.sourceforge.net
2. CMUSphinx4 (version 1.0beta included in this package) - cmusphinx.sourceforge.net
3. Java 1.5 runtime environment; also, Java SDK required to build Voce from source
4. Java Native Interface (JNI) (usually included in the Java SDK) required for C++ applications


------------------------------------------
build and compilation
-------------------------------------------
give the absolute path to in this directory  classpath file
sudo chmod +x build
./build
sudo chmod +x run
./run

------------------------------------
Tips for good recognition results
------------------------------------
* A good microphone makes a world of difference.  I saw substantial improvements in recognition accuracy when I switched to a cheap Labtec headset after using my laptop's built-in microphone.
