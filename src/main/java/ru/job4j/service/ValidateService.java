package ru.job4j.service;

import ru.job4j.persistence.DBStore;
import ru.job4j.persistence.Store;
import ru.job4j.service.model.Account;
import ru.job4j.service.model.Place;

import java.util.Collection;

public class ValidateService implements Service {

    private static final Store STORE = DBStore.getInstance();
    private static final long INTERVAL_UPDATE_DB = 60 * 60 * 1000L;
    private static final Updater UPDATER = UpdateStorage.getInstance();
    private static final ValidateService SERVICE = new ValidateService();

    private ValidateService() {
        UPDATER.setTimeout(INTERVAL_UPDATE_DB);
        UPDATER.setStore(STORE);
    }

    public static Service getInstance() {
        return SERVICE;
    }

    @Override
    public Collection<Place> allPlaces() {
        return STORE.allPlaces();
    }

    @Override
    public boolean takePlaces(Collection<Place> places, Account account) {
        return STORE.takePlaces(places, account);
    }

    @Override
    public Updater getStorageUpdater() {
        return UPDATER;
    }
}
