package ru.job4j.persistence;

import ru.job4j.service.model.Account;
import ru.job4j.service.model.Place;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

public class MemoryStore implements Store {

    private static final Map<Place, Account> STORE = new TreeMap<>();
    private static final AtomicInteger NEXT_ID = new AtomicInteger();
    private static final MemoryStore PERSISTENT = new MemoryStore();

    private MemoryStore() {
    }

    public static MemoryStore getInstance() {
        return PERSISTENT;
    }

    public MemoryStore fillStore(int row, int seat) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < seat; j++) {
                STORE.put(new Place(i, j), null);
            }
        }
        return this;
    }

    @Override
    public Set<Place> allPlaces() {
        return STORE.keySet();
    }

    @Override
    public boolean takePlaces(Collection<Place> places, Account account) {
        synchronized (STORE) {
            for (Place place : places) {
                if (Objects.nonNull(STORE.get(place))) {
                    return false;
                }
            }
            account.setId(NEXT_ID.getAndIncrement());
            for (Place place : places) {
                STORE.compute(place, changeMap(true, account.getId(), account));
            }
        }
        return true;
    }

    @Override
    public boolean reset() {
        synchronized (STORE) {
            STORE.replaceAll(changeMap(false, null, null));
        }
        return true;
    }

    private BiFunction<Place, Account, Account> changeMap(boolean pick, Integer idAccount, Account account) {
        return (key, value) -> {
            key.setPick(pick);
            key.setIdAccount(idAccount);
            return account;
        };
    }
}
