package com.carbonprint.main;

import com.carbonprint.interfaces.CarbonFootprint;
import com.carbonprint.model.*;
import com.carbonprint.services.CarbonFootprintService;
import com.carbonprint.persistence.FileManager;

import java.io.IOException;
import java.util.List;

/**
 * Clase Main - Punto de entrada de la aplicación
 * Demuestra el uso de herencia, polimorfismo, modularidad y manejo de archivos
 */
public class Main {
    
    public static void main(String[] args) {
        try {
            // 1. Crear instancia del servicio (Dependency Injection)
            CarbonFootprintService service = new CarbonFootprintService();
            
            // 2. Crear instancias de las tres clases (HERENCIA: todas heredan de EmissionSource)
            Building building = new Building(
                    "Edificio Central",
                    "Oficinas principales con aire acondicionado",
                    8760,
                    250000, // kWh anuales
                    12,     // pisos
                    "comercial"
            );
            
            Car car = new Car(
                    "Tesla Model 3",
                    "Vehículo eléctrico corporativo",
                    365,
                    15000, // km anuales
                    "sedan",
                    "eléctrico",
                    2023
            );
            
            Bicycle bicycle = new Bicycle(
                    "Bicicleta Urbana",
                    "Bicicleta para transporte diario",
                    365,
                    5000, // km anuales
                    "urbana",
                    3 // años de antigüedad
            );
            
            // 3. Agregar al servicio (POLIMORFISMO: todas implementan CarbonFootprint)
            service.addEmissionSource(building);
            service.addEmissionSource(car);
            service.addEmissionSource(bicycle);
            
            // 4. Mostrar información de cada objeto
            System.out.println("=== FUENTES DE EMISIÓN ===\n");
            System.out.println(building);
            System.out.println(car);
            System.out.println(bicycle);
            
            // 5. Calcular y mostrar estadísticas usando POLIMORFISMO
            System.out.println("\n=== ESTADÍSTICAS ===\n");
            System.out.println("Huella total: " + String.format("%.6f", service.calculateTotalFootprint()) + " ton CO2");
            System.out.println("Huella promedio: " + String.format("%.6f", service.calculateAverageFootprint()) + " ton CO2");
            
            CarbonFootprint maxSource = service.getMaxEmissionSource();
            if (maxSource != null) {
                System.out.println("Mayor emisor: " + maxSource.getClass().getSimpleName() + 
                                 " con " + String.format("%.6f", maxSource.getCarbonFootprint()) + " ton CO2");
            }
            
            // 6. Generar y mostrar reporte
            System.out.println("\n" + service.generateReport());
            
            // 7. Guardar en archivo (MANEJO DE ARCHIVOS)
            FileManager fileManager = new FileManager();
            fileManager.saveToFile(service.getEmissions());
            System.out.println("Datos guardados en: src/main/resources/emissions.txt\n");
            
            // 8. Leer desde archivo (MANEJO DE ARCHIVOS)
            List<String> records = fileManager.readFromFile();
            System.out.println("=== LECTURA DEL ARCHIVO ===\n");
            for (String record : records) {
                System.out.println(record);
                System.out.println();
            }
            
        } catch (IOException e) {
            System.err.println("Error al manejar archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
