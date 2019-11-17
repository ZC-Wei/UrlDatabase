package com.newswhip.UrlDB.component;

import com.newswhip.UrlDB.model.Url;
import com.newswhip.UrlDB.repository.UrlRepository;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class UrlDBRunner implements CommandLineRunner {
    private static Logger logger = LoggerFactory.getLogger(UrlDBRunner.class);
    private static Scanner scanner = new Scanner(System.in);
    @Autowired
    public UrlRepository urlRepository;

    @Autowired
    private ConfigurableApplicationContext context;

    @Override
    public void run(String... args) {
        logger.info("Running " + UrlDBRunner.class);
        System.out.println("Please type your command, type HELP for more information.");
        try {
            while (true) {
                String command = scanner.nextLine();
                ExecuteCommand(command);
            }
        } catch (IllegalStateException | NoSuchElementException e) {
            logger.error(e.getMessage());
        }
    }

    public void ExecuteCommand(String command) {
        logger.info("Running command: "+ command);
        List<String> commandArguments = Stream.of(command.split(" ", -1))
                .collect(Collectors.toList());
        switch (commandArguments.get(0)) {
            case "ADD":
                AddUrl(commandArguments);
                break;
            case "REMOVE":
                RemoveUrl(commandArguments);
                break;
            case "EXPORT":
                ExportReport();
                break;
            case "HELP":
                PrintHelpInfo();
                break;
            case "EXIT":
                System.exit(SpringApplication.exit(context));
                break;
            default:
                logger.warn("Invalid command: " + command);
                System.out.println("Invalid command");
        }
    }

    private void AddUrl(@NonNull List<String> commandArguments) {
        try {
            if (commandArguments.size() != 3) {
                throw new Exception();
            }
            String url = commandArguments.get(1);
            String score = commandArguments.get(2);
            logger.info("Adding URL: " + url);
            String domain = getDomain(url);
            Integer socialScore = Integer.parseInt(score);
            Url newEntity = Url.builder()
                    .domain(domain).url(url).socialScore(socialScore).build();
            urlRepository.save(newEntity);
            logger.info("1 record created");
        } catch (URISyntaxException | NumberFormatException e) {
            logger.error(e.getMessage());
            System.out.println("Invalid URL or Score");
        } catch (Exception e) {
            logger.error("Invalid command: " + commandArguments.toString());
            System.out.println("Invalid command format");
        }
    }

    private void RemoveUrl(@NonNull List<String> commandArguments) {
        try {
            if (commandArguments.size() != 2) {
                throw new Exception();
            }
            String url = commandArguments.get(1);
            logger.info("Removing URL: " + url);
            if (!url.startsWith("http") && !url.startsWith("https")) {
                url = "http://" + url;
            }
            Integer result = urlRepository.deleteByUrl(url);
            logger.info(result + " record deleted");
        } catch (Exception e) {
            logger.error("Invalid command: "+ commandArguments.toString());
            System.out.println("Invalid command format");
        }
    }

    private void ExportReport() {
        logger.info("Exporting report");
        List<Url> result = urlRepository.findAll();
        if (result != null) {
            Map<String, Integer> score = getSocialScoreByDomain(result);
            Map<String, Long> counting = getCountingByDomain(result);
            counting.forEach((key, value) -> System.out.println(key + ";" + value + ";" + score.get(key)));
        } else {
            System.out.println("No record");
        }
    }

    private void PrintHelpInfo() {
        System.out.println("Existing command are: ADD, REMOVE, EXPORT, HELP, EXIT");
    }

    private String getDomain(@NonNull String url) throws URISyntaxException {
        if (!url.startsWith("http") && !url.startsWith("https")) {
            url = "http://" + url;
        }
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    private Map<String, Integer> getSocialScoreByDomain(List<Url> records) {
        return records.stream().collect(
                Collectors.toMap(Url::getDomain, Url::getSocialScore, Integer::sum));
    }

    private Map<String, Long> getCountingByDomain(List<Url> records) {
        return records.stream().collect(
                Collectors.groupingBy(Url::getDomain, Collectors.counting()));
    }
}
