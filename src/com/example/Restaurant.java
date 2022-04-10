package com.example;

import com.example.kitchen.Cook;
import com.example.kitchen.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Restaurant {
    private static final int ORDER_CREATING_INTERVAL = 100;
    private static final LinkedBlockingQueue<Order> ORDER_QUEUE = new LinkedBlockingQueue(200);

    public static LinkedBlockingQueue<Order> getOrderQueue() {
        return ORDER_QUEUE;
    }

    public static void main(String[] args) {
        List<Tablet> tablets = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Tablet tablet = new Tablet(i + 1);
            tablet.setQueue(ORDER_QUEUE);
            tablets.add(tablet);
        }

        Cook cook1 = new Cook("Amigo");
        cook1.setQueue(ORDER_QUEUE);
        Cook cook2 = new Cook("Diego");
        cook2.setQueue(ORDER_QUEUE);

//        StatisticManager.getInstance().register(cook1);
//        StatisticManager.getInstance().register(cook2);

        Thread thread = new Thread(new RandomOrderGeneratorTask(tablets, ORDER_CREATING_INTERVAL));
        thread.start();

        try {
            Thread.sleep(1000);
            thread.interrupt();
            thread.join();
            Thread.sleep(1000);
        } catch (InterruptedException ignore) {
        }

        DirectorTablet directorTablet = new DirectorTablet();
        directorTablet.printAdvertisementProfit();
        directorTablet.printCookWorkloading();
        directorTablet.printActiveVideoSet();
        directorTablet.printArchivedVideoSet();
    }
}
