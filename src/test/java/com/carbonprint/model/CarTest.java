package com.carbonprint.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Car
 * Prueba cálculo correcto de huella de carbono con diferentes combustibles
 */
@DisplayName("Pruebas de la clase Car")
class CarTest {
    
    private Car carGasoline;
    private Car carDiesel;
    private Car carElectric;
    
    @BeforeEach
    void setUp() {
        carGasoline = new Car(
                "Toyota Corolla",
                "Vehículo gasolina",
                365,
                20000, // km
                "sedan",
                "gasolina",
                2020
        );
        
        carDiesel = new Car(
                "Volkswagen Golf",
                "Vehículo diésel",
                365,
                20000,
                "hatchback",
                "diésel",
                2019
        );
        
        carElectric = new Car(
                "Tesla Model 3",
                "Vehículo eléctrico",
                365,
                20000,
                "sedan",
                "eléctrico",
                2023
        );
    }
    
    @Test
    @DisplayName("Calcula correctamente huella para vehículo gasolina")
    void testGasolineCarbonFootprint() {
        // 20000 km * 0.21 kg CO2/km = 4200 kg = 4.2 ton CO2
        double expected = 4.2;
        assertEquals(expected, carGasoline.getCarbonFootprint(), 0.0001);
    }
    
    @Test
    @DisplayName("Calcula correctamente huella para vehículo diésel")
    void testDieselCarbonFootprint() {
        // 20000 km * 0.17 kg CO2/km = 3400 kg = 3.4 ton CO2
        double expected = 3.4;
        assertEquals(expected, carDiesel.getCarbonFootprint(), 0.0001);
    }
    
    @Test
    @DisplayName("Calcula correctamente huella para vehículo eléctrico")
    void testElectricCarbonFootprint() {
        // 20000 km * 0.05 kg CO2/km = 1000 kg = 1.0 ton CO2
        double expected = 1.0;
        assertEquals(expected, carElectric.getCarbonFootprint(), 0.0001);
    }
    
    @Test
    @DisplayName("Vehículo eléctrico emite menos que gasolina")
    void testElectricLessThanGasoline() {
        assertTrue(carElectric.getCarbonFootprint() < carGasoline.getCarbonFootprint());
    }
    
    @Test
    @DisplayName("Getters y Setters funcionan correctamente")
    void testGettersSetters() {
        carGasoline.setAnnualKilometers(25000);
        assertEquals(25000, carGasoline.getAnnualKilometers());
        
        carGasoline.setYear(2022);
        assertEquals(2022, carGasoline.getYear());
    }
}
