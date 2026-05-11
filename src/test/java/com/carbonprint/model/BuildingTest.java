package com.carbonprint.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Building
 * Prueba cálculo correcto de huella de carbono
 */
@DisplayName("Pruebas de la clase Building")
class BuildingTest {
    
    private Building building;
    
    @BeforeEach
    void setUp() {
        building = new Building(
                "Edificio Test",
                "Edificio para pruebas",
                8760,
                100000, // kWh
                5,
                "comercial"
        );
    }
    
    @Test
    @DisplayName("Calcula correctamente huella de carbono para edificio")
    void testCarbonFootprintCalculation() {
        // 100000 kWh * 0.4 kg CO2/kWh = 40000 kg = 40 ton CO2
        double expected = 40.0;
        double actual = building.getCarbonFootprint();
        assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    @DisplayName("Getters y Setters funcionan correctamente")
    void testGettersSetters() {
        building.setEnergyConsumptionKwh(50000);
        assertEquals(50000, building.getEnergyConsumptionKwh());
        
        building.setFloors(10);
        assertEquals(10, building.getFloors());
        
        building.setBuildingType("residencial");
        assertEquals("residencial", building.getBuildingType());
    }
    
    @Test
    @DisplayName("toString() retorna formato esperado")
    void testToString() {
        String result = building.toString();
        assertTrue(result.contains("Building"));
        assertTrue(result.contains("Edificio Test"));
        assertTrue(result.contains("ton CO2"));
    }
    
    @Test
    @DisplayName("Hereda correctamente de EmissionSource")
    void testInheritance() {
        assertEquals("Edificio Test", building.getName());
        assertEquals("Edificio para pruebas", building.getDescription());
        assertEquals(8760, building.getYearlyUsage());
    }
}
