package ru.khizriev.storage.server.exception;

import java.sql.SQLException;

public class DBConnectException extends SQLException {

    public DBConnectException() {
        super("Ошибка соединения с базой данных.");
    }
}
