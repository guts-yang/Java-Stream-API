package com.learn.stream.advanced;

import com.learn.stream.model.Student;
import com.learn.stream.model.Course;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Stream API高级操作示例
 * 演示函数式接口、自定义Collector、异常处理、状态操作等
 */
public class AdvancedStreamOperations {
    
    public static void main(String[] args) {
        System.out.println("=== Stream API高级操作示例 ===\n");
        
        // 1. 自定义函数式接口
        demonstrateCustomFunctionalInterfaces();
        
        // 2. 状态操作 (peek, peek复杂用法)
        demonstrateStatefulOperations();
        
        // 3. 自定义Collector
        demonstrateCustomCollector();
        
        // 4. 异常处理在Stream中的使用
        demonstrateExceptionHandling();
        
        // 5. 无限Stream
        demonstrateInfiniteStreams();
        
        // 6. 高级收集操作 (joining, partitioningBy)
        demonstrateAdvancedCollecting();
        
        // 7. Stream链式操作
        demonstrateStreamChaining();
        
        // 8. 性能优化技巧
        demonstratePerformanceOptimization();
    }
    
    /**
     * 演示自定义函数式接口
     */
    private static void demonstrateCustomFunctionalInterfaces() {
        System.out.println("1. 自定义函数式接口:");
        
        // 自定义Predicate
        Predicate<Student> isExcellentStudent = student -> student.getGpa() >= 4.0;
        Predicate<Student> isCSMajor = student -> "计算机科学".equals(student.getMajor());
        
        List<Student> students = createSampleStudents();
        
        // 组合Predicate
        System.out.println("  优秀且是计算机专业的学生:");
        students.stream()
                .filter(isExcellentStudent.and(isCSMajor))
                .forEach(student -> System.out.println("    - " + student.getName() + ", GPA: " + student.getGpa()));
        
        // 自定义Function
        Function<Student, String> studentInfo = student -> 
                String.format("%s(%s, %.1f)", student.getName(), student.getMajor(), student.getGpa());
        
        System.out.println("\n  学生信息转换:");
        students.stream()
                .map(studentInfo)
                .forEach(info -> System.out.println("    - " + info));
        
        // 自定义Consumer
        Consumer<Student> printStudentInfo = student -> {
            System.out.println("    学生: " + student.getName());
            System.out.println("      年龄: " + student.getAge());
            System.out.println("      专业: " + student.getMajor());
            System.out.println("      GPA: " + student.getGpa());
        };
        
        System.out.println("\n  使用自定义Consumer:");
        students.stream()
                .filter(s -> s.getGpa() > 4.0)
                .forEach(printStudentInfo);
        
        System.out.println();
    }
    
    /**
     * 演示状态操作
     */
    private static void demonstrateStatefulOperations() {
        System.out.println("2. 状态操作:");
        
        List<Student> students = createSampleStudents();
        
        // 使用peek调试Stream处理过程
        System.out.println("  使用peek调试:");
        students.stream()
                .filter(student -> student.getGpa() > 3.5)
                .peek(student -> System.out.println("  过滤后: " + student.getName()))
                .map(Student::getName)
                .peek(name -> System.out.println("  映射后: " + name))
                .collect(Collectors.toList());
        
        // 使用peek修改对象状态（注意：这在并行Stream中可能导致问题）
        System.out.println("\n  使用peek修改状态:");
        students.stream()
                .peek(student -> {
                    if (student.getGpa() > 4.0) {
                        student.setName(student.getName() + "*"); // 标记优秀学生
                    }
                })
                .filter(student -> student.getName().contains("*"))
                .forEach(student -> System.out.println("    优秀学生: " + student.getName()));
        
        // 重置学生姓名
        students.forEach(student -> {
            if (student.getName().endsWith("*")) {
                student.setName(student.getName().replace("*", ""));
            }
        });
        
        System.out.println();
    }
    
