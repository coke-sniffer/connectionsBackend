package com.example.connectionsbackend;

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

@CrossOrigin(
        allowCredentials = "true",
        origins = {"https://blooooopybleep.github.io", "http://localhost:63342","https://play.william-duan.games"},
        allowedHeaders = "*",
        methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT}
)
@RestController
@RequestMapping("/api/v1")
@SpringBootApplication
public class ConnectionsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConnectionsBackendApplication.class, args);
        System.out.println("p0 initialized");
        timerTask.run();
        init();
    }


    public static String[] data=new String[16];

    public static void init() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 15);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long delay = calendar.getTimeInMillis() - System.currentTimeMillis();
        if (delay < 0) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            delay = calendar.getTimeInMillis() - System.currentTimeMillis();
        }
        new Timer().scheduleAtFixedRate(timerTask, delay, TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
    }

    private static final TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            WebDriver driver = new ChromeDriver(new ChromeOptions().addArguments("--remote-allow-origins=*",
                    "--headless=new",
                    "--disable-gpu",
                    "--no-sandbox"));
            driver.navigate().to("https://www.nytimes.com/games/connections");

            ConnectionsBackendApplication.data = java.util.regex.Pattern.compile("(>[A-Z]{1,10}<)").matcher(driver.findElement(By.id("board")).getAttribute("innerHTML")).results().map(mr->mr.group().substring(1,mr.group().length()-1)).toArray(String[]::new);
            driver.quit();
            System.out.println(Arrays.toString(ConnectionsBackendApplication.data) +" retrieved @"+System.currentTimeMillis());
        }
    };

    @CrossOrigin
    @GetMapping("/dailypuzzle")
    public String getData() {
        System.out.println("Data requested");
        return Arrays.toString(ConnectionsBackendApplication.data);
    }


}
