@echo off
setlocal enabledelayedexpansion

echo.
echo ============================================================
echo Android Gradle Build Fix - Gradle 9.3.1 Downloader
echo ============================================================
echo.
echo Current Directory: %cd%
echo.

REM Delete cached gradle to force redownload
echo Clearing Gradle cache...
if exist "%USERPROFILE%\.gradle\wrapper\dists" (
    echo Removing old Gradle distributions...
    rmdir /s /q "%USERPROFILE%\.gradle\wrapper\dists"
    if !errorlevel! equ 0 (
        echo ^[OK^] Cache cleared
    ) else (
        echo ^[WARNING^] Could not clear all cache
    )
) else (
    echo Cache already clean
)

echo.
echo Clearing local build cache...
if exist "build" rmdir /s /q build
if exist "app\build" rmdir /s /q app\build
if exist ".gradle" rmdir /s /q .gradle

echo ^[OK^] Local cache cleared
echo.

echo ============================================================
echo Downloading Gradle 9.3.1...
echo ============================================================
echo.

REM Download and run build
call .\gradlew.bat --version

echo.
echo ============================================================
echo Running Build...
echo ============================================================
echo.

call .\gradlew.bat clean build -x test --stacktrace

if %ERRORLEVEL% equ 0 (
    echo.
    echo ============================================================
    echo BUILD SUCCESSFUL!
    echo ============================================================
    echo.
    echo Your project has been built with Gradle 9.3.1
    echo Now do this in Android Studio:
    echo   1. File -^> Invalidate Caches -^> Invalidate and Restart
    echo   2. File -^> Sync Now
    echo   3. Build -^> Rebuild Project
    echo.
    echo Your error should now be fixed!
    echo.
) else (
    echo.
    echo ============================================================
    echo BUILD FAILED!
    echo ============================================================
    echo.
    echo Check the errors above.
    echo.
)

pause
endlocal