    /**
     * 演示自定义Collector
     */
    private static void demonstrateCustomCollector() {
        System.out.println("3. 自定义Collector:");
        
        List<Student> students = createSampleStudents();
        
        // 自定义Collector：收集统计信息
        Collector<Student, ?, StudentStatistics> studentStatsCollector = Collector.of(
                StudentStatistics::new,
                StudentStatistics::accept,
                StudentStatistics::combine
        );
        
        StudentStatistics stats = students.stream()
                .collect(studentStatsCollector);
        
        System.out.println("  自定义收集器统计结果:");
        System.out.println("    总人数: " + stats.totalCount);
        System.out.println("    平均年龄: " + String.format("%.2f", stats.getAvgAge()));
        System.out.println("    平均GPA: " + String.format("%.2f", stats.getAvgGpa()));
        
        // 使用Collectors.joining进行复杂字符串连接
        String majorList = students.stream()
                .map(Student::getMajor)
                .distinct()
                .collect(Collectors.joining(" | ", "专业列表: [", "]"));
        System.out.println("\n  复杂字符串连接: " + majorList);
        
        System.out.println();
    }
    
    /**
     * 演示异常处理
     */
    private static void demonstrateExceptionHandling() {
        System.out.println("4. 异常处理:");
        
        List<String> testData = Arrays.asList("10", "20", "invalid", "30", null, "40");
        
        // 使用try-catch包装函数
        Function<String, Integer> safeParseInt = str -> {
            try {
                return Integer.parseInt(str);
            } catch (Exception e) {
                return -1; // 返回默认值
            }
        };
        
        System.out.println("  安全解析数字:");
        testData.stream()
                .filter(Objects::nonNull)
                .map(safeParseInt)
                .filter(num -> num != -1)
                .forEach(num -> System.out.println("    - " + num));
        
        // 使用自定义异常处理wrapper
        List<Integer> numbers = testData.stream()
                .map(s -> {
                    try {
                        return safeConvertToInt(s);
                    } catch (Exception e) {
                        return -1;
                    }
                })
                .collect(Collectors.toList());
        
        System.out.println("\n  处理后的数字列表: " + numbers);
        
        // 过滤掉无效数据
        List<Integer> validNumbers = numbers.stream()
                .filter(num -> num > 0)
                .collect(Collectors.toList());
        System.out.println("  有效数字: " + validNumbers);
        
        System.out.println();
    }
    
    /**
     * 演示无限Stream
     */
    private static void demonstrateInfiniteStreams() {
        System.out.println("5. 无限Stream:");
        
        // 随机数Stream
        System.out.println("  生成5个随机数:");
        new Random()
                .ints(5, 1, 100)
                .forEach(num -> System.out.println("    - " + num));
        
        // 迭代Stream
        System.out.println("\n  迭代生成序列 (2的幂次):");
        Stream.iterate(1, n -> n * 2)
                .limit(8)
                .forEach(power -> System.out.println("    - " + power));
        
        // 生成Stream
        System.out.println("\n  使用generate创建Stream:");
        Stream.generate(() -> "Hello")
                .limit(3)
                .forEach(greeting -> System.out.println("    - " + greeting));
        
        // 复杂迭代：斐波那契数列
        System.out.println("\n  斐波那契数列:");
        Stream.iterate(new int[]{0, 1}, fib -> new int[]{fib[1], fib[0] + fib[1]})
                .map(fib -> fib[0])
                .limit(10)
                .forEach(num -> System.out.println("    - " + num));
        
        // 带条件的迭代
        System.out.println("\n  迭代到100以内的偶数:");
        Stream.iterate(2, n -> n + 2)
                .takeWhile(n -> n <= 100)
                .forEach(num -> System.out.println("    - " + num));
        
        System.out.println();
    }
    
