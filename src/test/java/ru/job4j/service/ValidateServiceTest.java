package ru.job4j.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.persistence.DBStore;
import ru.job4j.persistence.MemoryStore;
import ru.job4j.service.model.Account;
import ru.job4j.service.model.Place;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DBStore.class})
public class ValidateServiceTest {

    private static final int COUNT_ROWS = 5;
    private static final int COUNT_SEATS = 5;
    private final MemoryStore store = MemoryStore.getInstance().fillStore(COUNT_ROWS, COUNT_SEATS);

    @Before
    public void prepareStore() {
        PowerMockito.mockStatic(DBStore.class);
        when(DBStore.getInstance()).thenReturn(store);
    }

    @Test
    public void whenTakeOnePlaceThenTrue() {
        Service service = ValidateService.getInstance();
        boolean result = service.takePlaces(List.of(new Place(1, 1)), new Account());
        assertThat(result, is(true));
    }

    @Test
    public void whenTakeOnePlaceIsTakenThenFalse() {
        Service service = ValidateService.getInstance();
        Place place = new Place(1, 1);
        service.takePlaces(List.of(place), new Account("First", 1L));
        boolean result = service.takePlaces(List.of(place), new Account("Second", 2L));
        assertThat(result, is(false));
    }

    @Test
    public void whenAllPlacesThenPlaces() {
        Service service = ValidateService.getInstance();
        Collection<Place> places = service.allPlaces();
        assertThat(places.size(), is(COUNT_ROWS * COUNT_SEATS));
    }
}