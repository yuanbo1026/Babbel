package de.babbel.code.challenge.presenter;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.babbel.code.challenge.R;
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
    private List<Word> currentSubWordList = new ArrayList<>();
    private int rightScout = 0;
    private int wrongScout = 0;
    private final int expectedRightScout = 2;

    private int randomIndex;

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

    private boolean selectedRightTranslation() {
        return currentLanguageOneWord.equals(currentLanguageTwoWord);
    }

    private void showErrorMessage(String text) {
        this.gameActivity.showErrorMessage(text);
    }

    private void setLanguage_one_text(String text) {
        gameActivity.setLanguage_one_text(text);
    }

    private void setLanguage_two_text(String text) {
        gameActivity.setLanguage_two_text(text);
    }

    private void stopAnimation() {
        gameActivity.stopAnimation();
    }

    private void startAnimation() {
        gameActivity.startAnimation();
        setupLanguageTwoWordTextView();
    }

    private Word getRandomWord(@NonNull List<Word> words) {
        getRandomInt(getRandomIndex(), words.size());
        return words.get(getRandomIndex());
    }

    private void getRandomInt(int index, int length) {
        int i = new Random().nextInt(length);
        if (index == i) {
            setRandomIndex(index + 1 >= length ? index - 1 : index + 1);
        } else {
            setRandomIndex(i);
        }
    }

    private int getWordIndex(@NonNull List<Word> words, @NonNull Word word) {
        return words.indexOf(word);
    }

    private List<Word> getSubWordList(@NonNull List<Word> words, int index) {
        List<Word> subList;
        if (index + 3 > (words.size() - 1)) {
            subList = words.subList(words.size() - 3, words.size());
        } else {
            subList = words.subList(index, index + 3);
        }
        return subList;
    }

    public void setGameData() {
        setupLanguageOneWordTextView();
        setupLanguageTwoWordTextView();
    }

    private void setupLanguageOneWordTextView() {
        currentLanguageOneWord = getRandomWord(wordList);
        currentSubWordList = getSubWordList(wordList, getWordIndex(wordList, currentLanguageOneWord));
        setLanguage_one_text(currentLanguageOneWord.getText_eng());
    }

    public void setupLanguageTwoWordTextView() {
        currentLanguageTwoWord = getRandomWord(currentSubWordList);
        setLanguage_two_text(currentLanguageTwoWord.getText_spa());
    }

    public void resetRightScout() {
        rightScout = 0;
        setRightScout();
    }

    public void resetWrongScout() {
        wrongScout = 0;
        setWrongScout();
    }

    public void rightButtonClicked() {
        if (selectedRightTranslation()) {
            stopAnimation();
            setupLanguageOneWordTextView();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startAnimation();
            updateRightScout();
            checkScout();
        } else {
            //wrong scout +1
            updateWrongScout();
        }
    }
    // 2 for testing
    private void checkScout() {
        if (rightScout == expectedRightScout) {
            this.gameActivity.stopGame();
        }
    }

    private void updateRightScout() {
        rightScout++;
        setRightScout();
    }

    private void setRightScout() {
        this.gameActivity.setRight_scout_text(String.valueOf(rightScout));
    }

    private void updateWrongScout() {
        wrongScout++;
        setWrongScout();
    }

    private void setWrongScout() {
        this.gameActivity.setWrong_scout_text(String.valueOf(wrongScout));
    }

    private int getRandomIndex() {
        return randomIndex;
    }

    private void setRandomIndex(int randomIndex) {
        this.randomIndex = randomIndex;
    }

    private void showResult(String count) {
        this.gameActivity.showResult(count);
    }

    public void countResult() {
        String message = gameActivity.getString(R.string.win_text);
        showResult(message);
    }
}
