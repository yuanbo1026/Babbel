package de.babbel.code.challenge.presenter;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.babbel.code.challenge.domain.DataRepository;
import de.babbel.code.challenge.view.activity.GameActivity;

/**
 * Created by bo.yuan on 2017/3/26
 */
@Singleton
public class GamePresenter {
    private GameActivity gameActivity;
    private DataRepository dataRepository;

    @Inject
    public GamePresenter(GameActivity gameActivity, DataRepository dataRepository) {
        this.gameActivity = gameActivity;
        this.dataRepository = dataRepository;
    }
}
