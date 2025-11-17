@echo off
echo ========================================
echo Java Stream API 学习案例 - 编译运行脚本
echo ========================================
echo.

echo [1/3] 正在编译所有Java文件...
javac -d bin -cp "bin" src\main\java\com\learn\stream\*.java src\main\java\com\learn\stream\basic\*.java src\main\java\com\learn\stream\intermediate\*.java src\main\java\com\learn\stream\advanced\*.java src\main\java\com\learn\stream\example\*.java src\main\java\com\learn\stream\model\*.java

if %errorlevel% neq 0 (
    echo ❌ 编译失败！请检查代码错误。
    pause
    exit /b %errorlevel%
)

echo ✅ 编译成功！
echo.

echo [2/3] 正在启动图形化界面...
echo ========================================
echo 🎯 GUI界面启动中，请稍候...
echo ========================================
echo.

echo [3/3] 启动 Stream API 学习界面
java -cp "bin" com.learn.stream.StreamAPIGUI

echo.
echo 程序已结束运行。