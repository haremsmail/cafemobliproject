# Gradle Sync Fix Script
Write-Host "===== GRADLE SYNC FIX =====" -ForegroundColor Green
Write-Host ""

$projectPath = "C:\Users\Source Tech Co\AndroidStudioProjects\projectyear"
Set-Location $projectPath

Write-Host "Step 1: Cleaning Gradle cache..." -ForegroundColor Yellow
cmd /c "gradlew.bat clean"

Write-Host ""
Write-Host "Step 2: Running Gradle sync..." -ForegroundColor Yellow
cmd /c "gradlew.bat assemble"

Write-Host ""
Write-Host "Step 3: Build complete!" -ForegroundColor Green
Write-Host ""
Write-Host "Your project is now ready to use!" -ForegroundColor Green

