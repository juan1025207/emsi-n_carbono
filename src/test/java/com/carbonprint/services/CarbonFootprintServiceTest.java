package com.carbonprint.services;

import com.carbonprint.interfaces.CarbonFootprint;
import com.carbonprint.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase CarbonFootprintService
 * Prueba lógica de negocio y cálculos de agregación
 */
@DisplayName("Pruebas de CarbonFootprintService")
class CarbonFootprintServiceTest {
    
    private CarbonFootprintService service;
    private Building building;
    private Car car;
    private Bicycle bicycle;
    
    @BeforeEach
    void setUp() {
        service = new CarbonFootprintService();
        
        building = new Building("Building", "Test", 8760, 100000, 5, "comercial");
        car = new Car("Car", "Test", 365, 20000, "sedan", "gasolina", 2020);
        bicycle = new Bicycle("Bicycle", "Test", 365, 5000, "urbana", 0);
    }
    
    @Test
    @DisplayName("Agrega correctamente fuentes de emisión")
    void testAddEmissionSource() {
        service.addEmissionSource(building);
        service.addEmissionSource(car);
        
        assertEquals(2, service.getEmissionCount());
    }
    
    @Test
    @DisplayName("Calcula correctamente huella total")
    void testCalculateTotalFootprint() {
        service.addEmissionSource(building);
        service.addEmissionSource(car);
        
        // Building: 40 ton, Car: 4.2 ton
        double expected = 40.0 + 4.2;
        assertEquals(expected, service.calculateTotalFootprint(), 0.0001);
    }
    
    @Test
    @DisplayName("Calcula correctamente huella promedio")
    void testCalculateAverageFootprint() {
        service.addEmissionSource(building);
        service.addEmissionSource(car);
        service.addEmissionSource(bicycle);
        
        double total = service.calculateTotalFootprint();
        double expected = total / 3.0;
        assertEquals(expected, service.calculateAverageFootprint(), 0.0001);
    }
    
    @Test
    @DisplayName("Retorna cero si está vacío")
    void testEmptyListReturnsZero() {
        assertEquals(0.0, service.calculateTotalFootprint());
        assertEquals(0.0, service.calculateAverageFootprint());
    }
    
    @Test
    @DisplayName("Encuentra máximo emisor correctamente")
    void testGetMaxEmissionSource() {
        service.addEmissionSource(car);
        service.addEmissionSource(building);
        service.addEmissionSource(bicycle);
        
        CarbonFootprint max = service.getMaxEmissionSource();
        assertEquals(building, max);
    }
    
    @Test
    @DisplayName("Retorna null si está vacío al buscar máximo")
    void testGetMaxEmptyList() {
        assertNull(service.getMaxEmissionSource());
    }
    
    @Test
    @DisplayName("Genera reporte válido")
    void testGenerateReport() {
        service.addEmissionSource(building);
        
        String report = service.generateReport();
        assertTrue(report.contains("REPORTE DE HUELLA DE CARBONO"));
        assertTrue(report.contains("Número de fuentes: 1"));
    }
    
    @Test
    @DisplayName("Limpia correctamente la lista")
    void testClear() {
        service.addEmissionSource(building);
        service.addEmissionSource(car);
        
        service.clear();
        assertEquals(0, service.getEmissionCount());
    }
}
