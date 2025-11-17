@echo off
REM ========================================
REM Java Stream API 学习案例编译运行脚本
REM ========================================

echo.
echo ╔══════════════════════════════════════════════════════════════════╗
echo ║              Java Stream API 学习案例                            ║
echo ║              编译运行脚本                                        ║
echo ╚══════════════════════════════════════════════════════════════════╝
echo.

REM 检查 Java 环境
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ 错误: 未找到 Java 环境，请安装 JDK 8 或更高版本
    echo.
    pause
    exit /b 1
)

REM 检查 Maven 环境
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ⚠️  警告: 未找到 Maven 环境，将使用 javac 手动编译
    echo.
    goto :manual_compile
)

REM 使用 Maven 编译和运行
echo 🔨 使用 Maven 编译项目...
mvn clean compile

if %errorlevel% neq 0 (
    echo ❌ Maven 编译失败，尝试手动编译...
    goto :manual_compile
)

echo.
echo 🚀 启动 Stream API 学习案例...
echo.

mvn exec:java

if %errorlevel% neq 0 (
    echo ❌ Maven 运行失败，尝试手动运行...
    goto :manual_run
)

echo.
echo ✅ 程序执行完成!
pause
exit /b 0

:manual_compile
echo.
echo 🔨 使用 javac 手动编译...
REM 创建输出目录
if not exist "bin" mkdir bin

REM 编译所有 Java 文件
javac -d bin -cp "." src/main/java/com/learn/stream/**/*.java

if %errorlevel% neq 0 (
    echo ❌ 编译失败!
    echo.
    pause
    exit /b 1
)

echo ✅ 编译成功!

:manual_run
echo.
echo 🚀 运行 Stream API 学习案例...
echo.

java -cp bin com.learn.stream.StreamAPILauncher

if %errorlevel% neq 0 (
    echo ❌ 运行失败!
    echo.
    pause
    exit /b 1
)

echo.
echo ✅ 程序执行完成!
pause
exit /b 0