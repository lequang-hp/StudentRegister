package Controller;

import Model.Student;
import Model.StudentRegister;
import Model.Subject;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class DataController {
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private PrintWriter printWriter;
    private Scanner scanner;

    public void openFileToWrite(String fileName){
        try {
            fileWriter = new FileWriter(fileName,true);
            bufferedWriter = new BufferedWriter(fileWriter);
            printWriter = new PrintWriter(bufferedWriter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void closeFileAfterWrite(String fileName){
        try {
            printWriter.close();
            bufferedWriter.close();
            fileWriter.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void writeSubjectToFile(Subject subject,String fileName){
        openFileToWrite(fileName);
        printWriter.println(subject.getSubjectID()+"|"+subject.getSubjectName()+"|"+
                subject.getTotalLesson()+"|"+subject.getSubjectType());
        closeFileAfterWrite(fileName);
    }

    public void writeStudentToFile(Student student,String fileName){
        openFileToWrite(fileName);
        printWriter.println(student.getStudentID()+"|"+student.getFullName()+"|"+
                student.getAddress()+"|"+student.getPhoneNumber());
        closeFileAfterWrite(fileName);
    }

    public void updateSRFile(ArrayList<StudentRegister> list,String fileName){
        File file = new File(fileName);
        if(file.exists())
            file.delete();

        boolean test = true;
        openFileToWrite(fileName);
        for(int i = 0; i < list.size(); i++){
            StudentRegister sr = list.get(i);
            for(int t = 0; t < i; t++){
                if(sr.getStudent().getStudentID() == list.get(t).getStudent().getStudentID())
                {
                    test = false;
                    break;
                }
            }

            if (test == true){
                printWriter.println("ID sinh vien: " + sr.getStudent().getStudentID());
                printWriter.printf("ID hoc phan: " + sr.getSubject().getSubjectID()+"|");
                for(int j = i + 1; j < list.size(); j++){
                    StudentRegister sr2 = list.get(j);
                    if(sr.getStudent().getStudentID() == sr2.getStudent().getStudentID()){
                        printWriter.printf(sr2.getSubject().getSubjectID()+"|");
                    }
                }
                printWriter.println();
                printWriter.println();
            }
        }
        closeFileAfterWrite(fileName);
    }

    public void writeSRToFile(StudentRegister sr,String fileName){
        openFileToWrite(fileName);
        printWriter.println(sr.getStudent().getStudentID()+"|"+
                sr.getSubject().getSubjectID()+"|"+sr.getTotalSubjec());
        closeFileAfterWrite(fileName);
    }

    public void openFileToRead(String fileName){
        try {
            File file = new File(fileName);
            if(!file.exists())
                file.createNewFile();
            scanner = new Scanner(Paths.get(fileName),"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void closeFileAfterRead(String fileName){
        try {
            scanner.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Subject> readSubjectsFromFile(String fileName){
        ArrayList<Subject> subjects = new ArrayList<Subject>();
        openFileToRead(fileName);
        while(scanner.hasNextLine()){
            String data = scanner.nextLine();
            Subject subject = createSubjectFromData(data);
            subjects.add(subject);
        }
        closeFileAfterRead(fileName);
        return subjects;
    }

    private Subject createSubjectFromData(String data) {
        String [] datas = data.split("\\|");
        int subjectID = Integer.parseInt(datas[0]);
        int totalLesson = Integer.parseInt(datas[2]);
        Subject subject = new Subject(subjectID,datas[1],totalLesson,datas[3]);
        return subject;
    }

    public ArrayList<Student> readStudentsFromFile(String fileName){
        ArrayList<Student> students = new ArrayList<Student>();
        openFileToRead(fileName);
        while(scanner.hasNextLine()){
            String data = scanner.nextLine();
            Student student = createStdentFromData(data);
            students.add(student);
        }
        closeFileAfterRead(fileName);
        return students;
    }

    private Student createStdentFromData(String data) {
        String [] datas = data.split("\\|");
        int studentID = Integer.parseInt(datas[0]);
        Student student = new Student(studentID,datas[1],datas[2],datas[3]);
        return student;
    }

    public ArrayList<StudentRegister> readSRsFromFile(String fileName){
        ArrayList<StudentRegister> srs = new ArrayList<StudentRegister>();
        openFileToRead(fileName);
        while(scanner.hasNextLine()){
            String data = scanner.nextLine();
            StudentRegister sr = createSRFromFile(data);
            srs.add(sr);
        }
        closeFileAfterRead(fileName);
        return  srs;
    }

    private StudentRegister createSRFromFile(String data){
        String []datas = data.split("\\|");
        Subject subject = new Subject(Integer.parseInt(datas[1]));
        Student student = new Student(Integer.parseInt(datas[0]));
        int total = Integer.parseInt(datas[2]);
        StudentRegister sr = new StudentRegister(subject,student,total);
        return sr;
    }
}