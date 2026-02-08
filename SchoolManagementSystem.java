package StudentManagementSystem;

import java.io.*;
import java.util.*;

class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    String name;
    int rollNumber;
    String className;
    String section;
    Map<String, Integer> academicScores = new HashMap<>();
    Map<String, Integer> activityScores = new HashMap<>();
    String activity;
    String status;

    public Student(String name, int rollNumber, String className, String section, String status) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.className = className;
        this.section = section;
        this.status = status;
    }

    public void addActivity(String activity) {
        this.activity = activity;
    }

    public void addAcademicScore(String subject, int score) {
        academicScores.put(subject, score);
    }

    public void addActivityScore(int score) {
        if (activity != null) {
            activityScores.put(activity, score);
        }
    }

    public void updateAcademicScore(String subject, int score) {
        if (academicScores.containsKey(subject)) {
            academicScores.put(subject, score);
        } else {
            System.out.println("Subject not found.");
        }
    }

    public void updateStudentDetails(String name, String className, String section, String status) {
        this.name = name;
        this.className = className;
        this.section = section;
        this.status = status;
    }

    public void printMarksSheet() {
        System.out.println("\nMarks Sheet for " + name);
        for (String s : academicScores.keySet()) {
            System.out.println(s + " : " + academicScores.get(s));
        }
        System.out.println("Activity : " + activity +
                " Score : " + activityScores.getOrDefault(activity, 0));
        System.out.println("Status : " + status);
    }
}

public class StudentManagementSystem {

    static Map<String, Student> students = new HashMap<>();
    static Scanner scanner = new Scanner(System.in);

    static final List<String> subjects =
            Arrays.asList("Telugu", "Hindi", "English", "Math", "Science", "Social");

    static final Set<String> VALID_STATUSES =
            Set.of("ongoing", "graduated", "left");

    public static void main(String[] args) {
        loadData();

        while (true) {
            System.out.println("""
                    
                    1. Add Student Details
                    2. Add Activity
                    3. Add Scores
                    4. Update Scores
                    5. Print Mark Sheet
                    6. Display Summary
                    7. Update Record
                    8. Display Students
                    9. Display Course Average
                    10. Display Top Scorer
                    0. Exit
                    """);

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addStudentDetails();
                case 2 -> addActivity();
                case 3 -> addScores();
                case 4 -> updateScores();
                case 5 -> printMarkSheet();
                case 6 -> displaySummary();
                case 7 -> updateRecord();
                case 8 -> displayStudents();
                case 9 -> displayCourseAverage();
                case 10 -> displayTopScorer();
                case 0 -> {
                    saveData();
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // ---------- VALIDATION METHODS ----------

    static String getValidName() {
        while (true) {
            System.out.print("Enter name (letters only): ");
            String name = scanner.nextLine();
            if (name.matches("[a-zA-Z]+")) return name;
            System.out.println("Invalid name. Only letters allowed.");
        }
    }

    static int getValidRollNumber() {
        while (true) {
            System.out.print("Enter roll number (digits only): ");
            String input = scanner.nextLine();
            if (input.matches("\\d+")) return Integer.parseInt(input);
            System.out.println("Invalid roll number. Only digits allowed.");
        }
    }

    static String getValidClass() {
        while (true) {
            System.out.print("Enter class (1–10): ");
            String cls = scanner.nextLine();
            if (cls.matches("[1-9]|10")) return cls;
            System.out.println("Invalid class. Enter 1 to 10 only.");
        }
    }

    static String inputValidStatus() {
        while (true) {
            System.out.print("Status (ongoing/graduated/left): ");
            String status = scanner.nextLine().toLowerCase();
            if (VALID_STATUSES.contains(status)) return status;
            System.out.println("Invalid status.");
        }
    }

    static int getValidScore(String subject) {
        while (true) {
            System.out.print(subject + " score (0-100): ");
            try {
                int score = scanner.nextInt();
                scanner.nextLine();
                if (score >= 0 && score <= 100) return score;
            } catch (Exception e) {
                scanner.nextLine();
            }
            System.out.println("Invalid score.");
        }
    }

    // ---------- CORE METHODS ----------

    static String generateKey(int roll, String cls, String sec) {
        return roll + cls + sec;
    }

    static Student getStudent() {
        int roll = getValidRollNumber();
        System.out.print("Class: ");
        String cls = scanner.nextLine();
        System.out.print("Section: ");
        String sec = scanner.nextLine();

        String key = generateKey(roll, cls, sec);
        if (!students.containsKey(key)) {
            System.out.println("Student not found.");
            return null;
        }
        return students.get(key);
    }

    static void addStudentDetails() {
        String name = getValidName();
        int roll = getValidRollNumber();
        String cls = getValidClass();

        System.out.print("Section (letters only): ");
        String sec = scanner.nextLine();

        String status = inputValidStatus();

        students.put(generateKey(roll, cls, sec),
                new Student(name, roll, cls, sec, status));
        saveData();
    }

    static void addActivity() {
        Student s = getStudent();
        if (s == null) return;

        System.out.print("Activity: ");
        s.addActivity(scanner.nextLine());
        saveData();
    }

    static void addScores() {
        Student s = getStudent();
        if (s == null) return;

        for (String sub : subjects) {
            s.addAcademicScore(sub, getValidScore(sub));
        }
        s.addActivityScore(getValidScore("Activity"));
        saveData();
    }

    static void updateScores() {
        Student s = getStudent();
        if (s == null) return;

        System.out.print("Subject: ");
        String subject = scanner.nextLine();
        s.updateAcademicScore(subject, getValidScore(subject));
        saveData();
    }

    static void printMarkSheet() {
        Student s = getStudent();
        if (s != null) s.printMarksSheet();
    }

    static void displaySummary() {
        System.out.println("Total Students: " + students.size());
    }

    static void updateRecord() {
        Student s = getStudent();
        if (s == null) return;

        String name = getValidName();
        String cls = getValidClass();

        System.out.print("New Section: ");
        String sec = scanner.nextLine();

        String status = inputValidStatus();

        s.updateStudentDetails(name, cls, sec, status);
        saveData();
    }

    static void displayStudents() {
        students.values().forEach(s ->
                System.out.println(s.rollNumber + " " + s.name +
                        " Class " + s.className + s.section));
    }

    static void displayCourseAverage() {
        for (String sub : subjects) {
            int sum = 0, count = 0;
            for (Student s : students.values()) {
                if (s.academicScores.containsKey(sub)) {
                    sum += s.academicScores.get(sub);
                    count++;
                }
            }
            if (count > 0)
                System.out.println(sub + " Average: " + (sum / count));
        }
    }

    static void displayTopScorer() {
        Student top = null;
        int max = 0;

        for (Student s : students.values()) {
            int total = s.academicScores.values()
                    .stream().mapToInt(Integer::intValue).sum();
            if (total > max) {
                max = total;
                top = s;
            }
        }

        if (top != null)
            System.out.println("Top Scorer: " + top.name + " (" + max + ")");
    }

    static void saveData() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream("students.dat"))) {
            oos.writeObject(students);
        } catch (IOException ignored) {}
    }

    static void loadData() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream("students.dat"))) {
            students = (Map<String, Student>) ois.readObject();
        } catch (Exception ignored) {}
    }
}