package ru.yandex.practicum;

import exceptions.GameException;
import exceptions.InvalidWordFormatException;

import java.io.IOException;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    public static void main(String[] args) {
        boolean running = true;

        try (Logger logger = new Logger("wordle.log")) {

            try (Scanner scanner = new Scanner(System.in)) {

                while (running) {
                    showMenu();

                    int choice = Integer.parseInt(scanner.nextLine());

                    switch (choice) {
                        case 1:
                            logger.log("Запуск игры");
                            startGame(scanner, logger);
                            break;
                        case 2:
                            running = false;
                            logger.log("Выход из игры");
                            System.out.println("До свидания!");
                            break;
                        default:
                            logger.log("Выбран несуществующий пункт: " + choice);
                            System.out.println("Такого пункта нет. Попробуйте снова.");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showMenu() {
        System.out.println();
        System.out.println("=== МЕНЮ ===");
        System.out.println("1. Начать игру");
        System.out.println("2. Выход");
        System.out.print("Ваш выбор: ");
    }

    private static void startGame(Scanner scanner, Logger logger) throws IOException {

        try {
            WordleDictionaryLoader loader = new WordleDictionaryLoader();
            logger.log("Начинаем загрузку словаря");
            WordleDictionary dictionary = loader.load("words_ru.txt");
            logger.log("Словарь загружен. Количество слов: "
                    + dictionary.getSize());

            System.out.println("Правила игры:\n"
                    + "— Загадано слово из " + WordleDictionary.WORD_LENGTH + " букв\n"
                    + "— У вас " + WordleGame.MAX_STEPS + " попыток\n"
                    + "— После ввода слова отобразится подсказка:\n"
                    + "  + — буква на своем месте\n"
                    + "  ^ — буква есть в слове, но в другом месте\n"
                    + "  - — буквы нет в слове\n"
                    + "— Нажмите Enter без ввода слова, чтобы получить подсказку\n"
            );

            String secretWord = dictionary.getRandomWord();
            logger.log("Для этой игры загадано слово: " + secretWord);

            logger.log("Создание игры...");
            WordleGame game = new WordleGame(dictionary, secretWord);
            logger.log("Игра создана");

            while (!game.isGameOver()) {
                System.out.println("Осталось попыток: " + game.getSteps() + ". Введите слово:");
                String inputWord = WordleDictionary.normalize(scanner.nextLine());

                try {
                    if (inputWord.isBlank()) {
                        inputWord = game.getHintWord();
                        System.out.println(inputWord + " (подсказка)");
                        logger.log("Запрошена подсказка: " + inputWord);
                    } else {
                        logger.log("Введено слово: " + inputWord);
                    }

                    if (inputWord.length() != WordleDictionary.WORD_LENGTH) {
                        logger.log("Не верный ввод: " + inputWord);
                        throw new InvalidWordFormatException();
                    }

                    if (!game.isValidWord(inputWord)) {
                        System.out.println("Введено некорректное слово");
                        logger.log("Введено некорректное слово: " + inputWord);
                    } else if (game.isCorrectGuess(inputWord)) {
                        System.out.println("Слово угадано!");
                        logger.log("Игрок победил:" + inputWord);
                        return;
                    } else {
                        System.out.println(game.getHint(game.currentResult(inputWord)));
                    }

                } catch (GameException e) {
                    System.out.println(e.getMessage());
                    logger.log("Игровая ошибка: " + e.getMessage());
                }
            }

            System.out.println("Попытки закончились. Вы проиграли! Загаданное слово: " + game.getAnswer());
            logger.log("Игрок проиграл. Загаданное слово: " + game.getAnswer());

        } catch (IOException e) {
            System.out.println("Не удалось загрузить словарь.");
            logger.log("Ошибка загрузки словаря: " + e.getMessage());
        }





        /*


            if (!game.WordContainsInDictionary(inputWord)) {
                System.out.println("Это слово отсутствует в словаре, введите корректное слово");
            } else if (game.isCorrectGuess(inputWord)) {
                System.out.println("Слово угадано!");
                running = false;
            } else if (game.getSteps() == 0) {
                System.out.println("Попытки закончились. Вы проиграли! Загаданное слово: " + game.getAnswer());
                running = false;
            } else {
                System.out.println(game.getHint(game.currentResult(inputWord)));
            }

        }

         */
    }
}
