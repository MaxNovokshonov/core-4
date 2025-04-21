

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.netology.Employee;
import ru.netology.Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MainTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void parseCSV_ShouldParseEmployeesCorrectly() throws IOException {
        // Подготовка тестового CSV
        String csvContent = "1,John,Doe,USA,30\n2,Jane,Smith,Canada,25";
        File csvFile = tempFolder.newFile("test.csv");
        Files.write(csvFile.toPath(), csvContent.getBytes());

        // Вызов метода
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        List<Employee> employees = Main.parseCSV(columnMapping, csvFile.getAbsolutePath());

        // Проверки
        assertEquals(2, employees.size());
        assertEquals(new Employee(1, "John", "Doe", "USA", 30), employees.get(0));
        assertEquals(new Employee(2, "Jane", "Smith", "Canada", 25), employees.get(1));
    }

    @Test
    public void parseXML_ShouldParseEmployeesCorrectly() throws IOException {
        // Подготовка тестового XML
        String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root>" +
                "<employee><id>1</id><firstName>John</firstName><lastName>Doe</lastName><country>USA</country><age>30</age></employee>" +
                "<employee><id>2</id><firstName>Jane</firstName><lastName>Smith</lastName><country>Canada</country><age>25</age></employee>" +
                "</root>";
        File xmlFile = tempFolder.newFile("test.xml");
        Files.write(xmlFile.toPath(), xmlContent.getBytes());

        // Вызов метода
        List<Employee> employees = Main.parseXML(xmlFile.getAbsolutePath());

        // Проверки
        assertEquals(2, employees.size());
        assertEquals(new Employee(1, "John", "Doe", "USA", 30), employees.get(0));
        assertEquals(new Employee(2, "Jane", "Smith", "Canada", 25), employees.get(1));
    }

    @Test
    public void jsonToList_ShouldConvertJsonToEmployeeList() {
        // Подготовка тестовых данных
        List<Employee> expected = List.of(
                new Employee(1, "John", "Doe", "USA", 30),
                new Employee(2, "Jane", "Smith", "Canada", 25)
        );
        String json = Main.listToJson(expected);

        // Вызов метода
        List<Employee> actual = Main.jsonToList(json);

        // Проверка
        assertEquals("Списки сотрудников должны совпадать", expected, actual);
    }
}