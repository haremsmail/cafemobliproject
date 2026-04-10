@echo off
REM This script syncs the Gradle wrapper to the correct version
REM and performs a clean build

echo Syncing Gradle wrapper to version 9.3.1...
cd /d "%~dp0"

REM Delete the old gradle cache
if exist "%USERPROFILE%\.gradle\wrapper\dists" (
    echo Clearing old Gradle distributions...
    rmdir /s /q "%USERPROFILE%\.gradle\wrapper\dists"
)

REM Clear the local build cache
if exist "build" rmdir /s /q build
if exist "app\build" rmdir /s /q app\build
if exist ".gradle" rmdir /s /q .gradle

echo.
echo Running Gradle clean build...
call gradlew.bat clean build --refresh-dependencies

if %ERRORLEVEL% EQU 0 (
    echo.
    echo SUCCESS! Build completed successfully.
    echo The project is ready to use.
) else (
    echo.
    echo BUILD FAILED! Check the error messages above.
)

pause

