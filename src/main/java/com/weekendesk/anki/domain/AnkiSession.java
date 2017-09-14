package com.weekendesk.anki.domain;

import com.weekendesk.anki.pojo.Deck;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Anki session. Is serialized when the session is over and
 * deserialized when it begins.
 *
 * @author medlouati
 */
public class AnkiSession implements Serializable {

    private static final long serialVersionUID = -618126296419031808L;

    // Current session initial boxes 
    private final List<Card> initialRedBox = new ArrayList<>();
    private final List<Card> initialOrangeBox = new ArrayList<>();
    private final List<Card> initialGreenBox = new ArrayList<>();

    // Deposit boxes
    private List<Card> depRedBox;
    private List<Card> depOrangeBox;
    private List<Card> depGreenBox;

    public AnkiSession() {
        super();
    }

    public AnkiSession(Deck deck) {
        super();
        depRedBox = new ArrayList<>();
        depRedBox.addAll(deck);
    }

    public void deposeInRedBox(Card card) {
        if (depRedBox == null) {
            depRedBox = new ArrayList<>();
        }

        depRedBox.add(card);
    }

    public void deposeInOrangeBox(Card card) {
        if (depOrangeBox == null) {
            depOrangeBox = new ArrayList<>();
        }

        depOrangeBox.add(card);
    }

    public void deposeInGreenBox(Card card) {
        if (depGreenBox == null) {
            depGreenBox = new ArrayList<>();
        }

        depGreenBox.add(card);
    }

    /**
     * Prepares the boxes for the next session.
     *
     */
    public void concludeAnkiSession() {
        initialRedBox.clear();
        initialOrangeBox.clear();
        initialGreenBox.clear();
        if ((depRedBox == null || depRedBox.isEmpty())
                && (depOrangeBox == null || depOrangeBox.isEmpty())) {
            initialGreenBox.addAll(depGreenBox);
        } else {
            if (depOrangeBox != null) {
                initialRedBox.addAll(depOrangeBox);
            }
            if (depGreenBox != null) {
                initialOrangeBox.addAll(depGreenBox);
            }
        }
        depRedBox = null;
        depOrangeBox = null;
        depGreenBox = null;
    }

    public boolean isDeckLearned() {
        if (initialRedBox.isEmpty() && initialOrangeBox.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isSessionEnded() {
        return depRedBox == null || depRedBox.isEmpty();
    }

    /**
     * This method empties the red box. Can be called only once every retry
     * round.
     *
     * @return
     */
    public List<Card> pullCardsToRetry() {
        if (depRedBox == null) {
            return null;
        }
        List<Card> redBoxCopy = depRedBox.subList(0, depRedBox.size());
        depRedBox = null;
        return redBoxCopy;
    }

    public void resumeSession() {
        if (depRedBox == null) {
            depRedBox = new ArrayList<>();
        }
        depRedBox.addAll(initialRedBox);

        if (depOrangeBox == null) {
            depOrangeBox = new ArrayList<>();
        }
        depOrangeBox.addAll(initialOrangeBox);

        if (depGreenBox == null) {
            depGreenBox = new ArrayList<>();
        }
        depGreenBox.addAll(initialGreenBox);
    }

    public String status() {
        StringBuffer sb = new StringBuffer("Deposit boxes status : ");
        sb.append(" : RED box : ");
        sb.append((depRedBox != null) ? depRedBox.size() : "0");
        sb.append(" | ORANGE box : ");
        sb.append((depOrangeBox != null) ? depOrangeBox.size() : "0");
        sb.append(" | GREEN box : ");
        sb.append((depGreenBox != null) ? depGreenBox.size() : "0");
        
        return sb.toString();
    }
}
