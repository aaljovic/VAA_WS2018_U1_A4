:: This file starts all Nodes written in the file inputTextFile.txt and once the Initiator

:: Removes all class files
:: rm *.class

:: set Classpath for JSON jar
set CLASSPATH=.;D:\GitHubProjekte\VAA_WS2018_U1_A4\lib\org.json.jar

:: Compiles java files
javac Node.java
javac Initiator.java

:: Starts the java Initiator programm once
start cmd /K java Initiator
