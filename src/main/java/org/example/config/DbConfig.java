package org.example.config;

public record DbConfig(String url, String username, String password) {

    public static DbConfig getValuesFromEnv() {
        return new DbConfig(System.getenv("DB_URL"),
                System.getenv("DB_USER"),
                System.getenv("DB_PASSWORD"));
    }
}