    /**
     * 演示高级收集操作
     */
    private static void demonstrateAdvancedCollecting() {
        System.out.println("6. 高级收集操作:");
        
        List<Student> students = createSampleStudents();
        
        // 分区操作 (partitioningBy)
        Map<Boolean, List<Student>> partitionByGpa = students.stream()
                .collect(Collectors.partitioningBy(student -> student.getGpa() > 3.5));
        
        System.out.println("  按GPA分区 (>3.5):");
        System.out.println("    高GPA学生: " + 
                         partitionByGpa.get(true).stream()
                                       .map(Student::getName)
                                       .collect(Collectors.joining(", ")));
        System.out.println("    低GPA学生: " + 
                         partitionByGpa.get(false).stream()
                                       .map(Student::getName)
                                       .collect(Collectors.joining(", ")));
        
        // 自定义收集：计算GPA分布
        Map<String, Long> gpaDistribution = students.stream()
                .collect(Collectors.groupingBy(
                        student -> {
                            double gpa = student.getGpa();
                            if (gpa >= 4.0) return "优秀(≥4.0)";
                            else if (gpa >= 3.5) return "良好(3.5-3.9)";
                            else if (gpa >= 3.0) return "中等(3.0-3.4)";
                            else return "及格(<3.0)";
                        },
                        Collectors.counting()
                ));
        
        System.out.println("\n  GPA分布统计:");
        gpaDistribution.forEach((level, count) -> 
                System.out.println("    " + level + ": " + count + "人"));
        
        // 映射和收集的组合
        Map<String, Double> avgGpaByMajor = students.stream()
                .collect(Collectors.groupingBy(
                        Student::getMajor,
                        Collectors.averagingDouble(Student::getGpa)
                ));
        
        System.out.println("\n  各专业平均GPA:");
        avgGpaByMajor.forEach((major, avgGpa) -> 
                System.out.println("    " + major + ": " + String.format("%.2f", avgGpa)));
        
        System.out.println();
    }
    
    /**
     * 演示Stream链式操作
     */
    private static void demonstrateStreamChaining() {
        System.out.println("7. Stream链式操作:");
        
        List<Student> students = createSampleStudents();
        
        // 复杂的数据处理链
        List<String> result = students.stream()
                // 过滤：选择高GPA学生
                .filter(student -> student.getGpa() > 3.5)
                // 映射：转换信息
                .map(student -> {
                    String level;
                    if (student.getGpa() >= 4.0) level = "优秀";
                    else if (student.getGpa() >= 3.7) level = "良好";
                    else level = "不错";
                    return String.format("%s(%s, %s)", student.getName(), student.getMajor(), level);
                })
                // 排序：按姓名排序
                .sorted()
                // 限制：只取前5个
                .limit(5)
                // 收集结果
                .collect(Collectors.toList());
        
        System.out.println("  复杂链式操作结果: " + result);
        
        // 嵌套集合的处理
        List<List<String>> nestedLists = Arrays.asList(
                Arrays.asList("A", "B", "C"),
                Arrays.asList("D", "E", "F"),
                Arrays.asList("G", "H")
        );
        
        String allElements = nestedLists.stream()
                .flatMap(List::stream)
                .sorted()
                .collect(Collectors.joining(", ", "排序后的所有元素: [", "]"));
        
        System.out.println("\n  " + allElements);
        
        // 多条件复杂处理
        Map<String, List<String>> groupedAndProcessed = students.stream()
                .filter(student -> student.getAge() >= 20) // 过滤年龄
                .sorted(Comparator.comparingDouble(Student::getGpa).reversed()) // 按GPA降序
                .collect(Collectors.groupingBy(
                        Student::getMajor,
                        LinkedHashMap::new, // 保持插入顺序
                        Collectors.mapping(
                                student -> student.getName() + "(" + student.getGpa() + ")",
                                Collectors.toList()
                        )
                ));
        
        System.out.println("\n  按专业分组并排序:");
        groupedAndProcessed.forEach((major, studentsList) -> {
            System.out.println("    " + major + ": " + studentsList);
        });
        
        System.out.println();
    }
    
