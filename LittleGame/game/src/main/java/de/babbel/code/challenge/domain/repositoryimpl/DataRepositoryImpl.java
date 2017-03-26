package de.babbel.code.challenge.domain.repositoryimpl;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.babbel.code.challenge.R;
import de.babbel.code.challenge.domain.DataRepository;
import de.babbel.code.challenge.model.Word;
import rx.Observable;

/**
 * Created by bo.yuan on 2017/3/26
 */
@Singleton
public class DataRepositoryImpl implements DataRepository {
    private Context context;

    @Inject
    public DataRepositoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public Observable<List<Word>> getWordList() {
        return Observable.just(getWords());
    }

    private List<Word> getWords() {
        InputStream raw = context.getResources().openRawResource(R.raw.words);
        Reader rd = new BufferedReader(new InputStreamReader(raw));
        Type listType = new TypeToken<ArrayList<Word>>() {}.getType();
        return new Gson().fromJson(rd, listType);
    }
}
