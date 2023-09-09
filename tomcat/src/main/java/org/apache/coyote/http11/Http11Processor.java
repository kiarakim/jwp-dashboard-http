package org.apache.coyote.http11;

import nextstep.jwp.exception.UncheckedServletException;
import nextstep.jwp.presentation.Controller;
import org.apache.coyote.Processor;
import org.apache.coyote.http11.request.RequestReader;
import org.apache.coyote.http11.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static nextstep.jwp.presentation.ControllerType.findController;

public class Http11Processor implements Runnable, Processor {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

    private final Socket connection;

    public Http11Processor(final Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        log.info("connect host: {}, port: {}", connection.getInetAddress(), connection.getPort());
        process(connection);
    }

    @Override
    public void process(final Socket connection) {
        try (final var inputStream = connection.getInputStream();
             final var outputStream = connection.getOutputStream()) {

            RequestReader requestReader = new RequestReader(new BufferedReader(new InputStreamReader(inputStream)));
            requestReader.read();

            Controller controller = findController(requestReader.getRequestUri());
            Response response = controller.service(requestReader);

            outputStream.write(response.format().getBytes());
            outputStream.flush();
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }
}
