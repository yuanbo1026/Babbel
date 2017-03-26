package de.babbel.code.challenge.presenter;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.babbel.code.challenge.domain.DataRepository;
import de.babbel.code.challenge.model.Word;
import de.babbel.code.challenge.view.activity.GameActivity;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by bo.yuan on 2017/3/26
 */
@Singleton
public class GamePresenter {
    private GameActivity gameActivity;
    private DataRepository dataRepository;

    private List<Word> wordList = new ArrayList<>();
    private Word currentLanguageOneWord = new Word();
    private Word currentLanguageTwoWord = new Word();
    private int right_scout=0;
    private int wrong_scout=0;

    @Inject
    public GamePresenter(GameActivity gameActivity, DataRepository dataRepository) {
        this.gameActivity = gameActivity;
        this.dataRepository = dataRepository;
    }

    public void getWordList() {
        dataRepository.getWordList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Word>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showErrorMessage(e.toString());
                    }

                    @Override
                    public void onNext(List<Word> words) {
                        wordList = words;
                    }
                });
    }

    public void initGameData() {
    }

    private void setLanguage_one_text(String text) {
        gameActivity.setLanguage_one_text(text);
    }

    private void setLanguage_two_text(String text) {
        gameActivity.setLanguage_two_text(text);
    }

    private Word getSingleWord(@NonNull List<Word> words) {
        int i = new Random().nextInt(words.size());
        return words.get(i);
    }

    public void rightButtonClicked() {
        if (selectedRightTranslation()) {
            gameActivity.clearAnimation();
            setupLanguageOneWordTextView();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            gameActivity.startGame();
            //scout +1
        }
    }

    private void setupLanguageOneWordTextView() {
    }

    public void setupLanguageTwoWordTextView() {
    }

    private boolean selectedRightTranslation() {
        return currentLanguageOneWord.equals(currentLanguageTwoWord);
    }

    public void showErrorMessage(String text){
        this.gameActivity.showErrorMessage(text);
    }
}
