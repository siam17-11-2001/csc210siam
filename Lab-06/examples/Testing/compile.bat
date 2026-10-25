@echo off
setlocal
cd /d "%~dp0"
set "JAR=lib\junit-platform-console-standalone-1.11.3.jar"
set "JAR_URL=https://repo.maven.apache.org/maven2/org/junit/platform/junit-platform-console-standalone/1.11.3/junit-platform-console-standalone-1.11.3.jar"

if not exist lib mkdir lib
if not exist "%JAR%" (
  echo Downloading JUnit 5 Console Standalone...
  powershell -NoProfile -Command "try { Invoke-WebRequest -UseBasicParsing -Uri '%JAR_URL%' -OutFile '%JAR%' -ErrorAction Stop } catch { Write-Error $_; exit 1 }"
  if errorlevel 1 (
    echo Download failed. Place the JUnit jar in the lib folder and retry.
    exit /b 1
  )
)

if not exist out\main mkdir out\main
if not exist out\myprog mkdir out\myprog
if not exist out\test mkdir out\test
javac -d out\main src\main\com\example\math\*.java src\main\com\example\util\*.java
if errorlevel 1 exit /b 1
javac -cp out\main -d out\myprog src\myprog\com\myprog\main\Main.java
if errorlevel 1 exit /b 1
javac -cp "out\main;%JAR%" -d out\test src\test\com\example\math\*.java src\test\com\example\util\*.java
if errorlevel 1 exit /b 1
echo Successfully compiled production code, application, and JUnit tests.
