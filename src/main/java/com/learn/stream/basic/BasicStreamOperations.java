package com.learn.stream.basic;

import com.learn.stream.model.Student;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;

/**
 * Stream API基础操作示例
 * 演示Stream的创建、过滤、映射、排序等核心操作
 */
public class BasicStreamOperations {
    
    public static void main(String[] args) {
        System.out.println("=== Stream API基础操作示例 ===\n");
        
        // 1. 创建Stream的方式
        demonstrateStreamCreation();
        
        // 2. 过滤操作 (filter)
        demonstrateFilterOperation();
        
        // 3. 映射操作 (map, flatMap)
        demonstrateMapOperation();
        
        // 4. 排序操作 (sorted)
        demonstrateSortedOperation();
        
        // 5. 查找和匹配操作 (findFirst, anyMatch, allMatch)
        demonstrateFindAndMatchOperations();
        
        // 6. 收集操作 (collect)
        demonstrateCollectOperation();
    }
    
    /**
     * 演示Stream的不同创建方式
     */
    private static void demonstrateStreamCreation() {
        System.out.println("1. Stream创建方式:");
        
        // 方式1: 从集合创建
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");
        names.stream().forEach(name -> System.out.println("  - " + name));
        
        // 方式2: 使用Stream.of
        System.out.println("\n  使用Stream.of创建:");
        Stream.of("Java", "Python", "JavaScript", "C++")
              .forEach(language -> System.out.println("  - " + language));
        
        // 方式3: 创建数值Stream
        System.out.println("\n  创建数值Stream:");
        java.util.stream.IntStream.range(1, 6)
                                  .forEach(num -> System.out.println("  - " + num));
        
        System.out.println();
    }
    
    /**
     * 演示过滤操作
     */
    private static void demonstrateFilterOperation() {
        System.out.println("2. 过滤操作:");
        
        List<Student> students = createSampleStudents();
        
        // 过滤年龄大于20的学生
        System.out.println("  年龄大于20的学生:");
        students.stream()
                .filter(student -> student.getAge() > 20)
                .forEach(student -> System.out.println("    - " + student.getName() + ", " + student.getAge() + "岁"));
        
        // 过滤计算机专业的学生
        System.out.println("\n  计算机专业的学生:");
        students.stream()
                .filter(student -> "计算机科学".equals(student.getMajor()))
                .forEach(student -> System.out.println("    - " + student.getName()));
        
        // 组合条件过滤
        System.out.println("\n  计算机专业且GPA大于3.5的学生:");
        students.stream()
                .filter(student -> "计算机科学".equals(student.getMajor()) && student.getGpa() > 3.5)
                .forEach(student -> System.out.println("    - " + student.getName() + ", GPA: " + student.getGpa()));
        
        System.out.println();
    }
    
    /**
     * 演示映射操作
     */
    private static void demonstrateMapOperation() {
        System.out.println("3. 映射操作:");
        
        List<Student> students = createSampleStudents();
        
        // 提取姓名
        System.out.println("  所有学生姓名:");
        students.stream()
                .map(Student::getName)
                .forEach(name -> System.out.println("    - " + name));
        
        // 提取年龄并计算平方
        System.out.println("\n  学生年龄的平方:");
        students.stream()
                .mapToInt(Student::getAge)
                .map(age -> age * age)
                .forEach(ageSquare -> System.out.println("    - " + ageSquare));
        
        // flatMap示例：处理嵌套结构
        System.out.println("\n  所有课程类别:");
        List<Student> studentsWithCourses = createStudentsWithCourses();
        studentsWithCourses.stream()
                .flatMap(student -> {
                    // 这里简化为每个学生只有一门课程
                    return Arrays.stream(new String[]{student.getMajor()});
                })
                .distinct()
                .forEach(category -> System.out.println("    - " + category));
        
        System.out.println();
    }
    
