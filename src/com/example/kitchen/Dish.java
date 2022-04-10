package com.example.kitchen;

public enum Dish {
    FISH(25),
    STEAK(30),
    SOUP(15),
    JUICE(5),
    WATER(3);

    private int duration;

    public int getDuration() {
        return duration;
    }

    Dish(int duration) {
        this.duration = duration;
    }

    public static String allDishesToString() {
        String result = "";

        for (com.example.kitchen.Dish dish : com.example.kitchen.Dish.values()) {
            if ("".equals(result)) {
                result += dish.name();
            } else {
                result += ", " + dish.name();
            }
        }
        return result;
    }
}
