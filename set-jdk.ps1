# class-times-record JDK 21 Setup
# Run this before any Maven command:
#   . .\set-jdk.ps1

$env:JAVA_HOME = "D:\JAVA\jdk\jdk21"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

Write-Host "JAVA_HOME set to: $env:JAVA_HOME" -ForegroundColor Green
java -version
