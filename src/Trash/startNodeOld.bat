# This file starts all Nodes written in the file inputTextFile.txt and once the Initiator

# Removes all class files
rm *.class

# Compiles java files
javac Node.java
javac Initiator.java

echo Geben Sie die Anzahl an Knoten an: 
set /P nodes=
echo !nodes!

for /l %%x in (1, 1, %nodes%) do (
   echo %%x
   start cmd /K java Node %%x
)

timeout 100

setlocal enabledelayedexpansion
set counter=0
set limit=0

:: Reads every line of the file inputTextFile.txt
:: For every Node ID (written in each line) the java programm Node is started with the corresponding ID as parameter
for /f %%i in (D:\GitHubProjekte\VAA_WS2018_U1_A3\inputFiles\inputGraphFile) do (
set /a counter=!counter!+1
echo counter is !counter!
)

set /a limit=!counter!-2
echo limit is !limit!

for /l %%x in (1, 1, %limit%) do (
   echo %%x
   start cmd /K java Node %%x
)

timeout 100
