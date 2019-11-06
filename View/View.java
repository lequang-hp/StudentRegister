package View;

import Controller.ControllerUltility;
import Controller.DataController;
import Model.Student;
import Model.StudentRegister;
import Model.Subject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class View {
    public static void main(String[] args) {

        var subjectFile = "SUBJECT.DAT";
        var studentFile = "STUDENT.DAT";
        var srFile = "STUDENT_REGISTER.DAT";
        var resultFile = "RESULT.DAT";

        var subjects = new ArrayList<Subject>();
        var students = new ArrayList<Student>();
        var srs = new ArrayList<StudentRegister>();

        var controller = new DataController();
        File file1 = new File(subjectFile);
        File file2 = new File(studentFile);
        boolean ischeckSubject = false;
        boolean ischeckStudent = false;

        int choice = 0;
        Scanner scanner = new Scanner(System.in);

        do{
            System.out.println("\n--------ĐĂNG KÝ HỌC PHẦN--------");
            System.out.println("0:THOAT ĐĂNG KÝ");
            System.out.println("1:THÊM MÔN HỌC MỚI");
            System.out.println("2:DANH SÁCH CÁC MÔN HỌC");
            System.out.println("3:THÊM SINH VIÊN MỚI");
            System.out.println("4:DANNH SÁCH SINH VIÊN");
            System.out.println("5:ĐĂNG KÝ HỌC PHẦN");
            System.out.println("6:TÌM KIẾM THEO ID SINH VIEN");
            System.out.println("Bạn chọn ?: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice){
                case 0:
                    System.out.println("THOAT ĐĂNG KÝ THÀNH CÔNG");
                    break;
                case  1:
                    if(!ischeckSubject && file1.exists()){
                        checkSubjectID(controller,subjectFile);
                        ischeckSubject = true;
                    }

                    String name,type;
                    int totalLesson,sp;
                    String [] types = {"General","Specialization base","Compulsory major",
                            "Specialization options"};

                    System.out.println("Nhap ten mon hoc: ");
                    name = scanner.nextLine();
                    System.out.println("Tong so tiet:");
                    totalLesson = scanner.nextInt();
                    System.out.println("Nhap the loai: ");
                    do{
                        System.out.println("1:General\n2:Specialization base\n3:" +
                                "Compulsory major\n4:Specialization options");
                        System.out.println("Ban chon?: ");
                        sp = scanner.nextInt();
                    }while (sp <1 || sp > 4);

                    type = types[sp-1];
                    Subject subject = new Subject(0,name,totalLesson,type);
                    controller.writeSubjectToFile(subject,subjectFile);
                    System.out.println("THEM THANH CONG");
                    break;

                case 2:
                    System.out.println("--------DANH SACH MON HOC--------");
                    subjects = controller.readSubjectsFromFile(subjectFile);
                    showSubjectInfo(subjects);
                    break;

                case 3:
                    if(!ischeckStudent && file2.exists()){
                        checkStudentID(controller,studentFile);
                        ischeckStudent = true;
                    }

                    String nameStudent,address,phone;
                    System.out.println("Ten sinh vien:");
                    nameStudent = scanner.nextLine();
                    System.out.println("Dia chi:");
                    address = scanner.nextLine();
                    System.out.println("SDT: ");
                    phone = scanner.nextLine();
                    Student student = new Student(0,nameStudent,address,phone);
                    controller.writeStudentToFile(student,studentFile);
                    break;

                case 4:
                    System.out.println("--------DANH SACH SINH VIEN--------");
                    students = controller.readStudentsFromFile(studentFile);
                    showStudentInfo(students);
                    break;

                case 5:
                    subjects = controller.readSubjectsFromFile(subjectFile);
                    students = controller.readStudentsFromFile(studentFile);
                    srs = controller.readSRsFromFile(srFile);

                    int studentID = 0;
                    int subjectID = 0;

                    boolean isCheckedStudent = false;
                    boolean isCheckedSubject = false;
                    showStudentInfo(students);

                    do{
                        System.out.println("ID sinh vien: ");
                        studentID = scanner.nextInt();
                        isCheckedStudent = checkStudent(students,studentID);
                        if (!isCheckedStudent)
                            System.out.println("ID sinh vien ko ton tai");
                        else break;
                    }while(true);

                    showSubjectInfo(subjects);
                    boolean trung = false;

                    do{
                        int count = getTotal(srs,studentID); // So mon da dk
                        if(count > 8){
                            System.out.println("QUA SO LUONG DANG KY");
                            break;
                        }
                        System.out.println("ID mon hoc,da dk "+ count+" mon: ");
                        subjectID = scanner.nextInt();
                        isCheckedSubject = checkSubject(subjects,subjectID);
                        if(!isCheckedSubject)
                            System.out.println("MON HOC KO TON TAI");
                        trung = checkTrung(srs,subjectID,studentID);
                        if(trung)
                            System.out.println("MON HOC DA DANG KY TU TRUOC");
                        if (isCheckedSubject && !trung)
                            break;
                    }while (true);

                    Subject currentSubject = new Subject();
                    for(var r: subjects) {
                        if (r.getSubjectID() == subjectID)
                            currentSubject = r;
                    }

                    Student currentStudent = new Student();
                    for(var r: students){
                        if(r.getStudentID() == studentID)
                            currentStudent = r;
                    }

                    int total = getTotal(srs,studentID) + 1;
                    StudentRegister b = new StudentRegister(currentSubject,currentStudent,
                            total);

                    controller.writeSRToFile(b,srFile);
                    var ultility = new ControllerUltility();
                    srs = ultility.updateSR(srs,b);
                    controller.updateSRFile(srs,resultFile);
                    break;

                case 6:
                    int t;
                    boolean test = false;
                    students = controller.readStudentsFromFile(studentFile);
                    srs = controller.readSRsFromFile(srFile);

                    showStudentInfo(students);
                    do{
                        System.out.println("Nhap id sinh vien: ");
                        t = scanner.nextInt();
                        test = checkStudent(students,t);
                        if (!test)
                            System.out.println("ID sinh vien ko ton tai");
                        else break;
                    }while(true);

                    showInfoFromID(srs,t);
            }
        }while (choice != 0);
    }

    // Kiem tra mon hoc co bi trung
    private static boolean checkTrung(ArrayList<StudentRegister> srs, int subjectID,int studentID) {
        for(var r : srs){
            if(r.getStudent().getStudentID() == studentID){
                if(r.getSubject().getSubjectID() == subjectID)
                    return true;
            }
        }
        return false;
    }

    private static void showSubjectInfo(ArrayList<Subject> subjects) {
        for(var r: subjects){
            System.out.println(r);
        }
    }

    private static void showStudentInfo(ArrayList<Student> students){
        for(var r:students){
            System.out.println(r);
        }
    }

    private static void checkSubjectID(DataController controller, String fileName) {
        var listSubject = controller.readSubjectsFromFile(fileName);
        Subject.setId(listSubject.get(listSubject.size() - 1).getSubjectID() + 1);
    }

    private static void checkStudentID(DataController controller, String fileName) {
        var listStudent = controller.readStudentsFromFile(fileName);
        Student.setId(listStudent.get(listStudent.size() - 1).getStudentID() + 1);
    }

    // Tong so mon 1 sinh vien da dang ky
    private static int getTotal(ArrayList<StudentRegister> srs,int studentID){
        StudentRegister sr = new StudentRegister();
        for(var r: srs){
            if(r.getStudent().getStudentID() == studentID)
                sr = r;
        }

        int i = 0;
        for(var r: srs){
            if(r.getStudent().getStudentID() == studentID)
                i++;
        }
        sr.setTotalSubjec(i);
        return sr.getTotalSubjec();
    }

    //Kiem tra nhap dung id sinh vien
    private static boolean checkStudent(ArrayList<Student> students , int studentID){
        for(var r: students){
            if(r.getStudentID() == studentID)
                return  true;
        }
        return false;
    }

    // Kiem tra co nhap dung id mon hoc
    private static boolean checkSubject(ArrayList<Subject> subjects,int subjectID){
        for(var r : subjects){
            if(r.getSubjectID() == subjectID)
                return true;
        }
        return false;
    }

    // Tim kiem thong tin bang id sinh vien
    private static void showInfoFromID(ArrayList<StudentRegister> list, int studentID){
        boolean [] check = new boolean[10];
        for(int i = 0; i < check.length; i ++)
            check[i] = false;

        System.out.println("ID sinh vien: " + studentID);
        System.out.printf("ID hoc phan: ");
        for(int i = 0 ; i < list.size(); i++){
            if(list.get(i).getStudent().getStudentID() == studentID)
                System.out.printf(list.get(i).getSubject().getSubjectID()+"|");
        }
    }
}