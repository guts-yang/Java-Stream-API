package com.learn.stream;

import com.learn.stream.basic.BasicStreamOperations;
import com.learn.stream.intermediate.IntermediateStreamOperations;
import com.learn.stream.advanced.AdvancedStreamOperations;
import com.learn.stream.example.ComprehensiveExample;

/**
 * Stream API学习案例启动器
 * 提供一个统一的入口来运行所有示例类
 */
public class StreamAPILauncher {
    
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║              Java Stream API 学习案例启动器                     ║");
        System.out.println("║                                                                  ║");
        System.out.println("║  这是一个完整的Stream API学习项目，包含从基础到高级的各种示例     ║");
        System.out.println("║                                                                  ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        if (args.length == 0) {
            // 如果没有参数，显示菜单
            showMainMenu();
        } else {
            // 如果有参数，直接运行指定示例
            runExample(args[0]);
        }
    }
    
    /**
     * 显示主菜单
     */
    private static void showMainMenu() {
        System.out.println("请选择要运行的示例类别:");
        System.out.println();
        System.out.println("  1. 基础操作示例 (BasicStreamOperations)");
        System.out.println("  2. 中级操作示例 (IntermediateStreamOperations)");
        System.out.println("  3. 高级操作示例 (AdvancedStreamOperations)");
        System.out.println("  4. 综合应用示例 (ComprehensiveExample)");
        System.out.println("  5. 运行所有示例");
        System.out.println("  0. 退出");
        System.out.println();
        System.out.print("请输入选择 (0-5): ");
        
        // 这里使用简化的交互，实际使用时可以添加 Scanner 交互
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        int choice = scanner.nextInt();
        
        switch (choice) {
            case 1:
                runBasicExample();
                break;
            case 2:
                runIntermediateExample();
                break;
            case 3:
                runAdvancedExample();
                break;
            case 4:
                runComprehensiveExample();
                break;
            case 5:
                runAllExamples();
                break;
            case 0:
                System.out.println("感谢使用 Stream API 学习案例!");
                System.exit(0);
                break;
            default:
                System.out.println("无效选择，请重新运行程序!");
                break;
        }
        
        scanner.close();
    }
    
    /**
     * 运行基础示例
     */
    private static void runBasicExample() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("正在运行 Stream API 基础操作示例...");
        System.out.println("=".repeat(80) + "\n");
        
        try {
            BasicStreamOperations.main(new String[]{});
            System.out.println("\n✅ 基础操作示例运行完成!");
        } catch (Exception e) {
            System.err.println("❌ 运行基础操作示例时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 运行中级示例
     */
    private static void runIntermediateExample() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("正在运行 Stream API 中级操作示例...");
        System.out.println("=".repeat(80) + "\n");
        
        try {
            IntermediateStreamOperations.main(new String[]{});
            System.out.println("\n✅ 中级操作示例运行完成!");
        } catch (Exception e) {
            System.err.println("❌ 运行中级操作示例时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 运行高级示例
     */
    private static void runAdvancedExample() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("正在运行 Stream API 高级操作示例...");
        System.out.println("=".repeat(80) + "\n");
        
        try {
            AdvancedStreamOperations.main(new String[]{});
            System.out.println("\n✅ 高级操作示例运行完成!");
        } catch (Exception e) {
            System.err.println("❌ 运行高级操作示例时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 运行综合示例
     */
    private static void runComprehensiveExample() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("正在运行 Stream API 综合应用示例...");
        System.out.println("=".repeat(80) + "\n");
        
        try {
            ComprehensiveExample.main(new String[]{});
            System.out.println("\n✅ 综合应用示例运行完成!");
        } catch (Exception e) {
            System.err.println("❌ 运行综合应用示例时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 运行所有示例
     */
    private static void runAllExamples() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("正在依次运行所有 Stream API 示例...");
        System.out.println("=".repeat(80));
        
        // 运行基础示例
        runBasicExample();
        
        System.out.println("\n" + "-".repeat(80));
        pause();
        
        // 运行中级示例
        runIntermediateExample();
        
        System.out.println("\n" + "-".repeat(80));
        pause();
        
        // 运行高级示例
        runAdvancedExample();
        
        System.out.println("\n" + "-".repeat(80));
        pause();
        
        // 运行综合示例
        runComprehensiveExample();
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("🎉 所有示例运行完成! 感谢使用 Stream API 学习案例!");
        System.out.println("=".repeat(80));
    }
    
    /**
     * 运行指定示例（命令行参数版本）
     */
    private static void runExample(String exampleName) {
        switch (exampleName.toLowerCase()) {
            case "basic":
            case "基础":
                runBasicExample();
                break;
            case "intermediate":
            case "中级":
                runIntermediateExample();
                break;
            case "advanced":
            case "高级":
                runAdvancedExample();
                break;
            case "comprehensive":
            case "综合":
            case "example":
                runComprehensiveExample();
                break;
            case "all":
            case "所有":
                runAllExamples();
                break;
            default:
                System.out.println("未知的示例类型: " + exampleName);
                System.out.println("支持的示例类型: basic, intermediate, advanced, comprehensive, all");
                break;
        }
    }
    
    /**
     * 暂停，等待用户按键继续
     */
    private static void pause() {
        System.out.println("\n按 Enter 键继续下一个示例...");
        try {
            System.in.read();
        } catch (Exception e) {
            // 忽略异常
        }
    }
    
    /**
     * 显示项目信息
     */
    public static void showProjectInfo() {
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    Java Stream API 学习案例                     ║");
        System.out.println("║                                                                  ║");
        System.out.println("║  项目特点:                                                       ║");
        System.out.println("║  ✓ 完整的项目结构                                               ║");
        System.out.println("║  ✓ 递进式学习路径                                               ║");
        System.out.println("║  ✓ 详细的代码注释                                               ║");
        System.out.println("║  ✓ 综合应用示例                                                 ║");
        System.out.println("║  ✓ 实用的性能优化技巧                                           ║");
        System.out.println("║                                                                  ║");
        System.out.println("║  学习路径:                                                      ║");
        System.out.println("║  基础操作 → 中级操作 → 高级操作 → 综合应用                       ║");
        System.out.println("║                                                                  ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
    }
    
    /**
     * 显示使用说明
     */
    public static void showUsage() {
        System.out.println("使用说明:");
        System.out.println("  1. 直接运行程序，然后按照菜单提示选择");
        System.out.println("  2. 使用命令行参数直接运行指定示例:");
        System.out.println("     java -cp bin StreamAPILauncher basic");
        System.out.println("     java -cp bin StreamAPILauncher intermediate");
        System.out.println("     java -cp bin StreamAPILauncher advanced");
        System.out.println("     java -cp bin StreamAPILauncher comprehensive");
        System.out.println("     java -cp bin StreamAPILauncher all");
        System.out.println();
        System.out.println("示例说明:");
        System.out.println("  basic        - 基础操作示例：Stream创建、过滤、映射、排序等");
        System.out.println("  intermediate - 中级操作示例：聚合、分组、归约、Optional等");
        System.out.println("  advanced     - 高级操作示例：自定义Collector、性能优化等");
        System.out.println("  comprehensive- 综合应用示例：实际业务场景中的应用");
        System.out.println("  all          - 运行所有示例");
    }
}