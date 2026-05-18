public class Course {
    int id;
    String courseCode;
    String courseName;
    String instructor;
    int credits;
    int capacity;
    int enrolled;

    public Course(int id, String courseCode, String courseName, String instructor, int credits, int capacity, int enrolled) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.instructor = instructor;
        this.credits = credits;
        this.capacity = capacity;
        this.enrolled = enrolled;
    }

    public int getAvailableSlots() {
        return capacity - enrolled;
    }
}
