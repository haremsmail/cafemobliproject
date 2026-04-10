@echo off
REM Quick Android Studio Cache Clear Script

echo.
echo Clearing Android Studio Gradle Cache...
echo.

REM Clear .gradle cache
if exist "%USERPROFILE%\.gradle" (
    echo Removing .gradle folder...
    rmdir /s /q "%USERPROFILE%\.gradle"
    echo Done!
)

echo.
echo Cache cleared. Now do this:
echo 1. Open Android Studio
echo 2. File -^> Invalidate Caches -^> Invalidate and Restart
echo 3. Wait for Android Studio to restart
echo 4. File -^> Sync Now
echo.
echo Your Gradle error should now be gone!
echo.

pause

