@rem ----------------------------------------------------------------------------
@rem 启动Amoeba的脚本
@rem
@rem 需要设置如下环境变量：
@rem
@rem    JAVA_HOME           - JDK的安装路径
@rem
@rem ----------------------------------------------------------------------------
@echo off
if "%OS%"=="Windows_NT" setlocal

:CHECK_JAVA_HOME
if not "%JAVA_HOME%"=="" goto SET_SERVER_HOME

echo.
echo 错误: 必须设置环境变量“JAVA_HOME”，指向JDK的安装路径
echo.
goto END

:SET_SERVER_HOME
set BIN_HOME=%~dp0
cd %BIN_HOME%
cd ..
set SERVER_HOME=%cd%
if not "%SERVER_HOME%"=="" goto START_SERVER

echo.
echo 错误: 必须设置环境变量“SERVER_HOME”，指向server的安装路径
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