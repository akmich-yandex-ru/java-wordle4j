package ru.yandex.practicum;

import exceptions.GameException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
    private WordleGame game;

    @BeforeEach
    void setUp() throws IOException {
        List<String> words = List.of("трава", "велюр", "сапог");
        WordleDictionary dictionary = new WordleDictionary(words);

        Logger logger = new Logger("WordleTest.log");
        game = new WordleGame(dictionary, "велюр");
    }

    @Test
    void makeIncorrectGuess() throws GameException {
        boolean result = game.isCorrectGuess("гуава");
        assertFalse(result);
    }

    @Test
    void makeCorrectGuess() throws GameException {
        boolean result = game.isCorrectGuess("велюр");
        assertTrue(result);
    }

    @Test
    void printHintCorrectGuest() {
        String result = game.getHint(game.currentResult("велюр"));
        assertEquals("+++++", result);

    }

    @Test
    void printHintIncorrectGuest() {
        String word = "трава";
        game.isCorrectGuess(word);
        String result = game.getHint(game.currentResult(word));
        assertEquals("-^-^-", result);

    }
}
