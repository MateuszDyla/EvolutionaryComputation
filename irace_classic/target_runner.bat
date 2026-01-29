@echo off
setlocal EnableExtensions EnableDelayedExpansion

REM irace passes:
REM %1 = configuration id
REM %2 = instance id (numeric)
REM %3 = seed
REM %4 = instance name (e.g., Ackley)
REM %5.. = parameters (from parameters.txt)

set "PROBLEM=%4"
set "SEED=%3"

shift
shift
shift
shift

REM === EDIT THESE IF NEEDED ===
REM Path to your built JAR (relative to this folder)
set "CP=..\build\libs\Evo2-1.0-SNAPSHOT.jar"
REM Dimension and evaluation budget used in *each* run
set "N=40"
set "FES=1500000"
REM ===========================

set "LASTLINE="

for /f "usebackq delims=" %%L in (`
  java -cp "%CP%" org.example.IraceRunner --problem "%PROBLEM%" --n %N% --fes %FES% --seed %SEED% --alg ClassicGA --stopOnOptimum false %* 2^>^&1
`) do (
  set "LASTLINE=%%L"
)

if not defined LASTLINE (
  echo 1e100
) else (
  echo !LASTLINE!
)

endlocal
exit /b 0