    /**
     * 演示性能优化技巧
     */
    private static void demonstratePerformanceOptimization() {
        System.out.println("8. 性能优化技巧:");
        
        // 创建大量数据
        List<Integer> largeDataSet = IntStream.range(1, 100000)
                .boxed()
                .collect(Collectors.toList());
        
        // 1. 尽早过滤 (early filtering)
        System.out.println("  尽早过滤 vs 延迟过滤:");
        
        long start = System.currentTimeMillis();
        long countEarly = largeDataSet.stream()
                .filter(n -> n > 50000) // 尽早过滤
                .filter(n -> n % 2 == 0)
                .count();
        long timeEarly = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        long countLate = largeDataSet.stream()
                .filter(n -> {
                    if (n > 50000 && n % 2 == 0) {
                        return true;
                    }
                    return false;
                })
                .count();
        long timeLate = System.currentTimeMillis() - start;
        
        System.out.println("    尽早过滤: " + countEarly + "个数字，耗时" + timeEarly + "ms");
        System.out.println("    延迟过滤: " + countLate + "个数字，耗时" + timeLate + "ms");
        
        // 2. 使用合适类型的Stream
        System.out.println("\n  数值Stream优化:");
        start = System.currentTimeMillis();
        int sumIntStream = IntStream.range(1, 100000)
                .filter(n -> n % 2 == 0)
                .sum();
        long timeIntStream = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        int sumBoxedStream = largeDataSet.stream()
                .filter(n -> n % 2 == 0)
                .mapToInt(Integer::intValue)
                .sum();
        long timeBoxedStream = System.currentTimeMillis() - start;
        
        System.out.println("    IntStream: 和=" + sumIntStream + ", 耗时" + timeIntStream + "ms");
        System.out.println("    boxed Stream: 和=" + sumBoxedStream + ", 耗时" + timeBoxedStream + "ms");
        
        // 3. 并行Stream的使用场景
        System.out.println("\n  并行Stream性能对比:");
        int[] sizes = {1000, 10000, 100000};
        
        for (int size : sizes) {
            List<Integer> data = IntStream.range(1, size).boxed().collect(Collectors.toList());
            
            // 顺序处理
            start = System.currentTimeMillis();
            long sumSequential = data.stream()
                    .filter(n -> n % 3 == 0)
                    .mapToLong(Integer::longValue)
                    .sum();
            long timeSequential = System.currentTimeMillis() - start;
            
            // 并行处理
            start = System.currentTimeMillis();
            long sumParallel = data.parallelStream()
                    .filter(n -> n % 3 == 0)
                    .mapToLong(Integer::longValue)
                    .sum();
            long timeParallel = System.currentTimeMillis() - start;
            
            System.out.println("    数据量" + size + ":");
            System.out.println("      顺序: " + timeSequential + "ms, 并行: " + timeParallel + "ms");
        }
        
        System.out.println();
    }
    
    /**
     * 辅助方法：安全转换字符串为整数
     */
    private static int safeConvertToInt(String str) {
        return Integer.parseInt(str);
    }
    
    /**
     * 辅助方法：异常处理包装器
     */
    private static <T, R> Function<T, R> handleException(FunctionWithException<T, R> fn, R defaultValue) {
        return t -> {
            try {
                return fn.apply(t);
            } catch (Exception e) {
                return defaultValue;
            }
        };
    }
    
    /**
     * 自定义函数式接口：可能抛出异常的函数
     */
    @FunctionalInterface
    interface FunctionWithException<T, R> {
        R apply(T t) throws Exception;
    }
    
    /**
     * 自定义统计信息类
     */
    static class StudentStatistics {
        int totalCount = 0;
        int totalAge = 0;
        double totalGpa = 0;
        
        void accept(Student student) {
            totalCount++;
            totalAge += student.getAge();
            totalGpa += student.getGpa();
        }
        
        StudentStatistics combine(StudentStatistics other) {
            totalCount += other.totalCount;
            totalAge += other.totalAge;
            totalGpa += other.totalGpa;
            return this;
        }
        
        double getAvgAge() {
            return totalCount > 0 ? (double) totalAge / totalCount : 0;
        }
        
        double getAvgGpa() {
            return totalCount > 0 ? totalGpa / totalCount : 0;
        }
    }
    
    /**
     * 创建示例学生数据
     */
    private static List<Student> createSampleStudents() {
        return Arrays.asList(
            new Student("张三", 20, "计算机科学", 3.8, "男"),
            new Student("李四", 22, "数学", 3.6, "女"),
            new Student("王五", 21, "计算机科学", 4.2, "男"),
            new Student("赵六", 23, "物理", 3.9, "女"),
            new Student("孙七", 20, "数学", 3.2, "男"),
            new Student("周八", 24, "计算机科学", 3.7, "女"),
            new Student("吴九", 19, "化学", 3.5, "男"),
            new Student("郑十", 25, "物理", 3.1, "女"),
            new Student("钱一", 18, "计算机科学", 4.0, "男"),
            new Student("陈二", 26, "数学", 3.4, "女")
        );
    }
}