package com.carbonprint.services;

import com.carbonprint.interfaces.CarbonFootprint;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase CarbonFootprintService - Servicio de negocio
 * POLIMORFISMO: Utiliza ArrayList<CarbonFootprint> e itera llamando getCarbonFootprint()
 * Principio SOLID: Single Responsibility (solo lógica de negocio)
 * Principio SOLID: Dependency Inversion (depende de abstracciones, no de implementaciones)
 */
public class CarbonFootprintService {
    
    private List<CarbonFootprint> emissions;
    
    public CarbonFootprintService() {
        this.emissions = new ArrayList<>();
    }
    
    /**
     * Agrega una fuente de emisión
     */
    public void addEmissionSource(CarbonFootprint source) {
        emissions.add(source);
    }
    
    /**
     * Retorna lista de fuentes de emisión
     */
    public List<CarbonFootprint> getEmissions() {
        return new ArrayList<>(emissions);
    }
    
    /**
     * Calcula huella de carbono total
     * POLIMORFISMO: Itera sobre ArrayList<CarbonFootprint> y llama getCarbonFootprint()
     * Cada objeto ejecuta su propia implementación del método
     */
    public double calculateTotalFootprint() {
        double total = 0.0;
        for (CarbonFootprint source : emissions) {
            total += source.getCarbonFootprint();
        }
        return total;
    }
    
    /**
     * Calcula promedio de huella de carbono
     */
    public double calculateAverageFootprint() {
        if (emissions.isEmpty()) {
            return 0.0;
        }
        return calculateTotalFootprint() / emissions.size();
    }
    
    /**
     * Encuentra la mayor fuente de emisión
     */
    public CarbonFootprint getMaxEmissionSource() {
        if (emissions.isEmpty()) {
            return null;
        }
        
        CarbonFootprint max = emissions.get(0);
        for (CarbonFootprint source : emissions) {
            if (source.getCarbonFootprint() > max.getCarbonFootprint()) {
                max = source;
            }
        }
        return max;
    }
    
    /**
     * Genera reporte de emisiones
     */
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== REPORTE DE HUELLA DE CARBONO ===\n");
        report.append("Número de fuentes: ").append(emissions.size()).append("\n");
        report.append("Huella total: ").append(String.format("%.6f", calculateTotalFootprint())).append(" ton CO2\n");
        report.append("Huella promedio: ").append(String.format("%.6f", calculateAverageFootprint())).append(" ton CO2\n");
        report.append("\nDetalle por fuente:\n");
        
        for (int i = 0; i < emissions.size(); i++) {
            CarbonFootprint source = emissions.get(i);
            report.append(i + 1).append(". ").append(source.getClass().getSimpleName())
                  .append(" - Huella: ").append(String.format("%.6f", source.getCarbonFootprint()))
                  .append(" ton CO2\n");
        }
        
        return report.toString();
    }
    
    /**
     * Limpia la lista de emisiones
     */
    public void clear() {
        emissions.clear();
    }
    
    /**
     * Retorna cantidad de fuentes
     */
    public int getEmissionCount() {
        return emissions.size();
    }
}
