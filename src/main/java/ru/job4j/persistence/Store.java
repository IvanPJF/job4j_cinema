package ru.job4j.persistence;

import ru.job4j.service.model.Account;
import ru.job4j.service.model.Place;

import java.util.Collection;
import java.util.Set;

public interface Store {

    Set<Place> allPlaces();

    boolean takePlaces(Collection<Place> places, Account account);

    boolean reset();
}
