package ru.yandex.practicum;

import java.util.*;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    public static final int WORD_LENGTH = 5;
    private final List<String> words;
    private static final Random RANDOM = new Random();

    public List<String> getWords() {
        return words;
    }

    public WordleDictionary(List<String> words) {
        this.words = words;
    }

    public static String normalize(String word) {
        return word.trim().toLowerCase().replace("ё", "е");
    }

    public static boolean isValidGameWord(String word) {
        if (word.length() != WORD_LENGTH) return false;

        for (int i = 0; i < word.length(); i++) {
            if (!Character.isLetter(word.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public int getSize() {
        return words.size();
    }

    public String getRandomWord() {
        if (words.isEmpty()) {
            return null;
        }

        int index = RANDOM.nextInt(words.size());
        return words.get(index);
    }

    public boolean containsWord(String word) {
        return words.contains(word);
    }

    public String getSuitableWord(Set<Character> absentLetters, Set<Character> presentLetters, Character[] lettersOnCorrectPlaces, Character[][] lettersOnWrongPlaces, List<String> inputWords) {

        String suitableWord = "";

        Iterator<String> it = words.iterator();

        while (it.hasNext()) {
            String word = it.next();
            if (inputWords.contains(word)) {
                it.remove();
            } else if (isWordContainsAbsentLetters(word, absentLetters)) {
                it.remove();
            } else if (isWordDoNotContainsPresentLetters(word, presentLetters)) {
                it.remove();
            } else if (isWordLettersDoNotOnCorrectPlaces(word, lettersOnCorrectPlaces)) {
                it.remove();
            } else if (isWordLettersDoNotOnWrongPlaces(word, lettersOnWrongPlaces)) {
                it.remove();
            } else {
                suitableWord = word;
                break;
            }
        }

        return suitableWord;
    }

    private boolean isWordLettersDoNotOnWrongPlaces(String word, Character[][] lettersOnWrongPlaces) {
        boolean result = false;

        for (int i = 0; i < lettersOnWrongPlaces.length; i++) {
            for (int j = 0; j < lettersOnWrongPlaces[i].length; j++) {
                if (lettersOnWrongPlaces[i][j] != null && lettersOnWrongPlaces[i][j] == word.charAt(j)) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    private boolean isWordLettersDoNotOnCorrectPlaces(String word, Character[] lettersOnCorrectPlaces) {
        boolean result = false;
        for (int i = 0; i < word.length(); i++) {
            if (lettersOnCorrectPlaces[i] != null && lettersOnCorrectPlaces[i] != word.charAt(i)) {
                result = true;
                break;
            }
        }

        return result;
    }

    private boolean isWordDoNotContainsPresentLetters(String word, Set<Character> presentLetters) {
        boolean result;
        Set<Character> findPresentLetters = new LinkedHashSet<>();

        if (presentLetters.isEmpty()) {
            result = false;
        } else {
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (presentLetters.contains(c)) {
                    findPresentLetters.add(c);
                }
            }
            result = !(findPresentLetters.size() == presentLetters.size());
        }
        return result;
    }

    private boolean isWordContainsAbsentLetters(String word, Set<Character> absentLetters) {
        boolean result = false;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (absentLetters.contains(c)) {
                result = true;
                break;
            }
        }
        return result;
    }
}
