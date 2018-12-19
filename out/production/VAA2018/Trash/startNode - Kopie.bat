# This file starts all Nodes written in the file inputTextFile.txt and once the Initiator

# Removes all class files
rm *.class

# Compiles java files
javac Node.java
javac Initiator.java

set /A test=0
set /A test2=1
SET /A Ans=%test%+%test2%+1
ECHO The sum is: %Ans%

# Reads every line of the file inputTextFile.txt
# For every Node ID (written in each line) the java programm Node is started with the corresponding ID as parameter
for /f %%i in (D:\GitHubProjekte\VAA_WS2018_U1_A3\inputFiles\inputTextFile) do (
ECHO %%i
set /A test+=1
SET /A Ans+=1
ECHO %test%
ECHO The sum is: %Ans%
)


pause