    /**
     * 演示排序操作
     */
    private static void demonstrateSortedOperation() {
        System.out.println("4. 排序操作:");
        
        List<Student> students = createSampleStudents();
        
        // 按年龄排序
        System.out.println("  按年龄排序:");
        students.stream()
                .sorted((s1, s2) -> Integer.compare(s1.getAge(), s2.getAge()))
                .forEach(student -> System.out.println("    - " + student.getName() + ", " + student.getAge() + "岁"));
        
        // 按GPA降序排序
        System.out.println("\n  按GPA降序排序:");
        students.stream()
                .sorted((s1, s2) -> Double.compare(s2.getGpa(), s1.getGpa()))
                .forEach(student -> System.out.println("    - " + student.getName() + ", GPA: " + student.getGpa()));
        
        // 先按专业排序，再按年龄排序
        System.out.println("\n  按专业和年龄排序:");
        students.stream()
                .sorted((s1, s2) -> {
                    int majorCompare = s1.getMajor().compareTo(s2.getMajor());
                    if (majorCompare != 0) {
                        return majorCompare;
                    }
                    return Integer.compare(s1.getAge(), s2.getAge());
                })
                .forEach(student -> System.out.println("    - " + student.getMajor() + " - " + 
                                                     student.getName() + ", " + student.getAge() + "岁"));
        
        System.out.println();
    }
    
    /**
     * 演示查找和匹配操作
     */
    private static void demonstrateFindAndMatchOperations() {
        System.out.println("5. 查找和匹配操作:");
        
        List<Student> students = createSampleStudents();
        
        // findFirst: 找到第一个符合条件的元素
        Student firstGpaAbove4 = students.stream()
                .filter(student -> student.getGpa() > 4.0)
                .findFirst()
                .orElse(null);
        System.out.println("  第一个GPA>4.0的学生: " + 
                         (firstGpaAbove4 != null ? firstGpaAbove4.getName() : "没有找到"));
        
        // anyMatch: 是否有任何元素符合条件
        boolean hasComputerMajor = students.stream()
                .anyMatch(student -> "计算机科学".equals(student.getMajor()));
        System.out.println("  是否存在计算机专业学生: " + hasComputerMajor);
        
        // allMatch: 所有元素都符合条件
        boolean allGpaAbove3 = students.stream()
                .allMatch(student -> student.getGpa() > 3.0);
        System.out.println("  所有学生GPA都>3.0: " + allGpaAbove3);
        
        // noneMatch: 没有元素符合条件
        boolean noneGpaBelow2 = students.stream()
                .noneMatch(student -> student.getGpa() < 2.0);
        System.out.println("  没有学生的GPA<2.0: " + noneGpaBelow2);
        
        System.out.println();
    }
    
    /**
     * 演示收集操作
     */
    private static void demonstrateCollectOperation() {
        System.out.println("6. 收集操作:");
        
        List<Student> students = createSampleStudents();
        
        // 收集为List
        List<String> namesList = students.stream()
                .map(Student::getName)
                .collect(Collectors.toList());
        System.out.println("  学生姓名列表: " + namesList);
        
        // 收集为Set (去重)
        List<Integer> ages = Arrays.asList(20, 22, 20, 23, 22, 24);
        List<Integer> uniqueAges = ages.stream()
                .collect(Collectors.toList());
        System.out.println("  原始年龄列表: " + ages);
        System.out.println("  去重后年龄列表: " + uniqueAges);
        
        // 统计信息
        java.util.IntSummaryStatistics ageStats = students.stream()
                .mapToInt(Student::getAge)
                .summaryStatistics();
        System.out.println("  年龄统计 - 最大值: " + ageStats.getMax() + 
                         ", 最小值: " + ageStats.getMin() + 
                         ", 平均值: " + String.format("%.2f", ageStats.getAverage()));
        
        System.out.println();
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
            new Student("周八", 24, "计算机科学", 3.7, "女")
        );
    }
    
    /**
     * 创建带有课程的学生数据（用于演示flatMap）
     */
    private static List<Student> createStudentsWithCourses() {
        return Arrays.asList(
            new Student("张三", 20, "计算机科学", 3.8, "男"),
            new Student("李四", 22, "数学", 3.6, "女"),
            new Student("王五", 21, "计算机科学", 4.2, "男")
        );
    }
}