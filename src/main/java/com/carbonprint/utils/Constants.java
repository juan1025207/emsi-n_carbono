package com.carbonprint.utils;

/**
 * Clase Constants - Contiene constantes utilizadas en el proyecto
 * Siguiendo principio SOLID Single Responsibility
 */
public class Constants {
    
    public static final String FILE_PATH = "output/emissions.txt";
    public static final String ENCODING = "UTF-8";
    
    // Factores de emisión
    public static final double ELECTRICITY_FACTOR = 0.4; // kg CO2 per kWh
    public static final double GASOLINE_FACTOR = 0.21; // kg CO2 per km
    public static final double DIESEL_FACTOR = 0.17; // kg CO2 per km
    public static final double ELECTRIC_FACTOR = 0.05; // kg CO2 per km
    public static final double BICYCLE_MANUFACTURING = 0.008; // ton CO2
    
    // Separador de registros
    public static final String RECORD_SEPARATOR = "---";
    public static final String FIELD_SEPARATOR = "|";
    
    // Formato de salida
    public static final String DECIMAL_FORMAT = "%.6f";
    
    private Constants() {
        // Constructor privado para evitar instanciación
    }
}
