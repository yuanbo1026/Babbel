package de.babbel.code.challenge.view.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.babbel.code.challenge.R;
import de.babbel.code.challenge.di.component.DaggerGameComponent;
import de.babbel.code.challenge.di.module.GameModule;
import de.babbel.code.challenge.presenter.GamePresenter;

public class GameActivity extends AppCompatActivity {
    @BindView(R.id.language_one_text)
    TextView language_one_text;
    @BindView(R.id.language_two_text)
    TextView language_two_text;
    @BindView(R.id.right)
    TextView right;
    @BindView(R.id.wrong)
    TextView wrong;
    @BindView(R.id.right_answer_textview)
    TextView right_answer_textview;
    @BindView(R.id.wrong_answer_textview)
    TextView wrong_answer_textview;

    @Inject
    GamePresenter presenter;

    private Unbinder unbinder;
    protected float mScreenHeight;
    public static final long DEFAULT_ANIMATION_DURATION = 4500L;
    private ObjectAnimator objectAnimator;
    private boolean isGameRunning = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_layout);
        unbinder = ButterKnife.bind(this);
        DaggerGameComponent.builder().gameModule(new GameModule(this)).build().inject(this);
        initAnimation();
        startAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mScreenHeight = displaymetrics.heightPixels;
    }

    private void initAnimation() {
        objectAnimator = ObjectAnimator.ofFloat(language_two_text, "translationY", 0, mScreenHeight);
        objectAnimator.addListener(new ValueAnimator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                isGameRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isGameRunning = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isGameRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.d("Animation repeat","Animation repeat");
            }
        });
        objectAnimator.setDuration(DEFAULT_ANIMATION_DURATION);
        objectAnimator.setRepeatCount(Animation.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
    }

    @OnClick(R.id.game_frame)
    public void tapScreen(){
        if (!isGameRunning) {
            startGame();
        }
    }

    @OnClick(R.id.right)
    void right() {
        presenter.rightButtonClicked();
    }

    public void updateRightScout(String scout){
        wrong_answer_textview.setText(this.getString(R.string.right_scout_text) + scout);
    }

    public void updateWrongScout(String scout){
        wrong_answer_textview.setText(this.getString(R.string.wrong_scout_text) + scout);
    }

    public void startGame() {
        presenter.initGameData();
        objectAnimator.start();
    }

    public void startAnimation() {
        objectAnimator.start();
        presenter.setupLanguageTwoWordTextView();
    }

    public void clearAnimation() {
        objectAnimator.cancel();
        language_two_text.clearAnimation();
        language_two_text.setY(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (language_two_text != null)
            language_two_text.clearAnimation();
    }

    public void showErrorMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    public void setLanguage_two_text(String text){
        language_two_text.setText(text);
    }

    public void setLanguage_one_text(String text){
        language_one_text.setText(text);
    }
}
