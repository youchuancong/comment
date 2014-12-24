@rem ----------------------------------------------------------------------------
@rem ����Amoeba�Ľű�
@rem
@rem ��Ҫ�������»���������
@rem
@rem    JAVA_HOME           - JDK�İ�װ·��
@rem
@rem ----------------------------------------------------------------------------
@echo off
if "%OS%"=="Windows_NT" setlocal

:CHECK_JAVA_HOME
if not "%JAVA_HOME%"=="" goto SET_SERVER_HOME

echo.
echo ����: �������û���������JAVA_HOME����ָ��JDK�İ�װ·��
echo.
goto END

:SET_SERVER_HOME
set BIN_HOME=%~dp0
cd %BIN_HOME%
cd ..
set SERVER_HOME=%cd%
if not "%SERVER_HOME%"=="" goto START_SERVER

echo.
echo ����: �������û���������SERVER_HOME����ָ��server�İ�װ·��
echo.
goto END

:START_SERVER
echo "server_home:"
echo %SERVER_HOME%
@rem  FOR /F "tokens=1,* delims=.=" %%G IN (%AMOEBA_HOME%\jvm.properties) DO (
@rem 	 set %%G=%%H 
@rem 	 echo %%G=$ %%H
@rem )


set DEFAULT_OPTS=-server -Xms256m -Xmx1024m -Xss128k
set DEFAULT_OPTS=%DEFAULT_OPTS% -XX:+HeapDumpOnOutOfMemoryError -XX:+AggressiveOpts -XX:+UseParallelGC -XX:+UseBiasedLocking -XX:NewSize=64m
set DEFAULT_OPTS=%DEFAULT_OPTS% -Dcom.sun.management.jmxremote=true
set DEFAULT_OPTS=%DEFAULT_OPTS% -Dcom.sun.management.jmxremote.port=9090
set DEFAULT_OPTS=%DEFAULT_OPTS% -Dcom.sun.management.jmxremote.ssl=false
set DEFAULT_OPTS=%DEFAULT_OPTS% -Dcom.sun.management.jmxremote.authenticate=false
set DEFAULT_OPTS=%DEFAULT_OPTS% "-Dweb.home=%SERVER_HOME%"
set DEFAULT_OPTS=%DEFAULT_OPTS% "-Dclassworlds.conf=%SERVER_HOME%\bin\launcher.classpath"

set JAVA_EXE="%JAVA_HOME%\bin\java.exe"
set CLASSPATH="%SERVER_HOME%\lib\plexus-classworlds-2.5.2.jar"
set MAIN_CLASS="org.codehaus.classworlds.Launcher"

%JAVA_EXE% %DEFAULT_OPTS% -classpath %CLASSPATH% %MAIN_CLASS% %*

:END
if "%OS%"=="Windows_NT" endlocal
pause