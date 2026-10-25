@echo off
setlocal
cd /d "%~dp0"
call compile.bat
if errorlevel 1 exit /b 1
java -jar "lib\junit-platform-console-standalone-1.11.3.jar" execute --class-path "out\main;out\test" --scan-class-path --fail-if-no-tests
exit /b %errorlevel%
