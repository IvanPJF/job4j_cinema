package ru.job4j.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.service.ValidateService;
import ru.job4j.service.StubService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringJoiner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ValidateService.class})
public class HallServletTest {

    private static final int COUNT_ROWS = 1;
    private static final int COUNT_SEATS = 2;
    private final StubService service = StubService.getInstance();

    @Before
    public void prepareService() {
        service.fillStore(COUNT_ROWS, COUNT_SEATS);
        PowerMockito.mockStatic(ValidateService.class);
        when(ValidateService.getInstance()).thenReturn(service);
    }

    @Test
    public void whenAllPlacesThenPlaces() throws IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        StringJoiner expected = new StringJoiner(",", "[", "]");
        for (int i = 0; i < COUNT_ROWS; i++) {
            for (int j = 0; j < COUNT_SEATS; j++) {
                expected.add(String.format("{\"row\":%s,\"seat\":%s,\"pick\":false,\"idAccount\":null}", i, j));
            }
        }
        ByteArrayOutputStream expectedStream = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(expectedStream)) {
            when(resp.getWriter()).thenReturn(writer);
            new HallServlet().doGet(req, resp);
        }
        assertThat(expectedStream.toString(), is(expected.toString()));
    }
}