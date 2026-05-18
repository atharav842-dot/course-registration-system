import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class CourseTableModel extends AbstractTableModel {

    private final String[] columns = {"ID", "Code", "Course Name", "Instructor", "Credits", "Capacity", "Enrolled", "Available"};
    private ArrayList<Course> courses;

    public CourseTableModel(ArrayList<Course> courses) {
        this.courses = courses;
    }

    @Override
    public int getRowCount() {
        return courses.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Course c = courses.get(row);
        return switch (col) {
            case 0 -> c.id;
            case 1 -> c.courseCode;
            case 2 -> c.courseName;
            case 3 -> c.instructor;
            case 4 -> c.credits;
            case 5 -> c.capacity;
            case 6 -> c.enrolled;
            case 7 -> c.getAvailableSlots();
            default -> "";
        };
    }

    @Override
    public Class<?> getColumnClass(int col) {
        return switch (col) {
            case 0, 4, 5, 6, 7 -> Integer.class;
            default -> String.class;
        };
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public Course getCourseAt(int row) {
        return courses.get(row);
    }

    public void refresh() {
        fireTableDataChanged();
    }
}
