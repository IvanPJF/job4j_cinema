package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.job4j.service.ValidateService;
import ru.job4j.service.Service;
import ru.job4j.service.model.Place;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

public class HallServlet extends HttpServlet {

    private static final Service SERVICE = ValidateService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Collection<Place> places = SERVICE.allPlaces();
        ObjectMapper json = new ObjectMapper();
        try (PrintWriter writer = resp.getWriter()) {
            json.writeValue(writer, places);
        }
    }
}
