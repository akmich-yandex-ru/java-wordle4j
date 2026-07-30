package ru.yandex.practicum;

import exceptions.InvalidWordFormatException;
import exceptions.WordNotFoundInDictionaryException;

import java.util.*;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {
    public static final int MAX_STEPS = 6;

    private final WordleDictionary dictionary;
    private final String answer;
    private int steps;
    private int currentStep;
    private final List<String> inputWords = new ArrayList<>();
    private final Set<Character> absentLetters = new LinkedHashSet<>();
    private final Set<Character> presentLetters = new LinkedHashSet<>();
    private final Character[] lettersOnCorrectPlaces = new Character[5];
    private final Character[][] lettersOnWrongPlaces = new Character[MAX_STEPS][5];

    public String getAnswer() {
        return answer;
    }

    public WordleGame(WordleDictionary dictionary, String answer) {
        this.dictionary = dictionary;
        this.answer = answer;
        this.steps = MAX_STEPS;
        this.currentStep = -1;
    }

    public boolean isCorrectGuess(String guess) {
        steps--;
        currentStep++;
        inputWords.add(guess);
        return answer.equals(guess);
    }

    public boolean isValidWord(String word) {
        boolean result = true;

        if (word.length() != WordleDictionary.WORD_LENGTH) {
            result = false;
            throw new InvalidWordFormatException();
        }

        for (int i = 0; i < word.length(); i++) {
            if (!Character.isLetter(word.charAt(i))) {
                result = false;
                throw new InvalidWordFormatException();
            }
        }

        if (!dictionary.containsWord(word)) {
            result = false;
            throw new WordNotFoundInDictionaryException(word);
        }

        return result;
    }

    public LetterStatus[] currentResult(String word) {
        Map<Character, Integer> answerCounts = new HashMap<>();
        LetterStatus[] currentResult = new LetterStatus[5];

        for (int i = 0; i < WordleDictionary.WORD_LENGTH; i++) {
            char c = answer.charAt(i);
            answerCounts.put(c, answerCounts.getOrDefault(c, 0) + 1);
        }

        // точные совпадения
        for (int i = 0; i < WordleDictionary.WORD_LENGTH; i++) {
            char a = answer.charAt(i);
            char w = word.charAt(i);

            if (a == w) {
                currentResult[i] = LetterStatus.CORRECT;
                answerCounts.put(a, answerCounts.get(a) - 1);
                lettersOnCorrectPlaces[i] = a;
                presentLetters.add(a);
            }
        }

        // есть, но не там
        for (int i = 0; i < WordleDictionary.WORD_LENGTH; i++) {
            if (currentResult[i] != null) continue;

            char c = word.charAt(i);
            int count = answerCounts.getOrDefault(c, 0);

            if (count > 0) {
                currentResult[i] = LetterStatus.PRESENT;
                int countToPut = count - 1;
                answerCounts.put(c, countToPut);
                presentLetters.add(c);
                lettersOnWrongPlaces[currentStep][i] = c;
            } else {
                currentResult[i] = LetterStatus.ABSENT;

                if (!presentLetters.contains(c)) {
                    absentLetters.add(c);
                }
            }
        }

        return currentResult;
    }

    public String getHint(LetterStatus[] result) {
        StringBuilder hint = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            if (result[i] == LetterStatus.ABSENT) {
                hint.append("-");
            } else if (result[i] == LetterStatus.PRESENT) {
                hint.append("^");
            } else if (result[i] == LetterStatus.CORRECT) {
                hint.append("+");
            }
        }

        return hint.toString();
    }

    public String getHintWord() {
        String hint;

        if (inputWords.isEmpty()) {
            hint = dictionary.getRandomWord();
        } else {
            hint = dictionary.getSuitableWord(absentLetters, presentLetters, lettersOnCorrectPlaces, lettersOnWrongPlaces, inputWords);
        }

        return hint;
    }

    public int getSteps() {
        return steps;
    }

    public boolean isGameOver() {
        return steps < 1;
    }
}
