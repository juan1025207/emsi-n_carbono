package com.carbonprint.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Bicycle
 * Prueba cálculo correcto de amortización de emisiones de fabricación
 */
@DisplayName("Pruebas de la clase Bicycle")
class BicycleTest {
    
    private Bicycle bicycleNew;
    private Bicycle bicycleOld;
    private Bicycle bicycleAmortized;
    
    @BeforeEach
    void setUp() {
        bicycleNew = new Bicycle(
                "Bicicleta Nueva",
                "Bicicleta recién fabricada",
                365,
                5000,
                "urbana",
                0 // años
        );
        
        bicycleOld = new Bicycle(
                "Bicicleta Antigua",
                "Bicicleta con años de uso",
                365,
                5000,
                "carretera",
                5 // años
        );
        
        bicycleAmortized = new Bicycle(
                "Bicicleta Amortizada",
                "Bicicleta ya amortizada",
                365,
                5000,
                "montaña",
                10 // años - completamente amortizada
        );
    }
    
    @Test
    @DisplayName("Bicicleta nueva tiene huella de fabricación")
    void testNewBicycleHasFootprint() {
        // 0.008 ton / 10 años = 0.0008 ton CO2
        double expected = 0.0008;
        assertEquals(expected, bicycleNew.getCarbonFootprint(), 0.0001);
    }
    
    @Test
    @DisplayName("Bicicleta de 5 años tiene huella reducida")
    void testOldBicycleReducedFootprint() {
        // 0.008 ton / (10 - 5) años = 0.0016 ton CO2
        double expected = 0.0016;
        assertEquals(expected, bicycleOld.getCarbonFootprint(), 0.0001);
    }
    
    @Test
    @DisplayName("Bicicleta de 10+ años no tiene huella")
    void testAmortizedBicycleNoFootprint() {
        // Completamente amortizada
        assertEquals(0.0, bicycleAmortized.getCarbonFootprint());
    }
    
    @Test
    @DisplayName("Nueva emisión es menor que bicicleta vieja")
    void testNewBicycleLessThanOld() {
        assertTrue(bicycleNew.getCarbonFootprint() < bicycleOld.getCarbonFootprint());
    }
    
    @Test
    @DisplayName("Getters y Setters funcionan correctamente")
    void testGettersSetters() {
        bicycleNew.setAnnualKilometers(8000);
        assertEquals(8000, bicycleNew.getAnnualKilometers());
        
        bicycleNew.setYearsOld(3);
        assertEquals(3, bicycleNew.getYearsOld());
    }
}
