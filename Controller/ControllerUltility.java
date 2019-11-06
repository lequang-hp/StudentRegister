package Controller;

import Model.StudentRegister;

import java.util.ArrayList;

public class ControllerUltility {
    public ArrayList<StudentRegister> updateSR(ArrayList<StudentRegister> list,StudentRegister sr){
        boolean isUpdate = false;

        for(int i = 0; i < list.size(); i++){
            StudentRegister b = list.get(i);
            if(b.getStudent().getStudentID() == sr.getStudent().getStudentID() &&
                    b.getSubject().getSubjectID() == sr.getSubject().getSubjectID())
            {
                list.set(i,sr);
                isUpdate = true;
                break;
            }
        }

        if(!isUpdate)
            list.add(sr);
        return list;
    }
}
