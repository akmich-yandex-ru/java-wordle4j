package exceptions;

public class WordNotFoundInDictionaryException extends GameException {

    public WordNotFoundInDictionaryException(String message) {
        super("Нет в словаре слова: " + message);
    }
}
