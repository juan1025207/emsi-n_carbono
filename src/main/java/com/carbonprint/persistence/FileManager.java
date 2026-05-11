package com.carbonprint.persistence;

import com.carbonprint.interfaces.CarbonFootprint;
import com.carbonprint.model.*;
import com.carbonprint.utils.Constants;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase FileManager - Responsable de persistencia
 * Implementa patrón Service para manejo de archivos.
 * Principio SOLID: Single Responsibility (solo gestiona archivos)
 */
public class FileManager {
    
    /**
     * Guarda una lista de objetos CarbonFootprint en archivo .txt
     * Utiliza BufferedWriter para escritura eficiente
     */
    public void saveToFile(List<? extends CarbonFootprint> emissions) throws IOException {
        Path outputPath = Path.of(Constants.FILE_PATH).toAbsolutePath().normalize();
        Path parentDirectory = outputPath.getParent();
        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        try (FileWriter fw = new FileWriter(outputPath.toFile(), StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(fw)) {
            
            for (CarbonFootprint source : emissions) {
                bw.write(formatObjectForFile(source));
                bw.newLine();
                bw.write(Constants.RECORD_SEPARATOR);
                bw.newLine();
            }
            
            bw.flush();
        }
    }
    
    /**
     * Lee registros del archivo y retorna información formateada
     * Utiliza BufferedReader para lectura eficiente
     */
    public List<String> readFromFile() throws IOException {
        List<String> records = new ArrayList<>();
        
        File file = Path.of(Constants.FILE_PATH).toAbsolutePath().normalize().toFile();
        if (!file.exists()) {
            return records;
        }
        
        try (FileReader fr = new FileReader(file, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(fr)) {
            
            StringBuilder record = new StringBuilder();
            String line;
            
            while ((line = br.readLine()) != null) {
                if (line.equals(Constants.RECORD_SEPARATOR)) {
                    if (record.length() > 0) {
                        records.add(record.toString());
                        record = new StringBuilder();
                    }
                } else {
                    record.append(line).append("\n");
                }
            }
            
            if (record.length() > 0) {
                records.add(record.toString());
            }
        }
        
        return records;
    }
    
    /**
     * Formatea un objeto CarbonFootprint para guardarlo en archivo
     */
    private String formatObjectForFile(CarbonFootprint source) {
        if (source instanceof Building) {
            Building b = (Building) source;
            return String.format("Type: Building%s Name: %s%s Description: %s%s Energy: %.2f kWh%s " +
                    "Floors: %d%s Building Type: %s%s Carbon Footprint: %.6f ton CO2",
                    Constants.FIELD_SEPARATOR, b.getName(),
                    Constants.FIELD_SEPARATOR, b.getDescription(),
                    Constants.FIELD_SEPARATOR, b.getEnergyConsumptionKwh(),
                    Constants.FIELD_SEPARATOR, b.getFloors(),
                    Constants.FIELD_SEPARATOR, b.getBuildingType(),
                    Constants.FIELD_SEPARATOR, b.getCarbonFootprint());
        } else if (source instanceof Car) {
            Car c = (Car) source;
            return String.format("Type: Car%s Name: %s%s Description: %s%s Kilometers: %.2f%s " +
                    "Fuel: %s%s Year: %d%s Carbon Footprint: %.6f ton CO2",
                    Constants.FIELD_SEPARATOR, c.getName(),
                    Constants.FIELD_SEPARATOR, c.getDescription(),
                    Constants.FIELD_SEPARATOR, c.getAnnualKilometers(),
                    Constants.FIELD_SEPARATOR, c.getFuelType(),
                    Constants.FIELD_SEPARATOR, c.getYear(),
                    Constants.FIELD_SEPARATOR, c.getCarbonFootprint());
        } else if (source instanceof Bicycle) {
            Bicycle b = (Bicycle) source;
            return String.format("Type: Bicycle%s Name: %s%s Description: %s%s Kilometers: %.2f%s " +
                    "Bicycle Type: %s%s Years Old: %d%s Carbon Footprint: %.6f ton CO2",
                    Constants.FIELD_SEPARATOR, b.getName(),
                    Constants.FIELD_SEPARATOR, b.getDescription(),
                    Constants.FIELD_SEPARATOR, b.getAnnualKilometers(),
                    Constants.FIELD_SEPARATOR, b.getBicycleType(),
                    Constants.FIELD_SEPARATOR, b.getYearsOld(),
                    Constants.FIELD_SEPARATOR, b.getCarbonFootprint());
        }
        return "";
    }
}
