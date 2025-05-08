@echo off
echo Compilando el juego...
javac -d bin src/*.java

if %errorlevel% neq 0 (
    echo Error al compilar el juego
    pause
    exit /b %errorlevel%
)

echo Ejecutando el juego...
java -cp bin StreetBuds

pause 