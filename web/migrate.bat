@echo off
set VERSION=%1
set TOOL_HOME=C:\java\jakartaee-migration-1.0.8

echo [INFO] Migrating target\ctsms-%VERSION%.war...

"%JAVA_HOME%\bin\java.exe" -cp "%TOOL_HOME%\lib\*" org.apache.tomcat.jakartaee.MigrationCLI "target\ctsms-%VERSION%.war" "target\ctsms-%VERSION%-migrated.war"

if exist "target\ctsms-%VERSION%-migrated.war" (
    copy /y "target\ctsms-%VERSION%-migrated.war" "target\ctsms-%VERSION%.war"
    echo [INFO] Migration and overwrite complete.
)