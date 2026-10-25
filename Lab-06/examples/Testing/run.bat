@echo off
setlocal
cd /d "%~dp0"
if not exist out\myprog (
    call compile.bat
    if errorlevel 1 exit /b 1
)
java -cp "out\myprog;out\main" com.myprog.main.Main
exit /b %errorlevel%
