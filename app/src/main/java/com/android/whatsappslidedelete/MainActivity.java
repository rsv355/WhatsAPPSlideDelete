package com.android.whatsappslidedelete;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import enums.ButtonPosition;
import enums.MoveDirection;

public class MainActivity extends AppCompatActivity {
    private ImageView imgMicrophone,imgMic,imgSmile,imgTrash;
    private RelativeLayout linear;

    MoveDirection moveDirection;
    int windowwidth;
    int windowheight;

    int movementLeft;
    float initialX = 0;
    private TextView txt;



    /**
     * Touch Event
     */
    ButtonPosition currentPosition = ButtonPosition.ORIGIN;

    private float halfWidth;
    private float halfHeight;

    private float centerX;
    private float centerY;

    public float microX = 0;
    public float microY = 0;
    private TextView txtCancel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        windowwidth = getWindowManager().getDefaultDisplay().getWidth();
        windowheight = getWindowManager().getDefaultDisplay().getHeight();

        txt = (TextView)findViewById(R.id.txt);
        txtCancel = (TextView)findViewById(R.id.txtCancel);
        txtCancel.setVisibility(View.INVISIBLE);
        linear = (RelativeLayout)findViewById(R.id.linear);
        init();

    }




    public float x,y=0;
    public boolean isMoving = false;

    private void init(){

        imgSmile = (ImageView)findViewById(R.id.imgSmile);
        imgTrash = (ImageView)findViewById(R.id.imgTrash);

        imgMicrophone = (ImageView)findViewById(R.id.imgMicrophone);
        imgMic = (ImageView)findViewById(R.id.imgMic);
       // initialX = imgMicrophone.getX();

        ViewTreeObserver vto = imgMicrophone.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                microX = imgMicrophone.getX();
                microY = imgMicrophone.getY();

            }
        });

        imgMicrophone.setOnTouchListener(micTouch);

    }

    private View.OnTouchListener micTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            imgMicrophone.bringToFront();
            switch(event.getAction())
            {

                case MotionEvent.ACTION_UP:
                    isMoving = false;
                    txtCancel.setVisibility(View.INVISIBLE);
                    reset();

                    break;

                case MotionEvent.ACTION_DOWN:

                 //   Animation fadeIN = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fadein);
                //    imgMicrophone.startAnimation(fadeIN);

                    isMoving = true;
                    txtCancel.setVisibility(View.VISIBLE);
                    txt.setText("");

                    break;
                case MotionEvent.ACTION_MOVE:

                    txt.setText("");

                    if(isMoving){
                        x = event.getRawX()-imgMicrophone.getWidth()/2;

                        if(x<=(windowwidth/2)-80){
                            startDeleteAnimation();
                            reset();
                        }
                        else{
                            imgMicrophone.setX(x);

                            float tempW = x;
                            linear.getLayoutParams().width = (int) tempW;
                            linear.requestLayout();
                        }

                    }

                    break;
                default:
                    break;
            }
            return true;
        }

        private void reset() {

            Animation fadeIN = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fadeout);
            imgMicrophone.startAnimation(fadeIN);

            txt.setText("type a message");
            imgMicrophone.setX(windowwidth - imgMicrophone.getWidth() - 10);
            linear.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
            linear.requestLayout();
            txtCancel.setVisibility(View.INVISIBLE);
            imgMicrophone.bringToFront();
        }
    };

    private void startDeleteAnimation(){
        imgMic.setVisibility(View.VISIBLE);

        imgSmile.setVisibility(View.GONE);

        ObjectAnimator animation = ObjectAnimator.ofFloat(imgMic, "y", windowheight-630);
        ObjectAnimator animation2 = ObjectAnimator.ofFloat(imgMic, "rotation", 0.0f, 360f);
        final ObjectAnimator animation3 = ObjectAnimator.ofFloat(imgMic, "y", windowheight-265);

        animation3.setDuration(600);
        AnimatorSet animset = new AnimatorSet();
        animset.playTogether(animation,animation2);
        animset.setDuration(800);
        animset.setInterpolator(new AccelerateDecelerateInterpolator());
        animset.start();

        animset.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imgTrash.setVisibility(View.VISIBLE);

                Animation ai = AnimationUtils.loadAnimation(MainActivity.this,R.anim.push_up_in);
                imgTrash.startAnimation(ai);



                animation3.start();
                animation3.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {


                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        imgMic.setVisibility(View.GONE);

                        new CountDownTimer(500,500){

                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {

                                Animation ai = AnimationUtils.loadAnimation(MainActivity.this,R.anim.push_down_out);
                                imgTrash.startAnimation(ai);

                                new CountDownTimer(200,200){

                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {
                                        imgTrash.setVisibility(View.GONE);
                                        imgSmile.setVisibility(View.VISIBLE);
                                        Animation in = AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in);
                                        imgSmile.startAnimation(in);
                                    }
                                }.start();


                            }
                        }.start();


                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    //end of main class
}
