package de.babbel.code.challenge.di.component;

import javax.inject.Singleton;

import dagger.Component;
import de.babbel.code.challenge.di.module.GameModule;
import de.babbel.code.challenge.view.activity.GameActivity;

/**
 * Created by bo.yuan on 2017/3/26
 */
@Singleton
@Component(modules = GameModule.class)
public interface GameComponent {
    void inject(GameActivity gameActivity);
}
