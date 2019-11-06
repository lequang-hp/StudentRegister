package Model;

import java.util.Date;

public class StudentRegister {
    private Subject subject;
    private Student student;
    private int totalSubjec;

    public StudentRegister() {
    }

    public StudentRegister(Subject subject, Student student, int totalSubjec) {
        this.subject = subject;
        this.student = student;
        this.totalSubjec = totalSubjec;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getTotalSubjec() {
        return totalSubjec;
    }

    public void setTotalSubjec(int totalSubjec) {
        this.totalSubjec = totalSubjec;
    }
}