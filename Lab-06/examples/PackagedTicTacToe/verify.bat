@echo off
setlocal
cd /d "%~dp0"
if not exist out mkdir out
javac -d out gamelogic\*.java userinterface\*.java main\*.java test\*.java
if errorlevel 1 exit /b 1
java -cp out test.GameSelfTest
exit /b %errorlevel%
