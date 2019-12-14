import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class LogAnalyzer {

    private static final Logger LOGGER = LogManager.getLogger(LogAnalyzer.class);
    private Path logDirectory;
    private List<File> fileList = new CopyOnWriteArrayList<>();
    private List<Log> logList = new CopyOnWriteArrayList<>();
    private FilterCriteria filterCriteria;
    private Integer treads;
    private Path outputDirectory;
    private Map<Integer, Integer> yearStatistic = new HashMap<>();
    private Map<MessageType, Integer> messageTypeStatistic = new HashMap<>();
    private Map<String, Integer> userStatistic = new HashMap<>();

    private LogAnalyzer() {
    }

    public static Builder newBuilder() {
        return new LogAnalyzer().new Builder();
    }

    private void getFilesList() {
        File pathFile = logDirectory.toFile();
        if (!pathFile.exists()) {
            LOGGER.error("File do not exist");
            return;
        }
        File[] files = pathFile.listFiles((File dir, String name) -> name.endsWith(".log"));

        for (File file : files) {
            fileList.add(file);
        }
    }

    public void analyzeLogs() {
        for (int i = 0; i < treads; i++) {
            new Thread(new LogParser(this, filterCriteria), "Tread" + i).start();
        }

    }

    public synchronized File getNextFile() {
        if (fileList.isEmpty()) {
            return null;
        }

        File file = fileList.get(0);
        fileList.remove(0);
        return file;
    }

    public synchronized void addToList(List<Log> list) {
        logList.addAll(list);
        if (fileList.isEmpty()) {
            safeLogsToFile();
            printStatistics();
        }
    }

    private void safeLogsToFile() {
        Map<String, Map<Integer, Map<MessageType, List<Log>>>> collect = logList.stream()
                .collect(Collectors.groupingBy(Log::getUserName,
                        Collectors.groupingBy(log -> log.getTime().getYear(), Collectors.groupingBy(Log::getMessageType))));

        collect.forEach((name, years) -> years.forEach((year, messageTypes) -> messageTypes.forEach((messageType, logs) -> logs.forEach(log -> {
            try (FileWriter fileWriter = new FileWriter(outputDirectory.toFile(), true)) {
                fileWriter.write(log.toString());
            } catch (IOException e) {
                LOGGER.error("Error while save data " + e.getMessage());
            }
        }))));


    }

    private void printStatistics() {
        logList.stream()
                .collect(Collectors.groupingBy(log -> log.getTime().getYear(), Collectors.counting()))
                .entrySet().forEach(statistic -> {
            try (FileWriter fileWriter = new FileWriter(outputDirectory.toFile(), true)) {
                fileWriter.write(statistic.getKey() + "=" + statistic.getValue() + "\n");
            } catch (IOException e) {
                LOGGER.error("Error while save data " + e.getMessage());
            }
        });

        logList.stream()
                .collect(Collectors.groupingBy(log -> log.getUserName(), Collectors.counting()))
                .entrySet().forEach(statistic -> {
            try (FileWriter fileWriter = new FileWriter(outputDirectory.toFile(), true)) {
                fileWriter.write(statistic.getKey() + "=" + statistic.getValue() + "\n");
            } catch (IOException e) {
                LOGGER.error("Error while save data " + e.getMessage());
            }
        });

        logList.stream()
                .collect(Collectors.groupingBy(log -> log.getMessageType(), Collectors.counting()))
                .entrySet().forEach(statistic -> {
            try (FileWriter fileWriter = new FileWriter(outputDirectory.toFile(), true)) {
                fileWriter.write(statistic.getKey() + "=" + statistic.getValue() + "\n");
            } catch (IOException e) {
                LOGGER.error("Error while save data " + e.getMessage());
            }
        });

    }


    public class Builder {

        private Builder() {
        }

        public Builder setLogDirectory(Path logDirectory) {
            LogAnalyzer.this.logDirectory = logDirectory;
            return this;
        }

        public Builder setFilterCriteria(FilterCriteria filterCriteria) {
            LogAnalyzer.this.filterCriteria = filterCriteria;
            return this;
        }

        public Builder setTreads(Integer treads) {
            LogAnalyzer.this.treads = treads;
            return this;
        }

        public Builder setOutputDirectory(Path outputDirectory) {
            LogAnalyzer.this.outputDirectory = outputDirectory;
            return this;
        }

        public LogAnalyzer build() {
            getFilesList();
            return LogAnalyzer.this;
        }
    }
}
