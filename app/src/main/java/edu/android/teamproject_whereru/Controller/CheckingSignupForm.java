package edu.android.teamproject_whereru.Controller;

import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.android.teamproject_whereru.LoginActivity;
import edu.android.teamproject_whereru.Model.Guest;

public class CheckingSignupForm {

    // 회원가입 정규식 관련 클래스

    public static final Pattern VALID_ID_REGEX = Pattern.compile("^[a-zA-z]{1}[a-zA-z0-9]*$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_NAME = Pattern.compile("^[가-힣]{2,4}$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_PHONE_REGEX = Pattern.compile("^010-[0-9]{4}-[0-9]{4}$", Pattern.CASE_INSENSITIVE);


    public CheckingSignupForm(){};

    // 아이디에 대한 패턴검사
    public static boolean validateId(String idStr) {
        Matcher matcher = VALID_ID_REGEX.matcher(idStr);
        return matcher.find();
    }

    // 이메일에 관한 패턴검사
    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    // 이름에 관한 패턴검사
    public static boolean validateName(String nameStr) {
        Matcher matcher = VALID_NAME.matcher(nameStr);
        return matcher.find();
    }

    // 폰번호에 대한 패턴검사
    public static boolean validatePhone(String phoneStr) {
        Matcher matcher = VALID_PHONE_REGEX.matcher(phoneStr);
        return matcher.find();
    }
}
