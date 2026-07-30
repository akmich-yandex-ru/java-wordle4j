package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WordleDictionaryLoaderTest {

    @Test
    void testLoadWordleDictionary() throws Exception {
        WordleDictionaryLoader loader = new WordleDictionaryLoader();
        String filename = "test/words_ru_test.txt";

        Path path = Paths.get(filename);
        assertTrue(Files.exists(path), "Файл словаря не найден. Текущий путь: " + path.toAbsolutePath());

        WordleDictionary dictionary = loader.load(path.toString());
        assertTrue(dictionary.getSize() > 0, "Словарь не должен быть пустым");
    }
}
