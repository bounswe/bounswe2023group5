package com.app.gamereview.util;

public class UtilExtensions {
    public static boolean isUUID(String str) {
        return str.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
    }
}
