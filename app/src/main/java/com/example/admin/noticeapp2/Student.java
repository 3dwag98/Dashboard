package com.example.admin.noticeapp2;

public class Student {
    String fname,lname,uname,std,email,profile_pic;
    String mobile_no;
    public Student(String fname,String lname,String uname,String email,String mobile_no){
        this.fname = fname;
        this.uname = uname;
        this.lname = lname;
        this.email = email;
        this.mobile_no = mobile_no;
    }

    public Student(String fname, String lname, String uname, String email, String std, String profile_pic, String mobile_no) {
        this.fname = fname;
        this.lname = lname;
        this.uname = uname;
        this.email = email;
        this.std = std;
        this.profile_pic = profile_pic;
        this.mobile_no = mobile_no;
    }

    public String getStd() {
        return std;
    }

    public void setStd(String std) {
        this.std = std;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }




    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }
}
