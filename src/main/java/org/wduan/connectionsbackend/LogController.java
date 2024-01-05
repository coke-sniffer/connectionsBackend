package org.wduan.connectionsbackend;


import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.springframework.web.bind.annotation.*;

import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.Buffer;
import java.util.stream.Collectors;


@CrossOrigin(
        origins = "*",
        allowedHeaders = "*",
        methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT}
)
@RestController
@RequestMapping("/logs")
public class LogController {

    private static final Logger logger = LogManager.getLogger(LogController.class);

    public static Logger getLogger() {
        return logger;
    }

    public static void clearLog() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new File("logs/log.log"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        writer.print("");
        writer.close();
    }

    public static void log(String message) {
        logger.info(message);
    }




    @GetMapping(produces = "text/plain")
    public String getLogs(HttpServletRequest request) {
        log("logs requested from ip: "+request.getRemoteAddr());
        try {
            // Read logs from the log file (adjust the path as needed)
            String logs = new BufferedReader(new FileReader("logs/log.log"))
                    .lines()
                    .collect(Collectors.joining("\n"));
            return logs;
        } catch (IOException e) {
            logger.error("Error reading logs", e);
            return "Error reading logs";
        }
    }

}
