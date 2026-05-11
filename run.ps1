Set-Location $PSScriptRoot

Write-Host "Compilando proyecto..."
mvn clean compile

Write-Host "Ejecutando pruebas..."
mvn test

Write-Host "Ejecutando aplicacion..."
mvn "exec:java" "-Dexec.mainClass=com.carbonprint.main.Main"