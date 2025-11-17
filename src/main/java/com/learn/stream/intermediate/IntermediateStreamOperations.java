package com.learn.stream.intermediate;

import com.learn.stream.model.Student;
import com.learn.stream.model.Course;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Stream API中级操作示例
 * 演示聚合、分组、归约、Optional等高级操作
 */
public class IntermediateStreamOperations {
    
    public static void main(String[] args) {
        System.out.println("=== Stream API中级操作示例 ===\n");
        
        // 1. 聚合操作 (count, sum, average, max, min)
        demonstrateAggregationOperations();
        
        // 2. 分组操作 (groupingBy)
        demonstrateGroupingOperations();
        
        // 3. 归约操作 (reduce)
        demonstrateReductionOperations();
        
        // 4. Optional类使用
        demonstrateOptionalOperations();
        
        // 5. 并行Stream
        demonstrateParallelStream();
        
        // 6. 去重和限制操作 (distinct, limit, skip)
        demonstrateDistinctAndLimitOperations();
    }
    
    /**
     * 演示聚合操作
     */
    private static void demonstrateAggregationOperations() {
        System.out.println("1. 聚合操作:");
        
        List<Student> students = createSampleStudents();
        
        // 统计学生总数
        long totalStudents = students.stream().count();
        System.out.println("  学生总数: " + totalStudents);
        
        // 计算GPA总和
        double totalGpa = students.stream()
                .mapToDouble(Student::getGpa)
                .sum();
        System.out.println("  所有学生GPA总和: " + String.format("%.2f", totalGpa));
        
        // 计算平均年龄
        double avgAge = students.stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
        System.out.println("  平均年龄: " + String.format("%.2f", avgAge) + "岁");
        
        // 找到GPA最高的学生
        Optional<Student> topStudent = students.stream()
                .max(Comparator.comparingDouble(Student::getGpa));
        topStudent.ifPresent(student -> 
                System.out.println("  GPA最高学生: " + student.getName() + " (GPA: " + student.getGpa() + ")"));
        
        // 找到年龄最小的学生
        Optional<Student> youngestStudent = students.stream()
                .min(Comparator.comparingInt(Student::getAge));
        youngestStudent.ifPresent(student -> 
                System.out.println("  年龄最小学生: " + student.getName() + " (" + student.getAge() + "岁)"));
        
        System.out.println();
    }
    
    /**
     * 演示分组操作
     */
    private static void demonstrateGroupingOperations() {
        System.out.println("2. 分组操作:");
        
        List<Student> students = createSampleStudents();
        
        // 按专业分组
        Map<String, List<Student>> studentsByMajor = students.stream()
                .collect(Collectors.groupingBy(Student::getMajor));
        
        System.out.println("  按专业分组:");
        studentsByMajor.forEach((major, studentList) -> {
            System.out.println("    " + major + ": " + 
                             studentList.stream()
                                       .map(Student::getName)
                                       .collect(Collectors.joining(", ")));
        });
        
        // 按性别分组并计算每组平均GPA
        Map<String, Double> avgGpaByGender = students.stream()
                .collect(Collectors.groupingBy(
                        Student::getGender,
                        Collectors.averagingDouble(Student::getGpa)
                ));
        
        System.out.println("\n  按性别分组的平均GPA:");
        avgGpaByGender.forEach((gender, avgGpa) -> 
                System.out.println("    " + gender + ": " + String.format("%.2f", avgGpa)));
        
        // 多级分组：先按专业，再按性别
        Map<String, Map<String, List<Student>>> groupedByMajorAndGender = students.stream()
                .collect(Collectors.groupingBy(
                        Student::getMajor,
                        Collectors.groupingBy(Student::getGender)
                ));
        
        System.out.println("\n  按专业和性别分组:");
        groupedByMajorAndGender.forEach((major, genderGroups) -> {
            System.out.println("    " + major + ":");
            genderGroups.forEach((gender, studentList) -> {
                System.out.println("      " + gender + ": " + 
                                 studentList.stream()
                                           .map(Student::getName)
                                           .collect(Collectors.joining(", ")));
            });
        });
        
        System.out.println();
    }
    
