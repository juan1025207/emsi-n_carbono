package com.carbonprint.model;

/**
 * Clase Bicycle - Hereda de EmissionSource
 * Calcula huella de carbono considerando su producción (después de ciertos años se amortizan emisiones).
 * Factor base: 0.008 ton CO2 de fabricación, amortizado en 10 años
 */
public class Bicycle extends EmissionSource {
    
    private double annualKilometers;
    private String bicycleType; // carretera, montaña, urbana
    private int yearsOld;
    private double manufacturingEmissionsTon; // emisiones de fabricación
    
    public Bicycle(String name, String description, double yearlyUsage,
                  double annualKilometers, String bicycleType, int yearsOld) {
        super(name, description, yearlyUsage);
        this.annualKilometers = annualKilometers;
        this.bicycleType = bicycleType;
        this.yearsOld = yearsOld;
        this.manufacturingEmissionsTon = 0.008; // 8 kg CO2 de fabricación
    }
    
    // Getters y Setters específicos de Bicycle
    public double getAnnualKilometers() {
        return annualKilometers;
    }
    
    public void setAnnualKilometers(double annualKilometers) {
        this.annualKilometers = annualKilometers;
    }
    
    public String getBicycleType() {
        return bicycleType;
    }
    
    public void setBicycleType(String bicycleType) {
        this.bicycleType = bicycleType;
    }
    
    public int getYearsOld() {
        return yearsOld;
    }
    
    public void setYearsOld(int yearsOld) {
        this.yearsOld = yearsOld;
    }
    
    /**
     * Implementación de getCarbonFootprint
     * Huella = emisiones de fabricación amortizadas en 10 años
     * En 10+ años, la bicicleta prácticamente no emite CO2 (es carbono neutral)
     */
    @Override
    public double getCarbonFootprint() {
        // Amortizar emisiones de fabricación en 10 años
        if (yearsOld >= 10) {
            return 0.0; // Ya se amortizaron las emisiones
        }
        
        double remainingYears = 10.0 - yearsOld;
        return manufacturingEmissionsTon / remainingYears;
    }
    
    @Override
    public String toString() {
        return String.format("Bicycle{name='%s', description='%s', kilometers=%.2f, " +
                "type='%s', yearsOld=%d, carbonFootprint=%.6f ton CO2}",
                name, description, annualKilometers, bicycleType, yearsOld, getCarbonFootprint());
    }
}
