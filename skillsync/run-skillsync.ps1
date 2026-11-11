
$projectPath = $PSScriptRoot
$url = "http://localhost:8080"

Write-Host "Starting SkillSync..."

# Start Spring Boot in a separate PowerShell window
Start-Process powershell.exe -ArgumentList @(
    "-NoExit",
    "-Command",
    "Set-Location -LiteralPath '$projectPath'; .\mvnw.cmd spring-boot:run"
)

# Wait until the application responds
Write-Host "Waiting for SkillSync to start..."

$started = $false

for ($i = 0; $i -lt 60; $i++) {
    try {
        $response = Invoke-WebRequest -Uri $url -TimeoutSec 2 -UseBasicParsing
        $started = $true
        break
    }
    catch {
        Start-Sleep -Seconds 2
    }
}

# Open the browser after the server is ready
if ($started) {
    Start-Process $url
    Write-Host "SkillSync opened in your browser!"
}
else {
    Write-Host "SkillSync did not respond in time."
    Write-Host "Check the Spring Boot terminal for errors."
}
