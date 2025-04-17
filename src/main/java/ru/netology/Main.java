package ru.netology;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Задание 1
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json, "data.json");

        // Задание 2
        List<Employee> xmlList = parseXML("data.xml");
        String json2 = listToJson(xmlList);
        writeString(json2, "data2.json");

        // Задание 3
        String inputJson = readString("data.json");
        List<Employee> employees = jsonToList(inputJson);
        employees.forEach(System.out::println);
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader).withMappingStrategy(strategy).build();

            return csv.parse();
        } catch (IOException e) {
            System.err.println("Ошибка парсинга: " + e.getMessage());
        }
        return List.of();
    }

    public static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        return gson.toJson(list, listType);
    }

    public static void writeString(String data, String fileName) {
        File file = new File(fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(data);
        } catch (IOException e) {
            System.err.println("Ошибка создания файла: " + e.getMessage());
        }
    }

    public static List<Employee> parseXML(String fileName) {
        List<Employee> employees = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileName));

            Node root = doc.getDocumentElement();
            NodeList nodeList = root.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    long id = Long.parseLong(getElementValue(element, "id"));
                    String firstName = getElementValue(element, "firstName");
                    String lastName = getElementValue(element, "lastName");
                    String country = getElementValue(element, "country");
                    int age = Integer.parseInt(getElementValue(element, "age"));
                    employees.add(new Employee(id, firstName, lastName, country, age));
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.err.println("Ошибка парсинга XML: " + e.getMessage());
        }
        return employees;
    }

    private static String getElementValue(Element element, String tagName) {
        return element.getElementsByTagName(tagName)
                .item(0)
                .getTextContent();
    }

    public static String readString(String fileName) {
        StringBuilder json = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
        return json.toString();
    }

    public static List<Employee> jsonToList(String json) {
        List<Employee> employees = new ArrayList<>();
        Gson gson = new Gson();
        JsonElement jsonElement = JsonParser.parseString(json);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonArray) {
            Employee employee = gson.fromJson(element, Employee.class);
            employees.add(employee);
        }
        return employees;
    }

}