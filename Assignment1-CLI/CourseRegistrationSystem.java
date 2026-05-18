
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class CourseRegistrationSystem {

    static ArrayList<Course> courses = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static int nextId = 1;

    public static void main(String[] args) {
        seedData();

        int choice;
        do {
            printMenu();
            choice = readInt("Enter your choice: ");
            System.out.println();

            switch (choice) {
                case 1 -> addCourse();
                case 2 -> viewCourses();
                case 3 -> updateCourse();
                case 4 -> deleteCourse();
                case 5 -> searchCourses();
                case 6 -> sortCourses();
                case 7 -> System.out.println("Thank you for using the Course Registration System. Goodbye!");
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 7.");
            }

            System.out.println();
        } while (choice != 7);

        scanner.close();
    }

    // ─── MENU ────────────────────────────────────────────────────────────────────

    static void printMenu() {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║      COURSE REGISTRATION SYSTEM          ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  1. Add Course                           ║");
        System.out.println("║  2. View All Courses                     ║");
        System.out.println("║  3. Update Course                        ║");
        System.out.println("║  4. Delete Course                        ║");
        System.out.println("║  5. Search Courses                       ║");
        System.out.println("║  6. Sort Courses                         ║");
        System.out.println("║  7. Exit                                 ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }

    // ─── CREATE ──────────────────────────────────────────────────────────────────

    static void addCourse() {
        System.out.println("=== ADD COURSE ===");

        System.out.print("Course Code (e.g. CS101): ");
        String code = scanner.nextLine().trim().toUpperCase();

        if (findByCode(code) != null) {
            System.out.println("Error: A course with code \"" + code + "\" already exists.");
            return;
        }

        System.out.print("Course Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Instructor: ");
        String instructor = scanner.nextLine().trim();

        int credits = readPositiveInt("Credits: ");
        int capacity = readPositiveInt("Capacity: ");

        Course course = new Course(nextId++, code, name, instructor, credits, capacity);
        courses.add(course);

        System.out.println("\nCourse added successfully!");
        printTableHeader();
        System.out.println(course);
        printTableFooter();
    }

    // ─── READ ─────────────────────────────────────────────────────────────────────

    static void viewCourses() {
        System.out.println("=== ALL COURSES ===");

        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }

        printTableHeader();
        for (Course c : courses) {
            System.out.println(c);
        }
        printTableFooter();
        System.out.println("Total courses: " + courses.size());
    }

    // ─── UPDATE ──────────────────────────────────────────────────────────────────

    static void updateCourse() {
        System.out.println("=== UPDATE COURSE ===");

        if (courses.isEmpty()) {
            System.out.println("No courses available to update.");
            return;
        }

        viewCourses();
        int id = readInt("\nEnter Course ID to update: ");
        Course course = findById(id);

        if (course == null) {
            System.out.println("Error: No course found with ID " + id + ".");
            return;
        }

        System.out.println("\nLeave a field blank to keep its current value.");

        System.out.print("Course Code [" + course.courseCode + "]: ");
        String code = scanner.nextLine().trim().toUpperCase();
        if (!code.isEmpty()) {
            Course existing = findByCode(code);
            if (existing != null && existing.id != course.id) {
                System.out.println("Error: Course code \"" + code + "\" is already used by another course.");
                return;
            }
            course.courseCode = code;
        }

        System.out.print("Course Name [" + course.courseName + "]: ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) course.courseName = name;

        System.out.print("Instructor [" + course.instructor + "]: ");
        String instructor = scanner.nextLine().trim();
        if (!instructor.isEmpty()) course.instructor = instructor;

        System.out.print("Credits [" + course.credits + "]: ");
        String creditsInput = scanner.nextLine().trim();
        if (!creditsInput.isEmpty()) {
            try {
                int credits = Integer.parseInt(creditsInput);
                if (credits > 0) course.credits = credits;
                else System.out.println("Credits unchanged (must be positive).");
            } catch (NumberFormatException e) {
                System.out.println("Credits unchanged (invalid input).");
            }
        }

        System.out.print("Capacity [" + course.capacity + "]: ");
        String capacityInput = scanner.nextLine().trim();
        if (!capacityInput.isEmpty()) {
            try {
                int capacity = Integer.parseInt(capacityInput);
                if (capacity >= course.enrolled) {
                    course.capacity = capacity;
                } else {
                    System.out.println("Capacity unchanged (cannot be less than enrolled count of " + course.enrolled + ").");
                }
            } catch (NumberFormatException e) {
                System.out.println("Capacity unchanged (invalid input).");
            }
        }

        System.out.print("Enrolled students [" + course.enrolled + "]: ");
        String enrolledInput = scanner.nextLine().trim();
        if (!enrolledInput.isEmpty()) {
            try {
                int enrolled = Integer.parseInt(enrolledInput);
                if (enrolled >= 0 && enrolled <= course.capacity) {
                    course.enrolled = enrolled;
                } else {
                    System.out.println("Enrolled unchanged (must be between 0 and capacity " + course.capacity + ").");
                }
            } catch (NumberFormatException e) {
                System.out.println("Enrolled unchanged (invalid input).");
            }
        }

        System.out.println("\nCourse updated successfully!");
        printTableHeader();
        System.out.println(course);
        printTableFooter();
    }

    // ─── DELETE ──────────────────────────────────────────────────────────────────

    static void deleteCourse() {
        System.out.println("=== DELETE COURSE ===");

        if (courses.isEmpty()) {
            System.out.println("No courses available to delete.");
            return;
        }

        viewCourses();
        int id = readInt("\nEnter Course ID to delete: ");
        Course course = findById(id);

        if (course == null) {
            System.out.println("Error: No course found with ID " + id + ".");
            return;
        }

        System.out.print("Are you sure you want to delete \"" + course.courseName + "\"? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y") || confirm.equals("yes")) {
            courses.remove(course);
            System.out.println("Course \"" + course.courseName + "\" deleted successfully.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // ─── SEARCH (Bonus) ───────────────────────────────────────────────────────────

    static void searchCourses() {
        System.out.println("=== SEARCH COURSES ===");
        System.out.print("Enter keyword (course code, name, or instructor): ");
        String keyword = scanner.nextLine().trim().toLowerCase();

        if (keyword.isEmpty()) {
            System.out.println("Search keyword cannot be empty.");
            return;
        }

        ArrayList<Course> results = new ArrayList<>();
        for (Course c : courses) {
            if (c.courseCode.toLowerCase().contains(keyword)
                    || c.courseName.toLowerCase().contains(keyword)
                    || c.instructor.toLowerCase().contains(keyword)) {
                results.add(c);
            }
        }

        if (results.isEmpty()) {
            System.out.println("No courses found matching \"" + keyword + "\".");
        } else {
            System.out.println("Found " + results.size() + " result(s) for \"" + keyword + "\":");
            printTableHeader();
            for (Course c : results) {
                System.out.println(c);
            }
            printTableFooter();
        }
    }

    // ─── SORT (Bonus) ─────────────────────────────────────────────────────────────

    static void sortCourses() {
        System.out.println("=== SORT COURSES ===");
        System.out.println("Sort by:");
        System.out.println("  1. Course Code (A-Z)");
        System.out.println("  2. Course Name (A-Z)");
        System.out.println("  3. Credits (Low to High)");
        System.out.println("  4. Available Slots (High to Low)");
        System.out.println("  5. Instructor (A-Z)");

        int choice = readInt("Enter your choice: ");

        Comparator<Course> comparator;
        String label;

        switch (choice) {
            case 1 -> { comparator = Comparator.comparing(c -> c.courseCode); label = "Course Code (A-Z)"; }
            case 2 -> { comparator = Comparator.comparing(c -> c.courseName); label = "Course Name (A-Z)"; }
            case 3 -> { comparator = Comparator.comparingInt(c -> c.credits); label = "Credits (Low to High)"; }
            case 4 -> { comparator = Comparator.comparingInt((Course c) -> c.getAvailableSlots()).reversed(); label = "Available Slots (High to Low)"; }
            case 5 -> { comparator = Comparator.comparing(c -> c.instructor); label = "Instructor (A-Z)"; }
            default -> { System.out.println("Invalid choice."); return; }
        }

        ArrayList<Course> sorted = new ArrayList<>(courses);
        Collections.sort(sorted, comparator);

        System.out.println("\nCourses sorted by " + label + ":");
        printTableHeader();
        for (Course c : sorted) {
            System.out.println(c);
        }
        printTableFooter();
    }

    // ─── HELPERS ─────────────────────────────────────────────────────────────────

    static Course findById(int id) {
        for (Course c : courses) {
            if (c.id == id) return c;
        }
        return null;
    }

    static Course findByCode(String code) {
        for (Course c : courses) {
            if (c.courseCode.equalsIgnoreCase(code)) return c;
        }
        return null;
    }

    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    static int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value > 0) return value;
            System.out.println("Value must be greater than 0.");
        }
    }

    static void printTableHeader() {
        System.out.println("+------+------------+--------------------------------+----------------------+---------+----------+----------+");
        System.out.println("| ID   | Code       | Course Name                    | Instructor           | Credits | Capacity | Enrolled |");
        System.out.println("+------+------------+--------------------------------+----------------------+---------+----------+----------+");
    }

    static void printTableFooter() {
        System.out.println("+------+------------+--------------------------------+----------------------+---------+----------+----------+");
    }

    // ─── SEED DATA ────────────────────────────────────────────────────────────────

    static void seedData() {
        courses.add(new Course(nextId++, "CS101",  "Introduction to Programming",     "Dr. Alice Reyes",    3, 40));
        courses.add(new Course(nextId++, "CS201",  "Data Structures & Algorithms",    "Prof. Ben Torres",   3, 35));
        courses.add(new Course(nextId++, "MATH101", "Calculus I",                     "Dr. Clara Mendoza",  4, 50));
        courses.add(new Course(nextId++, "ENG101", "Technical Writing",               "Ms. Diana Cruz",     2, 30));
        courses.add(new Course(nextId++, "CS301",  "Database Management Systems",     "Prof. Edwin Santos", 3, 30));
        courses.add(new Course(nextId++, "CS401",  "Software Engineering",            "Dr. Fiona Lim",      3, 25));
        courses.add(new Course(nextId++, "PHYS101","Physics for Engineers",           "Dr. George Tan",     4, 45));
        courses.add(new Course(nextId++, "CS202",  "Object-Oriented Programming",     "Prof. Ben Torres",   3, 35));

        courses.get(0).enrolled = 38;
        courses.get(1).enrolled = 20;
        courses.get(2).enrolled = 50;
        courses.get(3).enrolled = 15;
        courses.get(4).enrolled = 28;
        courses.get(5).enrolled = 10;
        courses.get(6).enrolled = 40;
        courses.get(7).enrolled = 25;
    }
}
