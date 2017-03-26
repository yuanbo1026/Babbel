package de.babbel.code.challenge.di.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.babbel.code.challenge.domain.DataRepository;
import de.babbel.code.challenge.domain.repositoryimpl.DataRepositoryImpl;
import de.babbel.code.challenge.view.activity.GameActivity;

/**
 * Created by bo.yuan on 2017/3/26
 */
@Module
public class GameModule {
    private GameActivity gameActivity;

    public GameModule(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    @Provides
    @Singleton
    public Context provideContext(){
        return this.gameActivity;
    }

    @Provides
    @Singleton
    public DataRepository provideDataRepository(DataRepositoryImpl impl){
        return impl;
    }

    @Provides
    @Singleton
    public GameActivity provideGameActivity(){
        return this.gameActivity;
    }
}
