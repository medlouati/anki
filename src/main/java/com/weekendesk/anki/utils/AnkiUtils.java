package com.weekendesk.anki.utils;

import com.weekendesk.anki.domain.AnkiSession;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AnkiUtils {

    private static final String ANKI_SESSION_FILENAME = "AnkiSession";
    private static final String ANKI_SESSION_EXT = ".srl";

    /**
     * Called when we start a session over. Deserializes the temp AnkiSession
     * object,
     *
     * @return The session in its state on the last end
     */
    public static AnkiSession restartSessionIfExists() {
        try {
            Path tempFile = Paths.get(ANKI_SESSION_FILENAME + ANKI_SESSION_EXT);
            if (Files.exists(tempFile)) {
                byte[] data = Files.readAllBytes(tempFile);
                try {
                    AnkiSession session = AnkiSession.class.cast(new ObjectInputStream(new ByteArrayInputStream(data)).readObject());
                    session.resumeSession();
                    return session;
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                return null;
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean endAnkiSession(AnkiSession session) {
        // Prepare the initial boxes for the next run
        session.concludeAnkiSession();

        Path tempFile = Paths.get(ANKI_SESSION_FILENAME + ANKI_SESSION_EXT);
        if (session.isDeckLearned()) {
            try {
                Files.deleteIfExists(tempFile);
                return true;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = null;

            try {
                objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(session);
            } catch (Exception cause) {
                cause.printStackTrace();
            } finally {
                try {
                    objectOutputStream.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            try {
                Files.deleteIfExists(tempFile);
                Files.write(tempFile, byteArrayOutputStream.toByteArray());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            return false;
        }
    }

}
