package com.example.kitchen;

import com.example.ConsoleHelper;
import com.example.Restaurant;
import com.example.Tablet;
import com.example.statistic.StatisticManager;
import com.example.statistic.event.CookedOrderEventDataRow;

import java.util.Observable;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class Cook extends Observable implements Runnable {
    private final String name;
    private LinkedBlockingQueue queue = new LinkedBlockingQueue(200);

    public void setQueue(LinkedBlockingQueue queue) {
        this.queue = queue;
    }

    private boolean busy;

    public Cook(String name) {
        this.name = name;
    }

    public boolean isBusy() {
        return busy;
    }

    public void startCookingOrder(Order order) {
        this.busy = true;

        Tablet tablet = order.getTablet();

        ConsoleHelper.writeMessage(name + " Start cooking - " + order);

        int totalCookingTime = order.getTotalCookingTime();
        CookedOrderEventDataRow row = new CookedOrderEventDataRow(order.getTablet().toString(), name, totalCookingTime * 60, order.getDishes());
        StatisticManager.getInstance().register(row);

        try {
            Thread.sleep(totalCookingTime * 10);
        } catch (InterruptedException ignored) {
        }

        setChanged();
        notifyObservers(order);
        this.busy = false;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10);
                if (!Restaurant.getOrderQueue().isEmpty() && !isBusy()) {
                    startCookingOrder(Restaurant.getOrderQueue().take());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
