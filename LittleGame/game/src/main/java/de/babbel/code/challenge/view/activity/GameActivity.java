package de.babbel.code.challenge.view.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
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
        presenter.getWordList();

    }

    @Override
    protected void onResume() {
        super.onResume();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mScreenHeight = displaymetrics.heightPixels;
        initAnimation();
    }

    @OnClick(R.id.activity_main)
    void start() {
        if (!isGameRunning) {
            startGame();
        }
    }

    @OnClick(R.id.stop)
    void stop() {
        if (objectAnimator.isRunning()) {
            stopAnimation();
        }
    }

    @OnClick(R.id.right)
    void right() {
        presenter.rightButtonClicked();
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
                presenter.setupLanguageTwoWordTextView();
            }
        });
        objectAnimator.setDuration(DEFAULT_ANIMATION_DURATION);
        objectAnimator.setRepeatCount(Animation.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
    }

    private void startGame() {
        presenter.initGameContent();
        objectAnimator.start();
    }

    public void startAnimation() {
        objectAnimator.start();
    }

    public void stopAnimation() {
        objectAnimator.cancel();
        language_two_text.clearAnimation();
        language_two_text.setY(0);
    }

    public void setLanguage_two_text(String text){
        language_two_text.setText(text);
    }

    public void setLanguage_one_text(String text){
        language_one_text.setText(text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (language_two_text != null && objectAnimator != null){
            language_two_text.clearAnimation();
            objectAnimator.cancel();
        }
    }

    public void showErrorMessage(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
