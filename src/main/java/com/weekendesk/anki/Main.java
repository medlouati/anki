package com.weekendesk.anki;

import com.weekendesk.anki.domain.AnkiSession;
import com.weekendesk.anki.domain.Card;
import com.weekendesk.anki.pojo.Deck;
import com.weekendesk.anki.utils.AnkiUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public final class Main {

    //You can use the file in the resources and place it under c:\
    private static final String MY_DECKCSV = "MyDeck.csv";
    
    
    public static void main(String[] args) {
        hideAnswer();

        AnkiSession session = AnkiUtils.restartSessionIfExists();
        if (session == null) {
            greatings();
            Path path = Paths.get(MY_DECKCSV);
            try {
                Deck newDeck = new Deck(path);
                session = new AnkiSession(newDeck);
            } catch (IOException ex) {
                System.err.println("OException while reading the Cards Deck");
                throw new RuntimeException(ex);
            }
        } else {
            System.out.println("*****         Welcome back !        *****");
        }

        while (!session.isSessionEnded()) {
            redBoxRetryRound(session);
        }

        boolean result = AnkiUtils.endAnkiSession(session);

        printGoodBy(result);
    }

    private static void redBoxRetryRound(AnkiSession session) throws RuntimeException {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            List<Card> retryDeck = session.pullCardsToRetry();
            System.out.print("Press enter when you are ready to start/continue going through the cards of the day ...");
            br.readLine();
            goThroughDeck(retryDeck, session);
        } catch (IOException ex) {
            System.err.println("OException while reading from user");
            throw new RuntimeException(ex);
        }
    }

    private static void goThroughDeck(List<Card> newDeck, AnkiSession session) {
        hideAnswer();
        for (Card card : newDeck) {
            char box = testCard(card);
            deposeCard(session, card, box);
            System.out.println("You put the last card in the " + (((box == 'r') ? "RED" : ((box == 'o') ? "ORANGE" : "GREEN"))) + " box");
            System.out.println(session.status());
            System.out.println();
            System.out.println();
            System.out.println();
        }
    }

    private static char testCard(Card card) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Question : " + card.getQuestion());
            System.out.println("--------");
            System.out.println();
            System.out.println();
            System.out.print("Press enter when you are ready for the answer...");
            br.readLine();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("Answer : " + card.getAnswer());
            System.out.println("------");
            String box = null;
            System.out.println();
            System.out.println();
            while (!"R".equalsIgnoreCase(box) && !"O".equalsIgnoreCase(box) && !"G".equalsIgnoreCase(box)) {
                System.out.print("Did you know the answer ? Type (R)ed / (O)range / (G)reen to select the box where you want to depose the card : ");
                box = br.readLine();
            }
            hideAnswer();
            return box.toLowerCase().charAt(0);
        } catch (IOException ex) {
            System.err.println("OException while reading from user");
            throw new RuntimeException(ex);
        }
    }

    private static void deposeCard(AnkiSession session, Card card, char box) {
        switch (box) {
            case 'r':
                session.deposeInRedBox(card);
                break;
            case 'o':
                session.deposeInOrangeBox(card);
                break;
            case 'g':
                session.deposeInGreenBox(card);
                break;
        }
    }

    private static void greatings() {
        System.out.println("Welcome to ANKI Game");
        System.out.println("Here are the rools :");
        System.out.println("When all the cards have been seen, the is over : ");
        System.out.println("  - The cards in the red box will be studied again the same day");
        System.out.println("  - The cards in the orange box will be studied again the next");
        System.out.println("  - The cards in the green box, will be studied again two days later");
        System.out.println();
        System.out.println("This being said, dont't be surprized to still have some cards to test tomorrow even if all the cards of the day are in the green box. Those are the green cards of yesterday.");
        System.out.println();
    }

    private static void printGoodBy(boolean result) {
        if (result) {
            System.out.println("**************************************************************");
            System.out.println("*                     !! Congratulations !!                  *");
            System.out.println("*                  !! You suceeded the test !!               *");
            System.out.println("**************************************************************");
        } else {
            System.out.println("**********************************************************");
            System.out.println("*          You still need to practice few cards ...      *");
            System.out.println("*                                                        *");
            System.out.println("*                  See you tomorrow ;)                   *");
            System.out.println("**********************************************************");
        }
    }

    private static void hideAnswer() {
        for (int l = 0; l < 100; l++) {
            System.out.println();
        }
    }
}
