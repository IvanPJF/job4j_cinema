package ru.job4j.service;

import ru.job4j.persistence.Store;

public interface Updater {

    boolean start();

    void stop();

    void setTimeout(Long timeout);

    void setStore(Store store);
}
