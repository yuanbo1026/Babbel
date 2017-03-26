package de.babbel.code.challenge.domain;

import java.util.List;

import de.babbel.code.challenge.model.Word;
import rx.Observable;

/**
 * Created by bo.yuan on 2017/3/26
 */

public interface DataRepository {
    Observable<List<Word>> getWordList();
}
