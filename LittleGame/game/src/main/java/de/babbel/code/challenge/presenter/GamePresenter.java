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
    private List<Word> currentSubWordList = new ArrayList<>();
    private int right_scout=0;
    private int wrong_scout=0;


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

    private boolean compareWords() {
        return currentLanguageOneWord.equals(currentLanguageTwoWord);
    }

    private void showErrorMessage(String text){
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

    private Word getSingleWord(@NonNull List<Word> words) {
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

    private int getIndexOfWord(@NonNull List<Word> words, @NonNull Word word) {
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

    public void initGameContent() {
        setupLanguageOneWordTextView();
        setupLanguageTwoWordTextView();
    }

    private void setupLanguageOneWordTextView() {
        currentLanguageOneWord = getSingleWord(wordList);
        currentSubWordList = getSubWordList(wordList, getIndexOfWord(wordList, currentLanguageOneWord));
        setLanguage_one_text(currentLanguageOneWord.getText_eng());
    }

    public void setupLanguageTwoWordTextView() {
        currentLanguageTwoWord = getSingleWord(currentSubWordList);
        setLanguage_two_text(currentLanguageTwoWord.getText_spa());
    }

    public void rightButtonClicked() {
        if (compareWords()) {
            stopAnimation();
            setupLanguageOneWordTextView();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startAnimation();
            //scout +1
        }
    }

    private void updateRightScout(){

    }

    private void updateWrongScout(){

    }

    private int getRandomIndex() {
        return randomIndex;
    }

    private void setRandomIndex(int randomIndex) {
        this.randomIndex = randomIndex;
    }
}
