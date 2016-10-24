package com.keven.joyrun.myplugin;

import android.animation.ObjectAnimator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextInputLayout textInputLayoutPwd;
    TextInputLayout textInputLayoutName;

    Button button;
    private float mDistance;
    private long mDuration = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDistance = ScreenUtil.getScreenWidth(this) - 200;

        textInputLayoutPwd = (TextInputLayout) findViewById(R.id.text_input_pwd);
        textInputLayoutName = (TextInputLayout) findViewById(R.id.text_input_name);
        textInputLayoutName.setHint("帐号");

        button = (Button) findViewById(R.id.btn_denglu);
        button.setOnClickListener(this);
    }

    private boolean isMatch(String pwd){
        if(!TextUtils.isEmpty(pwd) && pwd.contains("111")){
            return true;
        }

        return false;
    }

    @Override
    public void onClick(View v) {
//        String pwd = textInputLayoutPwd.getEditText().getText().toString();
//
//        if(!isMatch(pwd)){
//            textInputLayoutPwd.setError("密码错误");
//            textInputLayoutName.setError("帐号错误");
//        }else {
//            textInputLayoutPwd.setErrorEnabled(false);
//            textInputLayoutName.setErrorEnabled(false);
//        }

        toAnimate();
    }

    private void toAnimate(){
//        ObjectAnimator animator = ObjectAnimator.ofFloat(button, "translationX", 0,mDistance);
        ObjectAnimator animator = new ObjectAnimator();
        animator.setDuration(mDuration);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

}
