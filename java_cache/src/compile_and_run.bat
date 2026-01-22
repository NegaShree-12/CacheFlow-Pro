@echo off
echo 🔧 Compiling CacheFlow Java project...
echo ======================================

REM Compile all Java files
javac cacheflow\*.java

if %errorlevel% equ 0 (
    echo ✅ Compilation successful!
    echo.
    echo 🚀 Running CacheFlow...
    echo ========================
    java cacheflow.Main
) else (
    echo ❌ Compilation failed!
    pause
    exit /b 1
)