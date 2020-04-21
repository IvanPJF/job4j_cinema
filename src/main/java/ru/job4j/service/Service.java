package ru.job4j.service;

import ru.job4j.service.model.Account;
import ru.job4j.service.model.Place;

import java.util.Collection;

public interface Service {

    Collection<Place> allPlaces();

    boolean takePlaces(Collection<Place> places, Account account);

    Updater getStorageUpdater();
}
