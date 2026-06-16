set JAVA_HOME=C:\Program Files\Java\jdk-23
set PATH=%JAVA_HOME%\bin;%PATH%
call C:\glassfish-7.1.0\glassfish7\bin\asadmin.bat start-domain
call C:\glassfish-7.1.0\glassfish7\bin\asadmin.bat deploy --force=true "C:\Andrii\UstynovJavaAutomatization\PR1automatizationUstynov\target\PR1automatizationUstynov-1.0-SNAPSHOT.war"
pause