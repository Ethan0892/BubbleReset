@ECHO OFF
setlocal ENABLEEXTENSIONS

REM ----------------------------------------------------------------------------
REM Maven Wrapper startup script for Windows
REM ----------------------------------------------------------------------------

set "BASEDIR=%~dp0"
set "WRAPPER_DIR=%BASEDIR%\.mvn\wrapper"
set "WRAPPER_JAR=%WRAPPER_DIR%\maven-wrapper.jar"
set "WRAPPER_PROPERTIES=%WRAPPER_DIR%\maven-wrapper.properties"

REM Read wrapperUrl from properties
set DOWNLOAD_URL=
for /F "usebackq tokens=1,2 delims==" %%A in ("%WRAPPER_PROPERTIES%") do (
  if /I "%%A"=="wrapperUrl" set DOWNLOAD_URL=%%B
)

if not exist "%WRAPPER_JAR%" (
  echo Downloading Maven Wrapper JAR from %DOWNLOAD_URL%
  powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; New-Item -ItemType Directory -Force -Path '%WRAPPER_DIR%' ^| Out-Null; Invoke-WebRequest -Uri '%DOWNLOAD_URL%' -OutFile '%WRAPPER_JAR%'"
)

REM Determine project basedir
IF NOT DEFINED MAVEN_PROJECTBASEDIR (
  set "MAVEN_PROJECTBASEDIR=%BASEDIR%"
)

REM Resolve Java executable
set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
IF NOT EXIST "%JAVA_EXE%" set "JAVA_EXE=java"

"%JAVA_EXE%" -version >NUL 2>&1
IF ERRORLEVEL 1 (
  echo Java not found. Please install Java 21 or set JAVA_HOME and try again.
  exit /b 1
)

REM JVM and Maven opts
set MAVEN_OPTS=%MAVEN_OPTS%
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

"%JAVA_EXE%" %MAVEN_OPTS% -classpath "%WRAPPER_JAR%" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" %WRAPPER_LAUNCHER% %*

endlocal
