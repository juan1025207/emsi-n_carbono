package com.carbonprint.model;

import com.carbonprint.interfaces.CarbonFootprint;

/**
 * Clase abstracta EmissionSource - HERENCIA
 * Define atributos y métodos comunes para todas las fuentes de emisión.
 * Reutiliza código común evitando duplicación.
 */
public abstract class EmissionSource implements CarbonFootprint {
    
    protected String name;
    protected String description;
    protected double yearlyUsage; // horas, km, etc según clase
    
    /**
     * Constructor base para inicializar atributos comunes
     */
    public EmissionSource(String name, String description, double yearlyUsage) {
        this.name = name;
        this.description = description;
        this.yearlyUsage = yearlyUsage;
    }
    
    // REUTILIZACIÓN: getters y setters comunes
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public double getYearlyUsage() {
        return yearlyUsage;
    }
    
    public void setYearlyUsage(double yearlyUsage) {
        this.yearlyUsage = yearlyUsage;
    }
    
    /**
     * Método abstracto que cada subclase debe implementar.
     * Polimorfismo: cada clase calcula su huella de forma diferente.
     */
    @Override
    public abstract double getCarbonFootprint();
    
    @Override
    public abstract String toString();
}
