package org.wduan.connectionsbackend;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;

@CrossOrigin(
        allowCredentials = "true",
        origins = {"https://blooooopybleep.github.io", "http://localhost:69696","https://play.william-duan.games"},
        allowedHeaders = "*",
        methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT}
)
@RestController
@RequestMapping("/api/v1")
@SpringBootApplication
public class ConnectionsBackendApplication {

    public static String[] data=new String[16];
    public static void main(String[] args) {
        LogController.clearLog();
        SpringApplication.run(ConnectionsBackendApplication.class, args);
        LogController.log("p0 initialized");
        timerTask.run();
        init();
    }


    public static void init() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 15);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long delay = calendar.getTimeInMillis() - System.currentTimeMillis();
        if (delay < 0) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            //add 300k ms to compensate for GMT delay
            delay = calendar.getTimeInMillis() - System.currentTimeMillis() + 300000;
        }
        new Timer().scheduleAtFixedRate(timerTask, delay, TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
    }

    private static final TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            ConnectionsBackendApplication.data = new String[16];
            WebDriver driver = new ChromeDriver(new ChromeOptions().addArguments("--remote-allow-origins=*",
                    "--headless=new",
                    "--disable-gpu",
                    "--no-sandbox"));
            driver.navigate().to("https://www.nytimes.com/games/connections");

            ConnectionsBackendApplication.data = java.util.regex.Pattern.compile("(>[A-Z]{1,10}<)").matcher(driver.findElement(By.id("board")).getAttribute("innerHTML")).results().map(mr->mr.group().substring(1,mr.group().length()-1)).toArray(String[]::new);
            driver.quit();
            LogController.log(Arrays.toString(ConnectionsBackendApplication.data) +" retrieved @"+System.currentTimeMillis());
        }
    };

    @GetMapping("/dailypuzzle")
    public String getData(HttpServletRequest request) {
        LogController.log("Data requested from ip: "+request.getRemoteAddr()+" @"+System.currentTimeMillis());
        return Arrays.toString(ConnectionsBackendApplication.data);
    }


}
