import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogsAnalysisTool {

    public static void main(String[] args) throws Exception {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Print log path:");
            String logPath = bufferedReader.readLine();

            System.out.println("Print output file absolute path:");
            String outputFile = bufferedReader.readLine();

            System.out.println("Enter threads count or press 'Enter' to skip:");
            String threadsTextValue = bufferedReader.readLine();
            int threads = 1;
            if (!threadsTextValue.isEmpty()) {
                threads = Integer.valueOf(threadsTextValue);
            }

            System.out.println("Enter username or press 'Enter' to skip:");
            String userName = bufferedReader.readLine();
            if (userName.isEmpty()) {
                userName = null;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");

            System.out.println("Enter 'from' date in user format 'HH:mm:ss dd.MM.yyyy' or press Enter to skip");
            String fromDateTimeString = bufferedReader.readLine();
            LocalDateTime fromDateTime = null;
            if (!fromDateTimeString.isEmpty()) {
                fromDateTime = LocalDateTime.parse(fromDateTimeString, formatter);
            }

            System.out.println("Enter 'to' date in user format 'HH:mm:ss dd.MM.yyyy' or press Enter to skip");
            String toDateTimeString = bufferedReader.readLine();
            LocalDateTime toDateTime = null;
            if (!toDateTimeString.isEmpty()) {
                toDateTime = LocalDateTime.parse(toDateTimeString, formatter);
            }


            System.out.println("Enter message type: 'INFO, ERROR, DEBUG or WARN', or press Enter to skip");
            MessageType messageType = MessageType.valueOf(bufferedReader.readLine());

            FilterCriteria filterCriteria = new FilterCriteria();
            filterCriteria.setMessageTemplate(messageType);
            filterCriteria.setToDate(toDateTime);
            filterCriteria.setFromDate(fromDateTime);
            filterCriteria.setUserName(userName);


            LogAnalyzer logAnalyzer = LogAnalyzer.newBuilder()
                    .setLogDirectory(Paths.get(logPath))
                    .setFilterCriteria(filterCriteria)
                    .setTreads(threads)
                    .setOutputDirectory(Paths.get(outputFile))
                    .build();

            logAnalyzer.analyzeLogs();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
