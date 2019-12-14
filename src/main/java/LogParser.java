import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LogParser implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(LogParser.class);
    private LogAnalyzer logAnalyzer;
    private FilterCriteria filterCriteria;

    public LogParser(LogAnalyzer logAnalyzer, FilterCriteria filterCriteria) {
        this.logAnalyzer = logAnalyzer;
        this.filterCriteria = filterCriteria;
    }

    @Override
    public void run() {
        LOGGER.info("Create thread " + Thread.currentThread().getName());
        while (true) {
            File nextFile = logAnalyzer.getNextFile();
            if (nextFile == null) {
                LOGGER.info("End of thread" + Thread.currentThread().getName());
                return;
            }
            List<Log> dataFromFile = getDataFromFile(nextFile);
            logAnalyzer.addToList(dataFromFile);
        }
    }

    private List<Log> getDataFromFile(File logFile) {
        List<Log> logs = new ArrayList<>();
        if (logFile.exists() && logFile.isFile()) {
            try (BufferedReader bf = new BufferedReader(new FileReader(logFile))) {
                while (bf.ready()) {
                    String line = bf.readLine();
                    Log log = parsLine(line);
                    if (isLogValid(log)) {
                        logs.add(log);
                    }
                }
            } catch (Exception e) {
                LOGGER.error(String.format("Error while parsing file %s: %s", logFile.getName(), e.getMessage()));
            }
        }
        return logs;
    }

    private boolean isLogValid(Log log) {
        return (filterCriteria.getUserName() == null || filterCriteria.getUserName().equals(log.getUserName()))
                && (filterCriteria.getToDate() == null || filterCriteria.getToDate().compareTo(log.getTime()) >= 0)
                && (filterCriteria.getFromDate() == null || filterCriteria.getFromDate().compareTo(log.getTime()) <= 0)
                && (filterCriteria.getMessageType() == null || log.getMessageType().equals(filterCriteria.getMessageType()));
    }

    private Log parsLine(String line) {
        String[] params = line.split(" ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");
        LocalDateTime dateTime = LocalDateTime.parse(params[0] + " " + params[1], formatter);

        String name = params[2].substring(1, params[2].length() - 1);
        MessageType messageType;
        try {
            messageType = MessageType.valueOf(params[3]);
        } catch (IllegalArgumentException | NullPointerException exception) {
            LOGGER.warn("Unknown message type: " + exception.getMessage());
            messageType = MessageType.OTHER;
        }

        StringBuilder message = new StringBuilder();
        for (int i = 4; i < params.length; i++) {
            message.append(params[i]);
            message.append(" ");
        }
        message.setLength(message.length() - 1);

        return new Log(name, dateTime, message.toString(), messageType);
    }
}
