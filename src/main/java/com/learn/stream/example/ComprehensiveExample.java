package com.learn.stream.example;

import com.learn.stream.model.Student;
import com.learn.stream.model.Course;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Stream API综合应用示例
 * 展示Stream在实际项目中的综合运用场景
 */
public class ComprehensiveExample {
    
    public static void main(String[] args) {
        System.out.println("=== Stream API综合应用示例 ===\n");
        
        // 1. 学生成绩管理系统
        demonstrateStudentGradeManagement();
        
        // 2. 课程分析系统
        demonstrateCourseAnalysis();
        
        // 3. 数据统计报告
        demonstrateDataStatistics();
        
        // 4. 复杂业务逻辑处理
        demonstrateComplexBusinessLogic();
        
        // 5. 实时数据处理模拟
        demonstrateRealTimeDataProcessing();
    }
    
    /**
     * 学生成绩管理系统
     */
    private static void demonstrateStudentGradeManagement() {
        System.out.println("1. 学生成绩管理系统:");
        
        List<Student> students = createComprehensiveStudentData();
        
        // 1.1 找出各科最高分学生
        System.out.println("  各专业最高分学生:");
        Map<String, Optional<Student>> topStudentsByMajor = students.stream()
                .collect(Collectors.groupingBy(
                        Student::getMajor,
                        Collectors.maxBy(Comparator.comparingDouble(Student::getGpa))
                ));
        
        topStudentsByMajor.forEach((major, topStudent) -> {
            topStudent.ifPresent(student -> 
                    System.out.println("    " + major + ": " + student.getName() + " (GPA: " + student.getGpa() + ")"));
        });
        
        // 1.2 计算各专业平均分和人数
        System.out.println("\n  各专业统计信息:");
        Map<String, StudentSummary> majorStats = students.stream()
                .collect(Collectors.groupingBy(
                        Student::getMajor,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                studentList -> {
                                    double avgGpa = studentList.stream()
                                            .mapToDouble(Student::getGpa)
                                            .average()
                                            .orElse(0.0);
                                    double avgAge = studentList.stream()
                                            .mapToInt(Student::getAge)
                                            .average()
                                            .orElse(0.0);
                                    return new StudentSummary(studentList.size(), avgGpa, avgAge);
                                }
                        )
                ));
        
        majorStats.forEach((major, summary) -> 
                System.out.println("    " + major + ": " + summary.count + "人, 平均GPA: " + 
                                 String.format("%.2f", summary.avgGpa) + ", 平均年龄: " + 
                                 String.format("%.1f", summary.avgAge)));
        
        // 1.3 查找有潜力升入研究生的学生
        System.out.println("\n  有研究生潜力的学生 (GPA≥3.8且年龄≤23岁):");
        List<String> graduateCandidates = students.stream()
                .filter(student -> student.getGpa() >= 3.8 && student.getAge() <= 23)
                .sorted(Comparator.comparingDouble(Student::getGpa).reversed())
                .map(student -> String.format("%s(%s, GPA: %.2f, %d岁)", 
                        student.getName(), student.getMajor(), student.getGpa(), student.getAge()))
                .collect(Collectors.toList());
        
        graduateCandidates.forEach(candidate -> System.out.println("    - " + candidate));
        
        // 1.4 按性别分组统计
        System.out.println("\n  按性别分组统计:");
        Map<String, Long> genderCount = students.stream()
                .collect(Collectors.groupingBy(
                        Student::getGender,
                        Collectors.counting()
                ));
        
        genderCount.forEach((gender, count) -> {
            double avgGpa = students.stream()
                    .filter(s -> gender.equals(s.getGender()))
                    .mapToDouble(Student::getGpa)
                    .average()
                    .orElse(0.0);
            System.out.println("    " + gender + ": " + count + "人, 平均GPA: " + 
                             String.format("%.2f", avgGpa));
        });
        
