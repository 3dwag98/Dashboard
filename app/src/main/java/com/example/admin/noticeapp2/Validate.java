package com.example.admin.noticeapp2;

import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validate  {
    public Validate() {
    }

    public boolean isValidString(EditText text){
        String charPattern = "^[a-zA-Z]*$";

        String charP = text.getText().toString().trim();
        if(charP.matches(charPattern) && charP.length()>0){
            return true;
        }
        text.setError("Character's only !!");
        text.requestFocus();
        return false;
    }

    public boolean isValidEmail(EditText text){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        String email = text.getText().toString().trim();
        if(email.matches(emailPattern) && email.length()>0){
            return true;
        }
        text.setError("Invalid Email");
        text.requestFocus();
        return false;
    }

    public  boolean isValidPassword(EditText pass) {
         Pattern pattern;
         Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        String passwd = pass.getText().toString().trim();
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(passwd);
        if(passwd.length() < 8){
            pass.setError("Password must contain atleast 8 characters");
            pass.requestFocus();
            return false;
        }
        if(passwd.length() > 20){
            pass.setError("Password must not exceeds 20 characters");
            pass.requestFocus();
            return false;
        }

        if(!(matcher.matches())){
            pass.setError("Password must contain at least 1 Alphabet, 1 Number and 1 Special Character");
            pass.requestFocus();
            return false;
        }

        return true;

    }

    public boolean isValidNumber(EditText phone){
        String num = phone.getText().toString().trim();
        if(TextUtils.isEmpty(num)){
            phone.requestFocus();
            phone.setError("Field Required");
            return false;
        }
        if( !(num.matches("((\\+*)((0[ -]+)*|(91 )*)(\\d{12}+|\\d{10}+))|\\d{5}([- ]*)\\d{6}"))){
            phone.requestFocus();
            phone.setError("Invalid Phone Number");
            return false;
        }

        return true;
    }


    public boolean isValidString(EditText text, TextInputLayout error){
        String charPattern = "^[a-zA-Z]*$";

        String charP = text.getText().toString().trim();
        if(charP.matches(charPattern) && charP.length()>0){
            return true;
        }
        error.setError("Character's only !!");
        error.requestFocus();
        return false;
    }

    public boolean isValidEmail(EditText text, TextInputLayout error){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        String email = text.getText().toString().trim();
        if(email.matches(emailPattern) && email.length()>0){
            return true;
        }
        error.setError("Invalid Email");
        error.requestFocus();
        return false;
    }

    public  boolean isValidPassword(EditText pass, TextInputLayout error) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        String passwd = pass.getText().toString().trim();
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(passwd);
        if(passwd.length() < 8){
            error.setError("Password must contain atleast 8 characters");
            error.requestFocus();
            return false;
        }
        if(passwd.length() > 20){
            error.setError("Password must not exceeds 20 characters");
            error.requestFocus();
            return false;
        }

        if(!(matcher.matches())){
            error.setError("Password must contain at least 1 Alphabet, 1 Number and 1 Special Character");
            error.requestFocus();
            return false;
        }

        return true;

    }

    public boolean isValidNumber(EditText phone, TextInputLayout error){
        String num = phone.getText().toString().trim();
        if(TextUtils.isEmpty(num)){
            error.requestFocus();
            error.setError("Field Required");
            return false;
        }
        if( !(num.matches("((\\+*)((0[ -]+)*|(91 )*)(\\d{12}+|\\d{10}+))|\\d{5}([- ]*)\\d{6}"))){
            error.requestFocus();
            error.setError("Invalid Phone Number");
            return false;
        }

        return true;
    }
}
