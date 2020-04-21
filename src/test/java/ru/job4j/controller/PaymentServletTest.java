package ru.job4j.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.service.ValidateService;
import ru.job4j.service.StubService;
import ru.job4j.service.model.Account;
import ru.job4j.service.model.Place;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.StringJoiner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ValidateService.class)
public class PaymentServletTest {

    @Test
    public void whenTakeFreePlacesWhenReturnEmpty() throws IOException {
        StubService service = StubService.getInstance();
        service.fillStore(1, 2);
        PowerMockito.mockStatic(ValidateService.class);
        when(ValidateService.getInstance()).thenReturn(service);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        StringJoiner placesJSON = new StringJoiner(",", "[", "]");
        placesJSON.add("{\"row\":1,\"seat\":1,\"pick\":false,\"idAccount\":null}");
        placesJSON.add("{\"row\":1,\"seat\":2,\"pick\":false,\"idAccount\":null}");
        when(req.getParameter("places")).thenReturn(placesJSON.toString());
        String accountJSON = "{\"id\":null,\"name\":\"Buyer\",\"phone\":1}";
        when(req.getParameter("account")).thenReturn(accountJSON);
        ByteArrayOutputStream expectedStream = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(expectedStream)) {
            when(resp.getWriter()).thenReturn(writer);
            new PaymentServlet().doPost(req, resp);
        }
        assertThat(expectedStream.toString(), is(""));
    }

    @Test
    public void whenTakeOccupiedPlacesWhenReturnError() throws IOException {
        StubService service = StubService.getInstance();
        service.fillStore(1, 2);
        service.takePlaces(List.of(new Place(1, 1), new Place(1, 2)), new Account("Buyer", 1L));
        PowerMockito.mockStatic(ValidateService.class);
        when(ValidateService.getInstance()).thenReturn(service);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        StringJoiner placesJSON = new StringJoiner(",", "[", "]");
        placesJSON.add("{\"row\":1,\"seat\":1,\"pick\":false,\"idAccount\":null}");
        placesJSON.add("{\"row\":1,\"seat\":2,\"pick\":false,\"idAccount\":null}");
        when(req.getParameter("places")).thenReturn(placesJSON.toString());
        String accountJSON = "{\"id\":null,\"name\":\"Latecomer\",\"phone\":2}";
        when(req.getParameter("account")).thenReturn(accountJSON);
        ByteArrayOutputStream expectedStream = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(expectedStream)) {
            when(resp.getWriter()).thenReturn(writer);
            new PaymentServlet().doPost(req, resp);
        }
        assertThat(expectedStream.toString(), is("error"));
    }
}