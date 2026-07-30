package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class Logger implements AutoCloseable {
    private final PrintWriter printWriter;

    public Logger(String filename) throws IOException {
        this.printWriter = new PrintWriter(new FileWriter(filename, StandardCharsets.UTF_8, true));
        log("--- ЛОГ ЗАПУЩЕН ---");
    }

    public void log(String message) {
        printWriter.println("[" + LocalDateTime.now() + "] " + message);
        printWriter.flush();
    }

    public void logError(String message, Exception e) {
        log(message);
        log("ERROR: " + e.getClass().getSimpleName()
                + " - " + e.getMessage());
    }

    @Override
    public void close() {
        log("--- ЛОГ ЗАКРЫТ ---");
        printWriter.close();
    }
}
