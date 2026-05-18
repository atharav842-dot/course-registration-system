public class Course {
    int id;
    String courseCode;
    String courseName;
    String instructor;
    int credits;
    int capacity;
    int enrolled;

    public Course(int id, String courseCode, String courseName, String instructor, int credits, int capacity) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.instructor = instructor;
        this.credits = credits;
        this.capacity = capacity;
        this.enrolled = 0;
    }

    public int getAvailableSlots() {
        return capacity - enrolled;
    }

    @Override
    public String toString() {
        return String.format(
            "| %-4d | %-10s | %-30s | %-20s | %-7d | %-8d | %-8d |",
            id, courseCode, courseName, instructor, credits, capacity, enrolled
        );
    }
}
