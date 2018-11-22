# This file starts all Nodes written in the file inputTextFile.txt and once the Initiator

# Removes all class files
rm *.class

# Compiles java files
javac Node.java
javac Initiator.java

# Reads every line of the file inputTextFile.txt
# For every Node ID (written in each line) the java programm Node is started with the corresponding ID as parameter
for /f %%i in (D:\GitHubProjekte\VAA_WS2018_U1_A2\inputFiles\inputTextFile) do (
start cmd /K java Node %%i
)

# Starts the java Initiator programm once
start cmd /K java Initiator