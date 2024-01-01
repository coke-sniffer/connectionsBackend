package com.example.connectionsbackend;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.ILoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

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
        Calendar refreshDate = Calendar.getInstance();
        refreshDate.set(0,0,0, 0, 15, 0);
        Timer timer = new Timer();
        timer.schedule(timerTask, Math.abs(refreshDate.getTimeInMillis()-System.currentTimeMillis()), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
        System.out.println("p1 initialized");
    }

    private static TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            WebDriver driver = new ChromeDriver(new ChromeOptions().addArguments("--remote-allow-origins=*",
                    "--headless=new",
                    "--disable-gpu",
                    "--no-sandbox"));
            driver.navigate().to("https://www.nytimes.com/games/connections");

            ConnectionsBackendApplication.data = java.util.regex.Pattern.compile("(>[A-Z]{1,10}<)").matcher(driver.findElement(By.id("board")).getAttribute("innerHTML")).results().map(mr->mr.group().substring(1,mr.group().length()-1)).toArray(String[]::new);
            driver.quit();
            System.out.println("p2 initialized");
        }
    };

    @GetMapping("/dailypuzzle")
    public String getData() {
        System.out.println("Data requested");
        return Arrays.toString(ConnectionsBackendApplication.data);
    }


}
