package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordleDictionaryTest {
    @Test
    void normalize() {
        String result = WordleDictionary.normalize(" ВарАн  ");
        assertEquals("варан", result);
    }
}
