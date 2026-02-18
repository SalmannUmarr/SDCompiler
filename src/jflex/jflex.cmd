@echo off
java -cp "%~dp0jflex.jar;%~dp0java-cup-runtime.jar" jflex.Main %*
