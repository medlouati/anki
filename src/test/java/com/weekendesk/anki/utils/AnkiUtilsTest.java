package com.weekendesk.anki.utils;

import com.weekendesk.anki.domain.AnkiSession;
import com.weekendesk.anki.domain.Card;
import com.weekendesk.anki.pojo.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AnkiUtilsTest {

    private AnkiSession okSession;
    private AnkiSession koSession;

    @Before
    public void init() {
        Path path = Paths.get("src/test/resources", "TestDeck.csv");
        try {
            Deck testDeck = new Deck(path);
            okSession = new AnkiSession();
            for (Card card : testDeck) {
                okSession.deposeInGreenBox(card);
            }
            koSession = new AnkiSession();
            koSession.deposeInGreenBox(testDeck.get(0));
            koSession.deposeInGreenBox(testDeck.get(1));
            koSession.deposeInOrangeBox(testDeck.get(2));
        } catch (IOException ex) {
            Assert.assertTrue("IOException", false);
        }
    }

    @Test
    public void succeededSession() {
        Assert.assertTrue(AnkiUtils.endAnkiSession(okSession));
    }

    @Test
    public void failedSession() {
        Assert.assertFalse(AnkiUtils.endAnkiSession(koSession));
    }
}