        System.out.println();
    }
    
    /**
     * 课程分析系统
     */
    private static void demonstrateCourseAnalysis() {
        System.out.println("2. 课程分析系统:");
        
        List<Course> courses = createComprehensiveCourseData();
        
        // 2.1 找出最受好评的课程类别
        System.out.println("  课程类别平均评分:");
        Map<String, Double> categoryAvgScore = courses.stream()
                .collect(Collectors.groupingBy(
                        Course::getCategory,
                        Collectors.averagingDouble(Course::getScore)
                ));
        
        categoryAvgScore.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(entry -> System.out.println("    " + entry.getKey() + ": " + 
                        String.format("%.2f", entry.getValue())));
        
        // 2.2 找出每个老师的课程数量
        System.out.println("\n  各老师授课数量:");
        Map<String, Long> courseCountByInstructor = courses.stream()
                .collect(Collectors.groupingBy(
                        Course::getInstructor,
                        Collectors.counting()
                ));
        
        courseCountByInstructor.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(entry -> System.out.println("    " + entry.getKey() + ": " + entry.getValue() + "门课程"));
        
        // 2.3 高学分课程分析
        System.out.println("\n  高学分课程统计 (≥3学分):");
        long highCreditCount = courses.stream()
                .filter(course -> course.getCredit() >= 3)
                .count();
        
        double avgScoreHighCredit = courses.stream()
                .filter(course -> course.getCredit() >= 3)
                .mapToDouble(Course::getScore)
                .average()
                .orElse(0.0);
        
        System.out.println("    数量: " + highCreditCount + "门");
        System.out.println("    平均评分: " + String.format("%.2f", avgScoreHighCredit));
        
        // 2.4 优秀课程推荐 (评分≥85且≥3学分)
        System.out.println("\n  优秀课程推荐:");
        List<String> excellentCourses = courses.stream()
                .filter(course -> course.getScore() >= 85 && course.getCredit() >= 3)
                .sorted(Comparator.comparingDouble(Course::getScore).reversed()
                        .thenComparing(Course::getCredit).reversed())
                .map(course -> String.format("%s (%s教授, %d学分, %.0f分)", 
                        course.getCourseName(), course.getInstructor(), 
                        course.getCredit(), course.getScore()))
                .collect(Collectors.toList());
        
        excellentCourses.forEach(course -> System.out.println("    ★ " + course));
        
        System.out.println();
    }
    
    /**
     * 数据统计报告
     */
    private static void demonstrateDataStatistics() {
        System.out.println("3. 数据统计报告:");
        
        List<Student> students = createComprehensiveStudentData();
        List<Course> courses = createComprehensiveCourseData();
        
        // 3.1 学生整体统计
        System.out.println("  学生数据统计:");
        IntSummaryStatistics ageStats = students.stream()
                .mapToInt(Student::getAge)
                .summaryStatistics();
        
        DoubleSummaryStatistics gpaStats = students.stream()
                .mapToDouble(Student::getGpa)
                .summaryStatistics();
        
        System.out.println("    年龄统计: 最小" + ageStats.getMin() + "岁, 最大" + 
                         ageStats.getMax() + "岁, 平均" + String.format("%.1f", ageStats.getAverage()) + "岁");
        System.out.println("    GPA统计: 最低" + String.format("%.2f", gpaStats.getMin()) + 
                         ", 最高" + String.format("%.2f", gpaStats.getMax()) + 
                         ", 平均" + String.format("%.2f", gpaStats.getAverage()));
        
        // 3.2 专业分布分析
        System.out.println("\n  专业分布分析:");
        Map<String, Long> majorDistribution = students.stream()
                .collect(Collectors.groupingBy(Student::getMajor, Collectors.counting()));
        
        long totalStudents = students.size();
        majorDistribution.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(entry -> {
                    double percentage = (entry.getValue() * 100.0) / totalStudents;
                    System.out.println("    " + entry.getKey() + ": " + entry.getValue() + 
                                     "人 (" + String.format("%.1f", percentage) + "%)");
                });
        
        // 3.3 成绩等级分布
        System.out.println("\n  成绩等级分布:");
        Map<String, Long> gradeDistribution = students.stream()
                .collect(Collectors.groupingBy(
                        student -> {
                            double gpa = student.getGpa();
                            if (gpa >= 4.0) return "优秀(4.0-4.0)";
                            else if (gpa >= 3.7) return "良好(3.7-3.9)";
                            else if (gpa >= 3.3) return "中等(3.3-3.6)";
                            else if (gpa >= 3.0) return "及格(3.0-3.2)";
                            else return "不及格(<3.0)";
                        },
                        Collectors.counting()
                ));
        
        gradeDistribution.forEach((grade, count) -> {
            double percentage = (count * 100.0) / totalStudents;
            System.out.println("    " + grade + ": " + count + "人 (" + 
                             String.format("%.1f", percentage) + "%)");
        });
        
        // 3.4 性别比例分析
        System.out.println("\n  性别比例分析:");
        Map<String, Long> genderStats = students.stream()
                .collect(Collectors.groupingBy(Student::getGender, Collectors.counting()));
        
        genderStats.forEach((gender, count) -> {
            double percentage = (count * 100.0) / totalStudents;
            System.out.println("    " + gender + ": " + count + "人 (" + 
                             String.format("%.1f", percentage) + "%)");
        });
        
        System.out.println();
    }
    
    /**
     * 复杂业务逻辑处理
     */
    private static void demonstrateComplexBusinessLogic() {
        System.out.println("4. 复杂业务逻辑处理:");
        
        List<Student> students = createComprehensiveStudentData();
        List<Course> courses = createComprehensiveCourseData();
        
        // 4.1 综合评估学生潜力
        System.out.println("  学生综合潜力评估:");
        List<String> studentAssessments = students.stream()
                .filter(student -> student.getAge() >= 18 && student.getAge() <= 25)
                .map(student -> {
                    double ageScore = 25 - student.getAge(); // 年龄越小得分越高
                    double gpaScore = student.getGpa() * 25; // GPA得分
                    double potentialScore = (ageScore * 0.3 + gpaScore * 0.7);
                    
                    String potentialLevel;
                    if (potentialScore >= 90) potentialLevel = "极高";
                    else if (potentialScore >= 80) potentialLevel = "很高";
                    else if (potentialScore >= 70) potentialLevel = "较高";
                    else if (potentialScore >= 60) potentialLevel = "中等";
                    else potentialLevel = "待提升";
                    
                    return String.format("%s(%s): %.1f分(%s)", 
                            student.getName(), student.getMajor(), potentialScore, potentialLevel);
                })
                .sorted((s1, s2) -> {
                    double score1 = Double.parseDouble(s1.split(": ")[1].split("分")[0]);
                    double score2 = Double.parseDouble(s2.split(": ")[1].split("分")[0]);
                    return Double.compare(score2, score1);
                })
                .limit(8)
                .collect(Collectors.toList());
        
        studentAssessments.forEach(assessment -> System.out.println("    " + assessment));
        
        // 4.2 课程推荐系统
        System.out.println("\n  个性化课程推荐:");
        Map<String, List<String>> courseRecommendations = students.stream()
                .filter(student -> student.getGpa() >= 3.5)
                .collect(Collectors.groupingBy(
                        Student::getMajor,
                        Collectors.mapping(
                                student -> {
                                    // 根据学生GPA推荐合适难度的课程
                                    List<String> recommendations = courses.stream()
                                            .filter(course -> {
                                                if (student.getGpa() >= 4.0) {
                                                    return course.getScore() >= 80; // 高GPA推荐高质量课程
                                                } else if (student.getGpa() >= 3.7) {
                                                    return course.getScore() >= 75;
                                                } else {
                                                    return course.getScore() >= 70;
                                                }
                                            })
                                            .map(Course::getCourseName)
                                            .limit(3)
                                            .collect(Collectors.toList());
                                    return student.getName() + ": " + 
                                           String.join(", ", recommendations);
                                },
                                Collectors.toList()
                        )
                ));
        
        courseRecommendations.forEach((major, recommendations) -> {
            System.out.println("    " + major + "专业:");
            recommendations.forEach(rec -> System.out.println("      - " + rec));
        });
        
        // 4.3 风险学生预警
        System.out.println("\n  学业风险预警 (GPA<3.0或年龄>24):");
        List<String> riskWarnings = students.stream()
                .filter(student -> student.getGpa() < 3.0 || student.getAge() > 24)
                .map(student -> {
                    List<String> riskFactors = new ArrayList<>();
                    if (student.getGpa() < 3.0) riskFactors.add("GPA偏低");
                    if (student.getAge() > 24) riskFactors.add("年龄偏大");
                    if (student.getGpa() < 3.0 && student.getAge() > 24) 
                        riskFactors.add("双重风险");
                    
                    return String.format("%s: %s (%s)", student.getName(), 
                            student.getMajor(), String.join(", ", riskFactors));
                })
                .collect(Collectors.toList());
        
        riskWarnings.forEach(warning -> System.out.println("    ⚠ " + warning));
        
        System.out.println();
    }
    
    /**
     * 实时数据处理模拟
     */
    private static void demonstrateRealTimeDataProcessing() {
        System.out.println("5. 实时数据处理模拟:");
        
        // 模拟实时学生数据流
        System.out.println("  模拟实时成绩更新处理:");
        List<Student> students = createComprehensiveStudentData();
        
        // 模拟新的成绩数据
        List<StudentScoreUpdate> scoreUpdates = Arrays.asList(
                new StudentScoreUpdate("张三", 3.9),
                new StudentScoreUpdate("李四", 3.4),
                new StudentScoreUpdate("王五", 4.3),
                new StudentScoreUpdate("赵六", 3.8)
        );
        
        // 处理成绩更新
        Map<String, Double> updatedGpas = scoreUpdates.stream()
                .collect(Collectors.toMap(
                        StudentScoreUpdate::getName,
                        StudentScoreUpdate::getNewGpa
                ));
        
        // 实时排名更新
        List<String> updatedRankings = students.stream()
                .map(student -> {
                    Double newGpa = updatedGpas.get(student.getName());
                    double finalGpa = newGpa != null ? newGpa : student.getGpa();
                    return new Student(student.getName(), student.getAge(), 
                                     student.getMajor(), finalGpa, student.getGender());
                })
                .sorted(Comparator.comparingDouble(Student::getGpa).reversed())
                .map(student -> {
                    Double newGpa = updatedGpas.get(student.getName());
                    String status = newGpa != null ? " [已更新]" : "";
                    return String.format("%s: %.2f%s", student.getName(), 
                            student.getGpa(), status);
                })
                .limit(6)
                .collect(Collectors.toList());
        
        updatedRankings.forEach(ranking -> System.out.println("    " + ranking));
        
        // 实时统计信息
        System.out.println("\n  实时统计信息:");
        double avgGpaAfterUpdate = students.stream()
                .mapToDouble(student -> {
                    Double newGpa = updatedGpas.get(student.getName());
                    return newGpa != null ? newGpa : student.getGpa();
                })
                .average()
                .orElse(0.0);
        
        long excellentCount = (int) students.stream()
                .mapToDouble(student -> {
                    Double newGpa = updatedGpas.get(student.getName());
                    return newGpa != null ? newGpa : student.getGpa();
                })
                .filter(gpa -> gpa >= 4.0)
                .count();
        
        System.out.println("    实时平均GPA: " + String.format("%.2f", avgGpaAfterUpdate));
        System.out.println("    优秀学生数: " + excellentCount + "人");
        
        // 异常监控
        System.out.println("\n  成绩异常监控:");
        scoreUpdates.stream()
                .filter(update -> update.getNewGpa() > 4.2 || update.getNewGpa() < 2.0)
                .forEach(update -> System.out.println("    ⚠ 异常成绩: " + 
                        update.getName() + " - " + update.getNewGpa()));
        
        System.out.println();
    }
    
    /**
     * 综合学生数据
     */
    private static List<Student> createComprehensiveStudentData() {
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
                new Student("陈二", 26, "数学", 3.4, "女"),
                new Student("刘三", 21, "计算机科学", 3.3, "女"),
                new Student("杨四", 22, "物理", 3.8, "男"),
                new Student("黄五", 20, "化学", 3.7, "男"),
                new Student("林六", 23, "数学", 3.9, "女"),
                new Student("何七", 19, "计算机科学", 4.1, "女")
        );
    }
    
    /**
     * 综合课程数据
     */
    private static List<Course> createComprehensiveCourseData() {
        return Arrays.asList(
                new Course("数据结构与算法", "张教授", 4, 88, "专业课"),
                new Course("高等数学", "李教授", 5, 82, "基础课"),
                new Course("线性代数", "王教授", 3, 85, "基础课"),
                new Course("概率论", "赵教授", 4, 79, "基础课"),
                new Course("操作系统", "孙教授", 4, 91, "专业课"),
                new Course("数据库原理", "周教授", 3, 87, "专业课"),
                new Course("计算机网络", "吴教授", 4, 83, "专业课"),
                new Course("软件工程", "郑教授", 3, 86, "专业课"),
                new Course("人工智能", "钱教授", 4, 89, "专业课"),
                new Course("机器学习", "陈教授", 4, 92, "专业课"),
                new Course("编译原理", "刘教授", 3, 84, "专业课"),
                new Course("离散数学", "杨教授", 4, 80, "基础课")
        );
    }
    
    /**
     * 学生成绩更新记录
     */
    static class StudentScoreUpdate {
        private String name;
        private double newGpa;
        
        public StudentScoreUpdate(String name, double newGpa) {
            this.name = name;
            this.newGpa = newGpa;
        }
        
        public String getName() { return name; }
        public double getNewGpa() { return newGpa; }
    }
    
    /**
     * 学生统计摘要
     */
    static class StudentSummary {
        int count;
        double avgGpa;
        double avgAge;
        
        public StudentSummary(int count, double avgGpa, double avgAge) {
            this.count = count;
            this.avgGpa = avgGpa;
            this.avgAge = avgAge;
        }
    }
}