package ru.job4j.service;

import ru.job4j.persistence.Store;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateStorage implements Updater {

    private static final ExecutorService POOL = Executors.newSingleThreadExecutor();
    private static final UpdateStorage INSTANCE = new UpdateStorage();
    private Long time;
    private Store store;

    private UpdateStorage() {
    }

    public static UpdateStorage getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean start() {
        if (Objects.isNull(time) || Objects.isNull(store)) {
            return false;
        }
        POOL.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(time);
                    store.reset();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        return true;
    }

    @Override
    public void stop() {
        POOL.shutdownNow();
    }

    @Override
    public void setTimeout(Long timeout) {
        this.time = timeout;
    }

    @Override
    public void setStore(Store store) {
        this.store = store;
    }
}