    /**
     * 演示归约操作
     */
    private static void demonstrateReductionOperations() {
        System.out.println("3. 归约操作:");
        
        List<Student> students = createSampleStudents();
        
        // 计算所有学生GPA的总和 (方式1)
        Optional<Double> totalGpaOptional = students.stream()
                .map(Student::getGpa)
                .reduce(Double::sum);
        totalGpaOptional.ifPresent(totalGpa -> 
                System.out.println("  GPA总和 (方式1): " + String.format("%.2f", totalGpa)));
        
        // 计算所有学生GPA的总和 (方式2，带初始值)
        double totalGpaWithIdentity = students.stream()
                .map(Student::getGpa)
                .reduce(0.0, Double::sum);
        System.out.println("  GPA总和 (方式2): " + String.format("%.2f", totalGpaWithIdentity));
        
        // 计算GPA最高的值 (返回Optional)
        Optional<Double> maxGpa = students.stream()
                .map(Student::getGpa)
                .reduce(Double::max);
        maxGpa.ifPresent(max -> System.out.println("  最高GPA: " + String.format("%.2f", max)));
        
        // 自定义归约：连接所有学生姓名
        String allNames = students.stream()
                .map(Student::getName)
                .reduce("", (name1, name2) -> name1.isEmpty() ? name2 : name1 + ", " + name2);
        System.out.println("  所有学生姓名: " + allNames);
        
        // 统计符合条件的学生数量
        long countHighGpaStudents = students.stream()
                .map(student -> student.getGpa() > 3.5 ? 1 : 0)
                .reduce(0, Integer::sum);
        System.out.println("  GPA>3.5的学生数量: " + countHighGpaStudents);
        
        System.out.println();
    }
    
    /**
     * 演示Optional类操作
     */
    private static void demonstrateOptionalOperations() {
        System.out.println("4. Optional操作:");
        
        List<Student> students = createSampleStudents();
        
        // 查找特定学生
        Optional<Student> studentOpt = students.stream()
                .filter(student -> "王五".equals(student.getName()))
                .findFirst();
        
        System.out.println("  查找王五:");
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            System.out.println("    找到: " + student.getName() + ", " + student.getMajor());
        } else {
            System.out.println("    未找到");
        }
        
        // 使用orElse提供默认值
        Student notFoundStudent = students.stream()
                .filter(student -> "不存在的学生".equals(student.getName()))
                .findFirst()
                .orElse(new Student("默认值", 0, "未知", 0.0, "未知"));
        System.out.println("  未找到时的默认值: " + notFoundStudent.getName());
        
