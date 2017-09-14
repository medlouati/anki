package com.weekendesk.anki.pojo;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Assert;
import org.junit.Test;

public class DeckTest {
    
    @Test
    public void createDeck(){
        Path path = Paths.get("src/test/resources", "TestDeck.csv");
        try {
            Deck testDeck = new Deck(path);
            Assert.assertTrue(testDeck.size() == 3);
            Assert.assertEquals(testDeck.get(0).getQuestion(),"What enzyme breaks down sugars mouth and digestive tract?");
            Assert.assertEquals(testDeck.get(0).getAnswer(),"Amylase");
            Assert.assertEquals(testDeck.get(1).getQuestion(),"How is dietary cholesterol transported to target tissues?");
            Assert.assertEquals(testDeck.get(1).getAnswer(),"In chylomicrons");
            Assert.assertEquals(testDeck.get(2).getQuestion(),"What is the glucose transporter in the brain and what are its properties?");
            Assert.assertEquals(testDeck.get(2).getAnswer(),"GLUT-1 transports glucose across blood-brain barrier, GLUT-3 transports glucose into neurons.  Both are high-affinity.");
        } catch (IOException ex) {
            Assert.assertTrue("IOException", false);
        }
    }
}
