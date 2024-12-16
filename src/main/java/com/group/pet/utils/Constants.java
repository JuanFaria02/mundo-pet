package com.group.pet.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Constants {
    public static final String API_PATH = "/api";

    public static final String LOGIN_PATH = "/auth/login";

    public static final String REFRESH_TOKEN_PATH = "/refreshToken";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    public static final Instant TOKEN_EXPIRATION_DATE_INSTANT = LocalDateTime.now().plusMinutes(5).toInstant(ZoneOffset.of("-03:00"));

    public static final Instant REFRESH_TOKEN_EXPIRATION_DATE_INSTANT = LocalDateTime.now().plusDays(10).toInstant(ZoneOffset.of("-03:00"));

    public static final String TOKEN_EXPIRATION_DATE_TIME = LocalDateTime.ofInstant(TOKEN_EXPIRATION_DATE_INSTANT, ZoneOffset.UTC).format(formatter);

    public static final String REFRESH_TOKEN_EXPIRATION_DATE_TIME = LocalDateTime.ofInstant(REFRESH_TOKEN_EXPIRATION_DATE_INSTANT, ZoneOffset.UTC).format(formatter);
}
