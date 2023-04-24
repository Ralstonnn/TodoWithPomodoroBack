package test.services;

import io.github.cdimascio.dotenv.Dotenv;

public final class Env {
    private static final Dotenv INSTANCE = Dotenv.configure().load();

    public static String get(String name) {
        return INSTANCE.get(name);
    }
}
