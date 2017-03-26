package de.babbel.code.challenge.model;

/**
 * Created by bo.yuan on 2017/3/26
 */

public class Word {
    /**
     * text_eng : primary school
     * text_spa : escuela primaria
     */

    private String text_eng;
    private String text_spa;

    public String getText_eng() {
        return text_eng;
    }

    public void setText_eng(String text_eng) {
        this.text_eng = text_eng;
    }

    public String getText_spa() {
        return text_spa;
    }

    public void setText_spa(String text_spa) {
        this.text_spa = text_spa;
    }
}
