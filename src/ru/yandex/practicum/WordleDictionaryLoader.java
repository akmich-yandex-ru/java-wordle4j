package ru.yandex.practicum;

import exceptions.GameStateException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {

    public WordleDictionary load(String filename) throws IOException  {
        Path path = Paths.get(filename);

        if (!Files.exists(path)) {
            throw new FileNotFoundException(
                    "Файл словаря не найден: " + path.toAbsolutePath()
            );
        }

        List<String> words = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;

            while ((line = br.readLine()) != null) {
                String word = WordleDictionary.normalize(line.trim());

                if (WordleDictionary.isValidGameWord(word)) {
                    words.add(word);
                }
            }
        }

        if (words.isEmpty()) {
            throw new GameStateException("Словарь пуст");
        }

        return new WordleDictionary(words);
    }
}
