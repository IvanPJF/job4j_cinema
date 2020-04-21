package ru.job4j.service;

import ru.job4j.persistence.MemoryStore;
import ru.job4j.service.model.Account;
import ru.job4j.service.model.Place;

import java.util.Collection;

public class StubService implements Service {

    private static final MemoryStore STORE = MemoryStore.getInstance();
    private static final Updater UPDATER = UpdateStorage.getInstance();
    private static final StubService SERVICE = new StubService();

    private StubService() {
        UPDATER.setStore(STORE);
    }

    public static StubService getInstance() {
        return SERVICE;
    }

    public MemoryStore fillStore(int row, int seat) {
        return STORE.fillStore(row, seat);
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
