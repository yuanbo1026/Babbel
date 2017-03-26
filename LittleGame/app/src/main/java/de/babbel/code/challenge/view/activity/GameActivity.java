package de.babbel.code.challenge.view.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
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
        initAnimation();
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
            }
        });
        objectAnimator.setDuration(DEFAULT_ANIMATION_DURATION);
        objectAnimator.setRepeatCount(Animation.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (language_two_text != null)
            language_two_text.clearAnimation();
    }
}
