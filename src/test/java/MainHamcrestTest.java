import org.junit.Test;
import ru.netology.Employee;
import ru.netology.Main;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MainHamcrestTest {

    @Test
    public void listToJson_ShouldGenerateValidJsonStructure() {
        List<Employee> employees = List.of(new Employee(1, "John", "Doe", "USA", 30));
        String json = Main.listToJson(employees);

        // Учитываем пробелы в форматированном JSON
        assertThat(json, containsString("John"));
        assertThat(json, containsString("\"country\": \"USA\""));  // пробел после :
        assertThat(json, containsString("\"age\": 30"));           // пробел после :
    }

    @Test
    public void jsonToList_ShouldMatchEmployeeProperties() {
        String json = "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Doe\",\"country\":\"USA\",\"age\":30}]";
        List<Employee> employees = Main.jsonToList(json);

        assertThat(employees, hasSize(1));
        assertThat(employees.get(0), allOf(
                hasProperty("id", is(1L)),         // Если id в Employee long
                hasProperty("firstName", is("John")),
                hasProperty("lastName", is("Doe")),
                hasProperty("country", is("USA")),
                hasProperty("age", is(30))
        ));
    }
}