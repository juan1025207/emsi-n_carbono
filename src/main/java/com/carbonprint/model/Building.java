package com.carbonprint.model;

/**
 * Clase Building - Hereda de EmissionSource
 * Calcula huella de carbono basada en consumo de energía anual.
 * Factor: 0.4 kg CO2 por kWh (electricidad)
 */
public class Building extends EmissionSource {
    
    private double energyConsumptionKwh; // consumo anual en kWh
    private int floors;
    private String buildingType; // residencial, comercial, industrial
    
    public Building(String name, String description, double yearlyUsage,
                   double energyConsumptionKwh, int floors, String buildingType) {
        super(name, description, yearlyUsage);
        this.energyConsumptionKwh = energyConsumptionKwh;
        this.floors = floors;
        this.buildingType = buildingType;
    }
    
    // Getters y Setters específicos de Building
    public double getEnergyConsumptionKwh() {
        return energyConsumptionKwh;
    }
    
    public void setEnergyConsumptionKwh(double energyConsumptionKwh) {
        this.energyConsumptionKwh = energyConsumptionKwh;
    }
    
    public int getFloors() {
        return floors;
    }
    
    public void setFloors(int floors) {
        this.floors = floors;
    }
    
    public String getBuildingType() {
        return buildingType;
    }
    
    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }
    
    /**
     * Implementación de getCarbonFootprint
     * Huella = consumo kWh * factor emisión (0.4 kg CO2/kWh) / 1000 para convertir a toneladas
     */
    @Override
    public double getCarbonFootprint() {
        return (energyConsumptionKwh * 0.4) / 1000.0;
    }
    
    @Override
    public String toString() {
        return String.format("Building{name='%s', description='%s', energyConsumption=%.2f kWh, " +
                "floors=%d, type='%s', carbonFootprint=%.4f ton CO2}",
                name, description, energyConsumptionKwh, floors, buildingType, getCarbonFootprint());
    }
}
