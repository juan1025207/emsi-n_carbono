package com.carbonprint.model;

/**
 * Clase Car - Hereda de EmissionSource
 * Calcula huella de carbono basada en distancia recorrida anualmente.
 * Factor: 0.21 kg CO2 por km (promedio vehículo gasolina)
 */
public class Car extends EmissionSource {
    
    private double annualKilometers;
    private String carType; // sedan, suv, hatchback
    private String fuelType; // gasolina, diésel, eléctrico
    private int year;
    
    public Car(String name, String description, double yearlyUsage,
              double annualKilometers, String carType, String fuelType, int year) {
        super(name, description, yearlyUsage);
        this.annualKilometers = annualKilometers;
        this.carType = carType;
        this.fuelType = fuelType;
        this.year = year;
    }
    
    // Getters y Setters específicos de Car
    public double getAnnualKilometers() {
        return annualKilometers;
    }
    
    public void setAnnualKilometers(double annualKilometers) {
        this.annualKilometers = annualKilometers;
    }
    
    public String getCarType() {
        return carType;
    }
    
    public void setCarType(String carType) {
        this.carType = carType;
    }
    
    public String getFuelType() {
        return fuelType;
    }
    
    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }
    
    public int getYear() {
        return year;
    }
    
    public void setYear(int year) {
        this.year = year;
    }
    
    /**
     * Implementación de getCarbonFootprint
     * Factor varía según tipo de combustible:
     * - Gasolina: 0.21 kg CO2/km
     * - Diésel: 0.17 kg CO2/km
     * - Eléctrico: 0.05 kg CO2/km
     */
    @Override
    public double getCarbonFootprint() {
        double emissionFactor;
        
        switch (fuelType.toLowerCase()) {
            case "diésel":
            case "diesel":
                emissionFactor = 0.17;
                break;
            case "eléctrico":
            case "electric":
                emissionFactor = 0.05;
                break;
            case "gasolina":
            case "petrol":
            default:
                emissionFactor = 0.21;
        }
        
        return (annualKilometers * emissionFactor) / 1000.0;
    }
    
    @Override
    public String toString() {
        return String.format("Car{name='%s', description='%s', kilometers=%.2f, " +
                "type='%s', fuel='%s', year=%d, carbonFootprint=%.4f ton CO2}",
                name, description, annualKilometers, carType, fuelType, year, getCarbonFootprint());
    }
}
