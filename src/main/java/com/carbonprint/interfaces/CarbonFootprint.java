package com.carbonprint.interfaces;

/**
 * Interface CarbonFootprint define el contrato para calcular la huella de carbono.
 * Implementa el principio SOLID Interface Segregation Principle.
 */
public interface CarbonFootprint {
    
    /**
     * Calcula la huella de carbono en toneladas de CO2 equivalente.
     * @return cantidad de CO2 en toneladas
     */
    double getCarbonFootprint();
}