        // 使用orElseGet提供默认值
        Student studentOrElseGet = students.stream()
                .filter(student -> "李四".equals(student.getName()))
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("  使用orElseGet提供默认值");
                    return new Student("默认值", 0, "未知", 0.0, "未知");
                });
        System.out.println("  找到学生时的结果: " + studentOrElseGet.getName());
        
        // 使用map转换Optional内容
        String majorOfWangwu = students.stream()
                .filter(student -> "王五".equals(student.getName()))
                .findFirst()
                .map(Student::getMajor)
                .orElse("专业未知");
        System.out.println("  王五的专业: " + majorOfWangwu);
        
        // flatMap处理嵌套Optional
        Optional<String> firstMajor = students.stream()
                .filter(student -> "计算机科学".equals(student.getMajor()))
                .map(Student::getMajor)
                .findFirst();
        
        firstMajor.ifPresent(major -> 
                System.out.println("  第一个计算机科学专业: " + major));
        
        System.out.println();
    }
    
    /**
     * 演示并行Stream
     */
    private static void demonstrateParallelStream() {
        System.out.println("5. 并行Stream操作:");
        
        // 创建大量数据用于演示
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 1000000; i++) {
            numbers.add(i);
        }
        
        // 顺序Stream
        long startTime = System.currentTimeMillis();
        long countEvenSequential = numbers.stream()
                .filter(num -> num % 2 == 0)
                .count();
        long sequentialTime = System.currentTimeMillis() - startTime;
        
        System.out.println("  顺序Stream找到偶数数量: " + countEvenSequential);
        System.out.println("  顺序Stream耗时: " + sequentialTime + "ms");
        
        // 并行Stream
        startTime = System.currentTimeMillis();
        long countEvenParallel = numbers.parallelStream()
                .filter(num -> num % 2 == 0)
                .count();
        long parallelTime = System.currentTimeMillis() - startTime;
        
        System.out.println("  并行Stream找到偶数数量: " + countEvenParallel);
        System.out.println("  并行Stream耗时: " + parallelTime + "ms");
        
        // 性能提升百分比
        double improvement = ((double)(sequentialTime - parallelTime) / sequentialTime) * 100;
        System.out.println("  性能提升: " + String.format("%.1f", improvement) + "%");
        
        // 注意事项：并行Stream不保证顺序
        List<String> names = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H");
        System.out.println("\n  原始顺序: " + names);
        System.out.println("  顺序Stream: " + names.stream().collect(Collectors.toList()));
        System.out.println("  并行Stream: " + names.parallelStream().collect(Collectors.toList()));
        
        System.out.println();
    }
    
    /**
     * 演示去重和限制操作
     */
    private static void demonstrateDistinctAndLimitOperations() {
        System.out.println("6. 去重和限制操作:");
        
        // 创建重复数据的列表
        List<Integer> numbersWithDuplicates = Arrays.asList(1, 2, 3, 2, 4, 3, 5, 1, 6, 4, 7, 8, 5, 9);
        System.out.println("  原始数据: " + numbersWithDuplicates);
        
        // 去重操作
        List<Integer> distinctNumbers = numbersWithDuplicates.stream()
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        System.out.println("  去重后排序: " + distinctNumbers);
        
        // 限制前N个元素
        List<Integer> first5Numbers = numbersWithDuplicates.stream()
                .limit(5)
                .collect(Collectors.toList());
        System.out.println("  前5个元素: " + first5Numbers);
        
        // 跳过前N个元素
        List<Integer> skipFirst3 = numbersWithDuplicates.stream()
                .skip(3)
                .collect(Collectors.toList());
        System.out.println("  跳过前3个元素: " + skipFirst3);
        
        // 组合操作：去重 -> 排序 -> 取前5个
        List<Integer> complexOperation = numbersWithDuplicates.stream()
                .distinct()
                .sorted()
                .limit(5)
                .collect(Collectors.toList());
        System.out.println("  组合操作结果: " + complexOperation);
        
        // 查找GPA前三的学生
        List<Student> students = createSampleStudents();
        List<String> top3StudentsByGpa = students.stream()
                .sorted((s1, s2) -> Double.compare(s2.getGpa(), s1.getGpa()))
                .limit(3)
                .map(Student::getName)
                .collect(Collectors.toList());
        System.out.println("\n  GPA前3名学生: " + top3StudentsByGpa);
        
        // 查找除了前2名之外的所有学生
        List<String> studentsExceptTop2 = students.stream()
                .sorted((s1, s2) -> Double.compare(s2.getGpa(), s1.getGpa()))
                .skip(2)
                .map(Student::getName)
                .collect(Collectors.toList());
        System.out.println("  除了前2名的学生: " + studentsExceptTop2);
        
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
            new Student("周八", 24, "计算机科学", 3.7, "女"),
            new Student("吴九", 19, "化学", 3.5, "男"),
            new Student("郑十", 25, "物理", 3.1, "女")
        );
    }
}