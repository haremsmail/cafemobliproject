# PowerShell script to sync Gradle and build the project

Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "Android Studio Project Build Sync Script" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host ""

$projectRoot = $PSScriptRoot
Write-Host "Project Root: $projectRoot" -ForegroundColor Yellow

# Clear Gradle caches
Write-Host ""
Write-Host "Clearing Gradle caches..." -ForegroundColor Yellow

$gradleUserHome = "$env:USERPROFILE\.gradle"
if (Test-Path "$gradleUserHome\wrapper\dists") {
    Write-Host "Removing old Gradle distributions..."
    Remove-Item -Path "$gradleUserHome\wrapper\dists" -Recurse -Force -ErrorAction SilentlyContinue
}

if (Test-Path "$projectRoot\build") {
    Write-Host "Removing build directory..."
    Remove-Item -Path "$projectRoot\build" -Recurse -Force -ErrorAction SilentlyContinue
}

if (Test-Path "$projectRoot\app\build") {
    Write-Host "Removing app/build directory..."
    Remove-Item -Path "$projectRoot\app\build" -Recurse -Force -ErrorAction SilentlyContinue
}

if (Test-Path "$projectRoot\.gradle") {
    Write-Host "Removing .gradle directory..."
    Remove-Item -Path "$projectRoot\.gradle" -Recurse -Force -ErrorAction SilentlyContinue
}

Write-Host "Caches cleared!" -ForegroundColor Green
Write-Host ""

# Run Gradle build
Write-Host "Running Gradle clean build with Gradle 9.3.1..." -ForegroundColor Yellow
Write-Host "This may take a few minutes on first run..." -ForegroundColor Cyan
Write-Host ""

Set-Location $projectRoot

# Execute the Gradle wrapper
& .\gradlew.bat clean build --refresh-dependencies

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "==================================================" -ForegroundColor Green
    Write-Host "SUCCESS! Build completed successfully." -ForegroundColor Green
    Write-Host "==================================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Your project is now ready to use!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Next steps:" -ForegroundColor Yellow
    Write-Host "1. Sync the project in Android Studio (File > Sync Now)" -ForegroundColor White
    Write-Host "2. Build and Run the app" -ForegroundColor White
    Write-Host ""
} else {
    Write-Host ""
    Write-Host "==================================================" -ForegroundColor Red
    Write-Host "BUILD FAILED!" -ForegroundColor Red
    Write-Host "==================================================" -ForegroundColor Red
    Write-Host "Check the error messages above for details." -ForegroundColor Red
}

Read-Host "Press Enter to exit"

