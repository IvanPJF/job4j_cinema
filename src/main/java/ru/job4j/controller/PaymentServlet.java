package ru.job4j.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.job4j.service.ValidateService;
import ru.job4j.service.Service;
import ru.job4j.service.model.Account;
import ru.job4j.service.model.Place;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class PaymentServlet extends HttpServlet {

    private static final Service SERVICE = ValidateService.getInstance();

    @Override
    public void init() throws ServletException {
        SERVICE.getStorageUpdater().start();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/html/payment.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String placesJSON = req.getParameter("places");
        String accountJSON = req.getParameter("account");
        ObjectMapper objectMapper = new ObjectMapper();
        List<Place> places = objectMapper.readValue(placesJSON, new TypeReference<>() {
        });
        Account account = objectMapper.readValue(accountJSON, Account.class);
        String answer = SERVICE.takePlaces(places, account) ? "" : "error";
        try (PrintWriter writer = resp.getWriter()) {
            writer.print(answer);
        }
    }

    @Override
    public void destroy() {
        SERVICE.getStorageUpdater().stop();
    }
}
