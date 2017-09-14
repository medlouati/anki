package com.weekendesk.anki.domain;

import java.io.Serializable;

public class Card implements Serializable {

    private static final long serialVersionUID = 6222772674837355436L;
    
    private String question;
    private String answer;

    public Card(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    
}
