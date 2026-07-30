package exceptions;

public class InvalidWordFormatException extends RuntimeException {

    public InvalidWordFormatException() {
        super("Введите слово из 5 русских букв");
    }
}
