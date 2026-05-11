package com.carbonprint.persistence;

import com.carbonprint.interfaces.CarbonFootprint;
import com.carbonprint.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase FileManager
 * Prueba escritura y lectura de archivos
 */
@DisplayName("Pruebas de FileManager")
class FileManagerTest {
    
    private FileManager fileManager;
    private List<CarbonFootprint> testData;
    
    @BeforeEach
    void setUp() {
        fileManager = new FileManager();
        testData = new ArrayList<>();
        
        testData.add(new Building("Building Test", "Test", 8760, 100000, 5, "comercial"));
        testData.add(new Car("Car Test", "Test", 365, 20000, "sedan", "gasolina", 2020));
        testData.add(new Bicycle("Bicycle Test", "Test", 365, 5000, "urbana", 3));
    }
    
    @Test
    @DisplayName("Guarda correctamente en archivo")
    void testSaveToFile() throws IOException {
        fileManager.saveToFile(testData);
        
        // Verificar que el archivo existe
        assertTrue(Files.exists(Paths.get("output/emissions.txt")));
    }
    
    @Test
    @DisplayName("Lee correctamente del archivo")
    void testReadFromFile() throws IOException {
        fileManager.saveToFile(testData);
        List<String> records = fileManager.readFromFile();
        
        assertNotNull(records);
        assertEquals(3, records.size());
    }
    
    @Test
    @DisplayName("Archivo contiene datos Building")
    void testFileContainsBuilding() throws IOException {
        fileManager.saveToFile(testData);
        List<String> records = fileManager.readFromFile();
        
        boolean hasBuilding = records.stream()
                .anyMatch(r -> r.contains("Type: Building") && r.contains("Building Test"));
        assertTrue(hasBuilding);
    }
    
    @Test
    @DisplayName("Archivo contiene datos Car")
    void testFileContainsCar() throws IOException {
        fileManager.saveToFile(testData);
        List<String> records = fileManager.readFromFile();
        
        boolean hasCar = records.stream()
                .anyMatch(r -> r.contains("Type: Car") && r.contains("Car Test"));
        assertTrue(hasCar);
    }
    
    @Test
    @DisplayName("Archivo contiene datos Bicycle")
    void testFileContainsBicycle() throws IOException {
        fileManager.saveToFile(testData);
        List<String> records = fileManager.readFromFile();
        
        boolean hasBicycle = records.stream()
                .anyMatch(r -> r.contains("Type: Bicycle") && r.contains("Bicycle Test"));
        assertTrue(hasBicycle);
    }
    
    @Test
    @DisplayName("Lee correctamente lista vacía")
    void testReadEmptyFile() throws IOException {
        fileManager.saveToFile(new ArrayList<>());
        List<String> records = fileManager.readFromFile();
        
        assertTrue(records.isEmpty());
    }
}
