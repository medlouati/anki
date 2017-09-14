package com.weekendesk.anki.pojo;

import com.weekendesk.anki.domain.Card;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Deck extends ArrayList<Card> {

    private static final String SEPARATOR = "\\|";

    public Deck(Path filePath) throws IOException {
        Reader reader = Files.newBufferedReader(filePath, Charset.forName("UTF-8"));

        List<Card> records = new BufferedReader(reader).lines()
                .skip(1)
                .map(line -> line.split(SEPARATOR))
                .filter(line -> line.length == 2)
                .map(line -> new Card(line[0], line[1]))
                .collect(Collectors.toList());

        this.addAll(records);
    }
}
