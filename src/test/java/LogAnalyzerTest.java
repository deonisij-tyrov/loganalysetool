import org.junit.Test;

import java.nio.file.Paths;

public class LogAnalyzerTest {


    @Test
    public void SuccessTest() {
        FilterCriteria filterCriteria = new FilterCriteria();
//        filterCriteria.setMessageTemplate(MessageType.ERROR);
        filterCriteria.setUserName("main");

        LogAnalyzer logAnalyzer = LogAnalyzer.newBuilder()
                .setLogDirectory(Paths.get("C:\\Users\\denis\\IdeaProjects\\loganalysetool\\src\\test\\resources"))
                .setFilterCriteria(filterCriteria)
                .setOutputDirectory(Paths.get("C:\\Users\\denis\\IdeaProjects\\loganalysetool\\src\\test\\result.log"))
                .build();

        LogParser logParser = new LogParser(logAnalyzer, filterCriteria);
        logParser.run();
    }

}
