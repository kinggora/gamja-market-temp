package com.example.gamjamarket.Login;

import android.content.Context;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileVerifier {
    private Context context;
    public ProfileVerifier(Context mcontext){
        context = mcontext;
    }

    //in Login
    public boolean verifyProfile(String mEmail, String mPassword){
        if(mEmail.length() == 0){
            Toast.makeText(context,"이메일을 입력하세요.",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if(mPassword.length() == 0){
            Toast.makeText(context,"비밀번호를 입력하세요.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //이메일 유효성
        String Rule = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";

        Pattern p = Pattern.compile(Rule);
        Matcher m = p.matcher(mEmail);
        if(!m.matches()) {
            Toast.makeText(context,"이메일 양식이 올바르지 않습니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //비밀번호는 보안상 유효성 검사 X

        return true;
    }

    //in Register
    public boolean verifyProfile(String mEmail, String mPassword, String mPassword2, String mName, String mNickname){
        //항목 미기입
        if(mEmail.length() == 0 || mPassword.length() == 0 || mPassword2.length() == 0 || mName.length() == 0 || mNickname.length() == 0){
            Toast.makeText(context,"작성하지 않은 항목이 있습니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //이메일 유효성
        String Rule = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";

        Pattern p = Pattern.compile(Rule);
        Matcher m = p.matcher(mEmail);
        if(!m.matches()) {
            Toast.makeText(context,"이메일 양식이 올바르지 않습니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //비밀번호 유효성 (문자 1개/숫자 1개 이상 포함, 최소 8/최대 16글자, 정의된 특수문자 가능
        Rule = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$";
        p = Pattern.compile(Rule);
        m = p.matcher(mPassword);
        if(!m.matches()) {
            Toast.makeText(context,"비밀번호 양식이 올바르지 않습니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //비밀번호 유효성2
        if(!mPassword.equals(mPassword2)){
            Toast.makeText(context,"비밀번호가 일치하지 않습니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //이름 유효성 (한글만)
        Rule = "^[가-힣]*$";
        p = Pattern.compile(Rule);
        m = p.matcher(mName);
        if(!m.matches()) {
            Toast.makeText(context,"이름이 올바르지 않습니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //닉네임 유효성 (한글, 영문, 숫자, ._- 허용, 2자 이상
        Rule = "^[가-힣ㄱ-ㅎa-zA-Z0-9._-]{2,}$";
        p = Pattern.compile(Rule);
        m = p.matcher(mNickname);
        if(!m.matches()) {
            Toast.makeText(context,"닉네임이 올바르지 않습니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public boolean verifyProfile (String mNickname, String mName, String mEmail, String mPhonenumber){
        if(mEmail.length() == 0 || mPhonenumber.length() == 0 || mName.length() == 0 || mNickname.length() == 0){
            Toast.makeText(context,"작성하지 않은 항목이 있습니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //이메일 유효성
        String Rule = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";

        Pattern p = Pattern.compile(Rule);
        Matcher m = p.matcher(mEmail);
        if(!m.matches()) {
            Toast.makeText(context,"이메일 양식이 올바르지 않습니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //이름 유효성 (한글만)
        Rule = "^[가-힣]*$";
        p = Pattern.compile(Rule);
        m = p.matcher(mName);
        if(!m.matches()) {
            Toast.makeText(context,"이름이 올바르지 않습니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //닉네임 유효성 (한글, 영문, 숫자, ._- 허용, 2자 이상
        Rule = "^[가-힣ㄱ-ㅎa-zA-Z0-9._-]{2,}$";
        p = Pattern.compile(Rule);
        m = p.matcher(mNickname);
        if(!m.matches()) {
            Toast.makeText(context,"닉네임이 올바르지 않습니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }
}
