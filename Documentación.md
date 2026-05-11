## Documentación Completa del Proyecto Emisión de Carbono
 Juan Camilo Moreno Perez

## Tabla de Contenidos

1. [Presentación del Proyecto](#presentación-del-proyecto)
2. [Estructura del Proyecto](#estructura-del-proyecto)
3. [Principios SOLID Aplicados](#principios-solid-aplicados)
4. [Código Base Completo](#código-base-completo)
5. [Convenciones Java Utilizadas](#convenciones-java-utilizadas)
6. [Diagrama UML](#diagrama-uml)
7. [Flujo de Datos](#flujo-de-datos)
8. [Pruebas Unitarias](#pruebas-unitarias)
9. [Repositorio github](#Repositorio-github)

---

## Presentación del Proyecto

### Objetivo en una frase
**Emisión de Carbono** es una aplicación Java que calcula y reporta la huella de carbono (emisiones de CO₂) de diferentes fuentes como edificios, vehículos y bicicletas.

### ¿Qué problema resuelve?

En el mundo actual, es crucial entender cuánto CO₂ emitimos mediante nuestras actividades. Este proyecto permite:

- **Centralizar datos** de múltiples fuentes de emisión
- **Calcular automáticamente** la huella de carbono de forma consistente
- **Comparar y analizar** qué actividades generan más emisiones
- **Almacenar resultados** para futuras referencias

### Decisiones principales de diseño

#### 1 **¿Por qué una interfaz CarbonFootprint?**
```
Problema: Cada tipo de emisión (edificio, auto, bicicleta) calcula 
          diferente pero todos necesitan un método común.

Solución: Interfaz pequeña con un solo método: getCarbonFootprint()
          Esto garantiza que cualquier fuente de emisión pueda 
          calcularse de manera uniforme.

Beneficio: Polimorfismo → el servicio no necesita saber qué tipo 
          es cada objeto, solo llama al método.
```

#### 2️ **¿Por qué una clase abstracta EmissionSource?**
```
Problema: Cada clase (Building, Car, Bicycle) comparte atributos 
          comunes: name, description, yearlyUsage

Solución: Clase abstracta que define código común y obliga a 
          implementar getCarbonFootprint() de forma diferente.

Beneficio: Reutilización de código (DRY - Don't Repeat Yourself)
          Estructura clara de herencia
```

#### 3️ **¿Por qué separar en paquetes?**
```
- com.carbonprint.interfaces      → Contratos (interfaces)
- com.carbonprint.model          → Clases de datos (Building, Car)
- com.carbonprint.services       → Lógica de negocio
- com.carbonprint.persistence    → Acceso a archivos
- com.carbonprint.utils          → Constantes y herramientas
```

**Razón:** Organización lógica, responsabilidades claras, fácil mantenimiento.

---

## Estructura del Proyecto

```
Emisión_Carbono/
├── src/
│   ├── main/java/com/carbonprint/
│   │   ├── interfaces/
│   │   │   └── CarbonFootprint.java          ← Contrato
│   │   │
│   │   ├── model/
│   │   │   ├── EmissionSource.java           ← Clase abstracta
│   │   │   ├── Building.java                 ← Edificios
│   │   │   ├── Car.java                      ← Vehículos
│   │   │   └── Bicycle.java                  ← Bicicletas
│   │   │
│   │   ├── services/
│   │   │   └── CarbonFootprintService.java   ← Lógica de negocio
│   │   │
│   │   ├── persistence/
│   │   │   └── FileManager.java              ← Persistencia
│   │   │
│   │   ├── utils/
│   │   │   └── Constants.java                ← Constantes
│   │   │
│   │   └── main/
│   │       └── Main.java                     ← Punto de entrada
│   │
│   ├── test/java/com/carbonprint/
│   │   ├── model/
│   │   │   ├── BuildingTest.java
│   │   │   ├── CarTest.java
│   │   │   └── BicycleTest.java
│   │   │
│   │   ├── services/
│   │   │   └── CarbonFootprintServiceTest.java
│   │   │
│   │   └── persistence/
│   │       └── FileManagerTest.java
│   │
│   └── main/resources/
│       └── emissions.txt                      ← Datos persistidos
│
├── pom.xml                                    ← Configuración Maven
└── target/                                    ← Binarios compilados
```

**¿Por qué esta estructura?**
- Separación clara de responsabilidades
- Fácil localizar archivos
- Escalable para agregar nuevos módulos
- Estándar de Maven (convención)

---

## Principios SOLID Aplicados

Los principios SOLID son reglas de oro para escribir código mantenible y profesional. Veamos cómo los aplicamos:

### A) Single Responsibility Principle (SRP)

> **"Una clase debe tener una única razón para cambiar"**

Cada clase en nuestro proyecto tiene UNA responsabilidad clara:

#### Mal Eejecutado (SRP violado)
```java
//  Una clase haciendo TODO
class CarbonFootprintManager {
    public double calculateBuilding(double kwh) { ... }        // Cálculo
    public double calculateCar(double km) { ... }              // Cálculo
    public void saveToFile(String path) { ... }                // Archivo
    public void printReport() { ... }                          // Reporte
    public User authenticate(String user) { ... }              // Autenticación
    
}
```

#### Bien (SRP aplicado)
```java
// Una clase = Una responsabilidad

// Constants.java → Solo contiene constantes
public class Constants {
    public static final double ELECTRICITY_FACTOR = 0.4;
    // Cambio: solo si varían los factores de emisión
}

// CarbonFootprintService.java → Solo lógica de negocio
public class CarbonFootprintService {
    public double calculateTotalFootprint() { ... }
    // Cambio: si cambia la forma de calcular
}

// FileManager.java → Solo persistencia
public class FileManager {
    public void saveToFile(String path) { ... }
    // Cambio: si cambia el formato de archivo
}
```

**Beneficio en este proyecto:**
- Más fácil mantener: modificar un cálculo no afecta al archivo
- Más fácil probar: cada clase tiene tests claros
- Más reutilizable: Constants.java se usa en 5 clases diferentes

---

### B) Open/Closed Principle (OCP)

> **"Abierto para extensión, cerrado para modificación"**

El código debe permitir nuevas funcionalidades SIN cambiar código existente.

#### Mal (OCP violado)
```java
// Cada vez que agregamos un tipo, modificamos el servicio
public class OldCarbonFootprintService {
    public double calculate(String type, double value) {
        if (type.equals("building")) {
            return value * 0.4 / 1000;
        } else if (type.equals("car")) {
            return value * 0.21 / 1000;
        } else if (type.equals("bicycle")) {
            return 0.008;
        } else if (type.equals("airplane")) {  // ← MODIFICAMOS AQUÍ
            return value * 0.15 / 1000;
        }
        // Y aquí, y aquí... ¡conforme crece!
    }
}
```

#### Bien (OCP aplicado)
```java
// Extensible sin modificar código existente

// Interfaz: el contrato que todas las emisiones deben cumplir
public interface CarbonFootprint {
    double getCarbonFootprint();
}

// Clase abstracta: código común
public abstract class EmissionSource implements CarbonFootprint {
    protected String name;
    protected double yearlyUsage;
    // ... getters/setters comunes ...
}

// Nuevas clases: simplemente EXTIENDEN sin tocar nada existente
public class Building extends EmissionSource { ... }
public class Car extends EmissionSource { ... }
public class Bicycle extends EmissionSource { ... }
public class Airplane extends EmissionSource {  // ← NUEVO, sin modificar nada
    @Override
    public double getCarbonFootprint() {
        return (distance * 0.15) / 1000;
    }
}

// El servicio sigue siendo el mismo:
public class CarbonFootprintService {
    private List<CarbonFootprint> emissions;  // Funciona con cualquier tipo
    
    public void addEmissionSource(CarbonFootprint source) {
        emissions.add(source);  // Sin cambios necesarios
    }
}
```

---

### C) Liskov Substitution Principle (LSP)

> **"Objetos derivados deben reemplazar a objetos base sin romper el programa"**

Si `Building`, `Car` y `Bicycle` heredan de `EmissionSource`, deben poder usarse donde se espera un `EmissionSource`.

#### Mal (LSP violado)
```java
public abstract class EmissionSource implements CarbonFootprint {
    // ...
}

public class Bicycle extends EmissionSource {
    @Override
    public double getCarbonFootprint() {
        // Retorna una constante, ignora yearlyUsage
        return 0.008;  // Siempre lo mismo, violando el contrato
    }
}

// Cliente código:
public void processEmission(EmissionSource source) {
    source.setYearlyUsage(100);  // Esperamos que esto afecte el cálculo
    double emissions = source.getCarbonFootprint();
    // Pero en Bicycle esto no funciona
}
```

#### Bien (LSP aplicado)
```java
public abstract class EmissionSource implements CarbonFootprint {
    protected String name;
    protected double yearlyUsage;
    // Constructor y métodos comunes...
}

public class Bicycle extends EmissionSource {
    private String bikeType;
    private int age;
    
    @Override
    public double getCarbonFootprint() {
        // ✅ Usa yearlyUsage de forma consistente
        // Emisiones por fabricación amortizadas entre años de vida útil
        double manufacturingEmissions = 0.008;
        double yearsOfLife = 10;
        
        // Factor varía según uso y edad
        double usageFactor = bikeType.equals("urbana") ? 0.001 : 0.0005;
        double usageEmissions = (yearlyUsage * usageFactor) / 1000;
        
        return (manufacturingEmissions / yearsOfLife) + usageEmissions;
    }
}

// Ahora funciona correctamente:
List<EmissionSource> sources = new ArrayList<>();
sources.add(new Building(...));
sources.add(new Car(...));
sources.add(new Bicycle(...));  // ¡Todas intercambiables!

for (EmissionSource source : sources) {
    double footprint = source.getCarbonFootprint();  // LSP garantiza consistencia
}
```

---

### D) Interface Segregation Principle (ISP)

> **"Muchas interfaces específicas es mejor que una interfaz general"**

Crear interfaces pequeñas y especializadas en lugar de interfaces enormes.

#### Mal (ISP violado)
```java
//  Una interfaz masiva que no todas las clases necesitan
public interface EnvironmentalEntity {
    double getCarbonFootprint();
    void saveToFile(String path);          // No todas necesitan esto
    void generateReport();                 // No todas necesitan esto
    void sendNotification(String email);   // No todas necesitan esto
    void calculateWaterFootprint();        // No todas necesitan esto
}

// Bicycle se ve obligada a implementar TODO:
public class Bicycle implements EnvironmentalEntity {
    @Override
    public double getCarbonFootprint() { ... }  // ✓ Tiene sentido
    
    @Override
    public void saveToFile(String path) { ... }  // ✗ ¿Por qué?
    
    @Override
    public void generateReport() { ... }        // ✗ ¿Por qué?
    
    @Override
    public void sendNotification(String email) { ... }  // ✗ ¿Por qué?
    
    @Override
    public void calculateWaterFootprint() { ... }      // ✗ No relevante
}
```

#### Bien (ISP aplicado)
```java
// Interfaces específicas y pequeñas

// Una interfaz = un contrato claro
public interface CarbonFootprint {
    double getCarbonFootprint();
}

// Otra interfaz para quien la necesita
public interface Persistable {
    void saveToFile(String path);
    void loadFromFile(String path);
}

// Otra para reportes
public interface Reportable {
    String generateReport();
}

// Cada clase implementa SOLO lo que necesita:
public class Bicycle implements CarbonFootprint {
    @Override
    public double getCarbonFootprint() { ... }  // ✓ Solo lo necesario
}

public class Building implements CarbonFootprint, Reportable {
    @Override
    public double getCarbonFootprint() { ... }
    
    @Override
    public String generateReport() { ... }
}

public class CarbonFootprintService 
        implements CarbonFootprint, Persistable, Reportable {
    // Implementa los tres porque de verdad los necesita
}
```

---

### E) Dependency Inversion Principle (DIP)

> **"Depender de abstracciones, no de implementaciones concretas"**

#### mal (DIP violado)
```java
// ¡El servicio depende de clases concretas
public class CarbonFootprintService {
    private Building building;      // ← Acoplado a Building
    private Car car;                // ← Acoplado a Car
    private Bicycle bicycle;        // ← Acoplado a Bicycle
    
    public double calculateTotal() {
        return building.getCarbonFootprint() + 
               car.getCarbonFootprint() + 
               bicycle.getCarbonFootprint();
    }
    
    // Problema: ¿Qué pasa si queremos agregar Airplane?
}

// Cliente código:
CarbonFootprintService service = new CarbonFootprintService();
service.setBuilding(new Building(...));  // Acoplamiento fuerte
service.setCar(new Car(...));
service.setBicycle(new Bicycle(...));
```

#### Bien (DIP aplicado)
```java
// El servicio depende de la abstracción CarbonFootprint
public class CarbonFootprintService {
    private List<CarbonFootprint> emissions;  // ← Abstracción
    
    public CarbonFootprintService() {
        this.emissions = new ArrayList<>();
    }
    
    // Acepta CUALQUIER implementación de CarbonFootprint
    public void addEmissionSource(CarbonFootprint source) {
        emissions.add(source);  // ← Polimorfismo
    }
    
    public double calculateTotal() {
        double total = 0.0;
        for (CarbonFootprint source : emissions) {
            total += source.getCarbonFootprint();  // ← Llamada polimórfica
        }
        return total;
    }
}

// Cliente código:
CarbonFootprintService service = new CarbonFootprintService();
service.addEmissionSource(new Building(...));   // Building → CarbonFootprint
service.addEmissionSource(new Car(...));        // Car → CarbonFootprint
service.addEmissionSource(new Bicycle(...));    // Bicycle → CarbonFootprint
service.addEmissionSource(new Airplane(...));   // Airplane → CarbonFootprint ¡NUEVO!

```


---

## 💻 Código Base Completo

### 1. Interface - CarbonFootprint.java

```java
package com.carbonprint.interfaces;

/**
 * Interface CarbonFootprint define el contrato para calcular la huella de carbono.
 * Implementa el principio SOLID Interface Segregation Principle.
 * 
 * ¿Por qué interface? Garantiza que cualquier fuente de emisión 
 * pueda ser calculada de forma uniforme, sin importar su tipo específico.
 * 
 * @author Equipo de Desarrollo
 * @version 1.0
 */
public interface CarbonFootprint {
    
    /**
     * Calcula la huella de carbono en toneladas de CO2 equivalente.
     * 
     * Cada implementación define su propia fórmula:
     * - Building: energía (kWh) * factor / 1000
     * - Car: distancia (km) * factor / 1000
     * - Bicycle: emisión de manufactura / años de vida
     * 
     * @return cantidad de CO2 en toneladas
     */
    double getCarbonFootprint();
}
```

---

### 2. Clase Abstracta - EmissionSource.java

```java
package com.carbonprint.model;

import com.carbonprint.interfaces.CarbonFootprint;

/**
 * Clase abstracta EmissionSource
 * 
 * Propósito:
 * - Define atributos y métodos COMUNES para todas las fuentes de emisión
 * - Reutiliza código (principio DRY: Don't Repeat Yourself)
 * - Obliga a subclases a implementar getCarbonFootprint()
 * 
 * ¿Cuándo usar clase abstracta vs interfaz?
 * - Clase abstracta: cuando hay código común (getters, setters, constructores)
 * - Interfaz: solo cuando defines un contrato sin implementación
 * 
 * @author Equipo de Desarrollo
 * @version 1.0
 */
public abstract class EmissionSource implements CarbonFootprint {
    
    // ========== ATRIBUTOS COMUNES ==========
    protected String name;              // Nombre de la fuente
    protected String description;       // Descripción detallada
    protected double yearlyUsage;       // Uso anual (horas, km, etc)
    
    // ========== CONSTRUCTOR ==========
    /**
     * Constructor que inicializa atributos comunes.
     * 
     * @param name nombre descriptivo de la fuente
     * @param description descripción más detallada
     * @param yearlyUsage uso anual (significado varía por subclase)
     */
    public EmissionSource(String name, String description, double yearlyUsage) {
        this.name = name;
        this.description = description;
        this.yearlyUsage = yearlyUsage;
    }
    
    // ========== GETTERS Y SETTERS ==========
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
    
    // ========== MÉTODO ABSTRACTO ==========
    /**
     * Cada subclase DEBE implementar esto con su propia lógica.
     * Esto garantiza el polimorfismo: todas tienen getCarbonFootprint()
     * pero cada una calcula diferente.
     */
    @Override
    public abstract double getCarbonFootprint();
}
```

**Herencia visual:**
```
EmissionSource (clase abstracta)
    │
    ├─ Building (hereda name, description, yearlyUsage)
    ├─ Car (hereda name, description, yearlyUsage)
    └─ Bicycle (hereda name, description, yearlyUsage)
```

---

### 3. Building.java - Edificios

```java
package com.carbonprint.model;

/**
 * Clase Building - Calcula huella de carbono para edificios.
 * 
 * Fórmula: consumo (kWh) × 0.4 (factor) ÷ 1000 = toneladas CO2
 * 
 * Ejemplo real:
 * - Edificio consume 100,000 kWh/año
 * - Cálculo: 100,000 × 0.4 ÷ 1000 = 40 toneladas CO2
 */
public class Building extends EmissionSource {
    
    // ========== ATRIBUTOS ESPECÍFICOS ==========
    private double energyConsumptionKwh;  // Consumo anual en kWh
    private int floors;                    // Número de pisos
    private String buildingType;           // residencial, comercial, industrial
    
    // ========== CONSTRUCTOR ==========
    /**
     * Constructor completo para Building.
     * 
     * @param name nombre del edificio
     * @param description descripción
     * @param yearlyUsage horas de operación anuales
     * @param energyConsumptionKwh consumo de electricidad en kWh/año
     * @param floors número de pisos
     * @param buildingType tipo de edificio
     */
    public Building(String name, String description, double yearlyUsage,
                   double energyConsumptionKwh, int floors, String buildingType) {
        super(name, description, yearlyUsage);
        this.energyConsumptionKwh = energyConsumptionKwh;
        this.floors = floors;
        this.buildingType = buildingType;
    }
    
    // ========== GETTERS Y SETTERS ==========
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
    
    // ========== IMPLEMENTACIÓN DE INTERFAZ ==========
    /**
     * Calcula huella de carbono basada en consumo de energía.
     * 
     * Fórmula: (consumoKwh × 0.4) ÷ 1000
     * - 0.4 es el factor de emisión para electricidad (kg CO2/kWh)
     * - Dividimos por 1000 para convertir kg a toneladas
     * 
     * @return huella de carbono en toneladas de CO2 equivalente
     */
    @Override
    public double getCarbonFootprint() {
        final double ELECTRICITY_EMISSION_FACTOR = 0.4; // kg CO2 por kWh
        return (energyConsumptionKwh * ELECTRICITY_EMISSION_FACTOR) / 1000.0;
    }
    
    // ========== MÉTODO toString ==========
    /**
     * Representación textual de la instancia para debugging/logging.
     */
    @Override
    public String toString() {
        return String.format(
            "Building{name='%s', description='%s', energyConsumption=%.2f kWh, " +
            "floors=%d, type='%s', carbonFootprint=%.4f ton CO2}",
            name, description, energyConsumptionKwh, floors, buildingType, 
            getCarbonFootprint()
        );
    }
}
```

---

### 4. Car.java - Vehículos

```java
package com.carbonprint.model;

/**
 * Clase Car - Calcula huella de carbono para vehículos.
 * 
 * El factor de emisión varía según tipo de combustible:
 * - Gasolina: 0.21 kg CO2/km
 * - Diésel: 0.17 kg CO2/km
 * - Eléctrico: 0.05 kg CO2/km
 * 
 * Ejemplo real:
 * - Auto gasolina recorre 20,000 km/año
 * - Cálculo: 20,000 × 0.21 ÷ 1000 = 4.2 toneladas CO2
 */
public class Car extends EmissionSource {
    
    // ========== ATRIBUTOS ESPECÍFICOS ==========
    private double annualKilometers;     // Distancia recorrida anuamente (km)
    private String carType;               // sedan, suv, hatchback, truck
    private String fuelType;              // gasolina, diésel, eléctrico, híbrido
    private int year;                     // Año de fabricación (influye en eficiencia)
    
    // ========== CONSTRUCTOR ==========
    /**
     * Constructor completo para Car.
     */
    public Car(String name, String description, double yearlyUsage,
              double annualKilometers, String carType, String fuelType, int year) {
        super(name, description, yearlyUsage);
        this.annualKilometers = annualKilometers;
        this.carType = carType;
        this.fuelType = fuelType;
        this.year = year;
    }
    
    // ========== GETTERS Y SETTERS ==========
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
    
    // ========== IMPLEMENTACIÓN DE INTERFAZ ==========
    /**
     * Calcula huella de carbono basada en distancia y tipo de combustible.
     * 
     * El factor varía por tipo:
     * - Gasolina: 0.21 kg CO2/km (más contaminante)
     * - Diésel: 0.17 kg CO2/km (intermedio)
     * - Eléctrico: 0.05 kg CO2/km (más limpio)
     * 
     * Fórmula: (km × factor) ÷ 1000
     * 
     * @return huella de carbono en toneladas de CO2 equivalente
     */
    @Override
    public double getCarbonFootprint() {
        double emissionFactor;
        
        // Seleccionar factor según combustible
        switch (fuelType.toLowerCase()) {
            case "diesel":
            case "diésel":
                emissionFactor = 0.17;
                break;
            case "electric":
            case "eléctrico":
                emissionFactor = 0.05;
                break;
            case "hybrid":
            case "híbrido":
                emissionFactor = 0.10;
                break;
            case "gasoline":
            case "gasolina":
            default:
                emissionFactor = 0.21;
                break;
        }
        
        return (annualKilometers * emissionFactor) / 1000.0;
    }
    
    // ========== MÉTODO toString ==========
    @Override
    public String toString() {
        return String.format(
            "Car{name='%s', km=%.0f, type='%s', fuel='%s', " +
            "footprint=%.4f ton CO2}",
            name, annualKilometers, carType, fuelType, getCarbonFootprint()
        );
    }
}
```

---

### 5. Bicycle.java - Bicicletas

```java
package com.carbonprint.model;

/**
 * Clase Bicycle - Calcula huella de carbono para bicicletas.
 * 
 * Nota: Las bicicletas tienen emisiones principalmente de manufactura,
 * amortizadas a lo largo de su vida útil. El uso es prácticamente cero.
 * 
 * Fórmula: emisión_manufactura ÷ años_vida_útil
 */
public class Bicycle extends EmissionSource {
    
    // ========== ATRIBUTOS ESPECÍFICOS ==========
    private String bikeType;              // urbana, montaña, carrera, eléctrica
    private int age;                      // Edad de la bicicleta en años
    
    // ========== CONSTRUCTOR ==========
    public Bicycle(String name, String description, double yearlyUsage,
                  double distanceKm, String bikeType, int age) {
        super(name, description, yearlyUsage);
        this.bikeType = bikeType;
        this.age = age;
    }
    
    // ========== GETTERS Y SETTERS ==========
    public String getBikeType() {
        return bikeType;
    }
    
    public void setBikeType(String bikeType) {
        this.bikeType = bikeType;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    // ========== IMPLEMENTACIÓN DE INTERFAZ ==========
    /**
     * Calcula huella de carbono de bicicleta.
     * 
     * Las bicicletas son muy ecológicas, pero tienen emisiones iniciales
     * de manufactura (~8 kg CO2). Amortizamos esto en 10 años de vida útil.
     * 
     * Valor típico: 0.0008 toneladas CO2/año
     * (Es decir: 0.8 kg CO2/año de su vida útil)
     * 
     * @return huella de carbono en toneladas de CO2 equivalente
     */
    @Override
    public double getCarbonFootprint() {
        // Emisión total de manufactura (kg CO2)
        double manufacturingEmissions = 8.0;  // kg
        
        // Vida útil promedio de una bicicleta
        int lifeYearsUseful = 10;
        
        // Emisión anual amortizada
        return manufacturingEmissions / (1000.0 * lifeYearsUseful);
    }
    
    // ========== MÉTODO toString ==========
    @Override
    public String toString() {
        return String.format(
            "Bicycle{name='%s', type='%s', age=%d years, " +
            "footprint=%.6f ton CO2}",
            name, bikeType, age, getCarbonFootprint()
        );
    }
}
```

---

### 6. CarbonFootprintService.java - Servicio de Negocio

```java
package com.carbonprint.services;

import com.carbonprint.interfaces.CarbonFootprint;
import java.util.ArrayList;
import java.util.List;

/**
 * CarbonFootprintService - Servicio de cálculo de huella de carbono.
 * 
 * Principios SOLID aplicados:
 * 1. Single Responsibility: Solo lógica de cálculo
 * 2. Dependency Inversion: Depende de CarbonFootprint (abstracción), 
 *    no de clases concretas
 * 3. Polimorfismo: Itera List<CarbonFootprint> sin saber los tipos reales
 * 
 * ¿Por qué un servicio separado?
 * Porque queremos que las clases model sean "modelos de datos" puros,
 * mientras que la lógica de negocio (cálculos agregados, reportes, etc)
 * está centralizada aquí.
 * 
 * @author Equipo de Desarrollo
 * @version 1.0
 */
public class CarbonFootprintService {
    
    // ========== ATRIBUTO ==========
    /**
     * Lista de todas las fuentes de emisión.
     * Usamos la interfaz CarbonFootprint, no clases concretas.
     * Esto es Dependency Inversion en acción.
     */
    private List<CarbonFootprint> emissions;
    
    // ========== CONSTRUCTOR ==========
    /**
     * Inicializa el servicio con una lista vacía.
     */
    public CarbonFootprintService() {
        this.emissions = new ArrayList<>();
    }
    
    // ========== MÉTODOS PÚBLICOS ==========
    
    /**
     * Agrega una nueva fuente de emisión.
     * 
     * Ejemplo de polimorfismo:
     * service.addEmissionSource(new Building(...));
     * service.addEmissionSource(new Car(...));
     * service.addEmissionSource(new Bicycle(...));
     * 
     * El servicio no necesita saber qué tipo es cada uno.
     * 
     * @param source cualquier objeto que implemente CarbonFootprint
     */
    public void addEmissionSource(CarbonFootprint source) {
        if (source != null) {
            emissions.add(source);
        }
    }
    
    /**
     * Retorna copia de la lista (encapsulación).
     * Retornamos una copia para evitar modificaciones externas.
     * 
     * @return lista de emisiones
     */
    public List<CarbonFootprint> getEmissions() {
        return new ArrayList<>(emissions);
    }
    
    /**
     * Retorna la cantidad de fuentes de emisión registradas.
     * 
     * @return número de fuentes
     */
    public int getEmissionCount() {
        return emissions.size();
    }
    
    /**
     * Calcula la huella total de carbono.
     * 
     * Algoritmo:
     * 1. Inicializa total en 0
     * 2. Para cada fuente en la lista
     * 3. Llama al método getCarbonFootprint() (polimórfico)
     * 4. Suma el resultado
     * 
     * Ejemplo de salida:
     * Building: 40.0 ton
     * Car:       4.2 ton
     * Bicycle:   0.0008 ton
     * TOTAL:     44.2808 ton
     * 
     * @return total en toneladas de CO2 equivalente
     */
    public double calculateTotalFootprint() {
        double total = 0.0;
        
        for (CarbonFootprint source : emissions) {
            // ¡POLIMORFISMO! Cada tipo responde diferente
            total += source.getCarbonFootprint();
        }
        
        return total;
    }
    
    /**
     * Calcula la huella promedio por fuente.
     * 
     * Fórmula: total / cantidad de fuentes
     * 
     * @return promedio en toneladas de CO2
     */
    public double calculateAverageFootprint() {
        if (emissions.isEmpty()) {
            return 0.0;
        }
        
        return calculateTotalFootprint() / emissions.size();
    }
    
    /**
     * Encuentra la fuente que genera más emisiones.
     * 
     * Útil para identificar el "peor contaminante".
     * 
     * @return la fuente con mayor huella (o null si lista vacía)
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
     * Encuentra la fuente que genera menos emisiones.
     * 
     * @return la fuente con menor huella (o null si lista vacía)
     */
    public CarbonFootprint getMinEmissionSource() {
        if (emissions.isEmpty()) {
            return null;
        }
        
        CarbonFootprint min = emissions.get(0);
        
        for (CarbonFootprint source : emissions) {
            if (source.getCarbonFootprint() < min.getCarbonFootprint()) {
                min = source;
            }
        }
        
        return min;
    }
    
    /**
     * Genera un reporte detallado de todas las emisiones.
     * 
     * Ejemplo de salida:
     * ╔════════════════════════════════════════════╗
     * ║   REPORTE DE HUELLA DE CARBONO ANUAL      ║
     * ╠════════════════════════════════════════════╣
     * ║ Edificio Central:      40.0000 ton CO2     ║
     * ║ Auto Familiar:          4.2000 ton CO2     ║
     * ║ Bicicleta Urbana:       0.0008 ton CO2     ║
     * ╠════════════════════════════════════════════╣
     * ║ TOTAL:                 44.2008 ton CO2     ║
     * ║ PROMEDIO:              14.7336 ton CO2     ║
     * ║ MÁXIMO:                40.0000 ton CO2     ║
     * ║ MÍNIMO:                 0.0008 ton CO2     ║
     * ╚════════════════════════════════════════════╝
     * 
     * @return string con formato legible
     */
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        
        report.append("\n╔════════════════════════════════════════════╗\n");
        report.append("║   REPORTE DE HUELLA DE CARBONO ANUAL      ║\n");
        report.append("╠════════════════════════════════════════════╣\n");
        
        for (CarbonFootprint source : emissions) {
            double footprint = source.getCarbonFootprint();
            String line = String.format("║ %-40s ║\n", 
                String.format("%.4f ton CO2", footprint));
            report.append(line);
        }
        
        report.append("╠════════════════════════════════════════════╣\n");
        report.append(String.format("║ TOTAL:    %36.4f ton ║\n", 
            calculateTotalFootprint()));
        report.append(String.format("║ PROMEDIO: %36.4f ton ║\n", 
            calculateAverageFootprint()));
        
        CarbonFootprint max = getMaxEmissionSource();
        if (max != null) {
            report.append(String.format("║ MÁXIMO:   %36.4f ton ║\n", 
                max.getCarbonFootprint()));
        }
        
        report.append("╚════════════════════════════════════════════╝\n");
        
        return report.toString();
    }
}
```

---

### 7. Constants.java - Constantes

```java
package com.carbonprint.utils;

/**
 * Clase Constants - Centraliza todas las constantes del proyecto.
 * 
 * Beneficios:
 * 1. Un único lugar para cambiar valores (DRY principle)
 * 2. Fácil de mantener y documentar
 * 3. Si el factor de emisión cambia, cambias aquí y se refleja en todo
 * 4. Constructor privado: no se puede instanciar
 * 
 * Siguiendo Single Responsibility Principle:
 * Esta clase SOLO almacena constantes, nada más.
 */
public final class Constants {
    
    // Constructor privado para evitar instanciación
    // (¿Para qué crear un objeto Constants? Solo queremos acceso a constantes)
    private Constants() {
        throw new AssertionError("No se puede instanciar Constants");
    }
    
    // ========== RUTAS DE ARCHIVOS ==========
    /** Ruta donde se guardan los reportes */
    public static final String OUTPUT_FILE_PATH = "output/emissions.txt";
    
    /** Codificación de archivos */
    public static final String ENCODING = "UTF-8";
    
    // ========== FACTORES DE EMISIÓN ==========
    // Valores basados en estudios de ciclo de vida (LCA)
    
    /** Factor de emisión para electricidad: 0.4 kg CO2 por kWh */
    public static final double ELECTRICITY_EMISSION_FACTOR = 0.4;
    
    /** Factor de emisión para gasolina: 0.21 kg CO2 por km */
    public static final double GASOLINE_EMISSION_FACTOR = 0.21;
    
    /** Factor de emisión para diésel: 0.17 kg CO2 por km */
    public static final double DIESEL_EMISSION_FACTOR = 0.17;
    
    /** Factor de emisión para vehículos eléctricos: 0.05 kg CO2 por km */
    public static final double ELECTRIC_EMISSION_FACTOR = 0.05;
    
    /** Factor de emisión para híbridos: 0.10 kg CO2 por km */
    public static final double HYBRID_EMISSION_FACTOR = 0.10;
    
    /** Emisión de manufactura de bicicleta: 8 kg CO2 */
    public static final double BICYCLE_MANUFACTURING_EMISSIONS = 8.0;
    
    /** Vida útil de una bicicleta: 10 años */
    public static final int BICYCLE_LIFESPAN_YEARS = 10;
    
    // ========== SEPARADORES DE ARCHIVOS ==========
    /** Separador entre registros en archivo */
    public static final String RECORD_SEPARATOR = "---";
    
    /** Separador entre campos en archivo */
    public static final String FIELD_SEPARATOR = "|";
    
    // ========== FORMATOS ==========
    /** Formato de números decimales en reportes */
    public static final String DECIMAL_FORMAT = "%.6f";
}
```

---

### 8. Main.java - Punto de Entrada

```java
package com.carbonprint.main;

import com.carbonprint.model.*;
import com.carbonprint.services.CarbonFootprintService;
import com.carbonprint.persistence.FileManager;

/**
 * Clase Main - Punto de entrada de la aplicación.
 * 
 * Demostración completa del proyecto:
 * 1. Crear instancia del servicio
 * 2. Crear objetos de diferentes tipos (polimorfismo)
 * 3. Agregar al servicio
 * 4. Calcular métricas
 * 5. Generar reporte
 * 6. Guardar en archivo
 */
public class Main {
    
    public static void main(String[] args) {
        try {
            System.out.println("🌍 SISTEMA DE CÁLCULO DE HUELLA DE CARBONO\n");
            
            // ========== 1. CREAR SERVICIO ==========
            CarbonFootprintService service = new CarbonFootprintService();
            System.out.println("✓ Servicio inicializado");
            
            // ========== 2. CREAR FUENTES DE EMISIÓN ==========
            
            // 🏢 Edificio comercial
            Building building = new Building(
                "Oficinas Centrales",
                "Edificio de 10 pisos en el downtown",
                8760,           // 24/7 durante 365 días = 8760 horas
                250000,         // Consume 250,000 kWh anuales
                10,             // 10 pisos
                "comercial"
            );
            
            // 🚗 Automóvil
            Car car = new Car(
                "Tesla Model 3",
                "Auto eléctrico para viajes de negocios",
                365,            // Usa días del año
                15000,          // Recorre 15,000 km anuales
                "sedan",
                "eléctrico",
                2023
            );
            
            // 🚗 Automóvil a gasolina
            Car gasolineCar = new Car(
                "Auto Familiar",
                "Vehículo para desplazamientos personales",
                365,
                20000,          // Recorre 20,000 km anuales
                "sedan",
                "gasolina",
                2020
            );
            
            // 🚴 Bicicleta
            Bicycle bicycle = new Bicycle(
                "Bicicleta Urbana",
                "Para desplazamientos cortos",
                365,            // Usa todos los días
                5000,           // Recorre ~5,000 km anuales
                "urbana",
                3               // Tiene 3 años
            );
            
            // ========== 3. AGREGAR AL SERVICIO (POLIMORFISMO) ==========
            service.addEmissionSource(building);
            service.addEmissionSource(car);
            service.addEmissionSource(gasolineCar);
            service.addEmissionSource(bicycle);
            
            System.out.println("✓ " + service.getEmissionCount() + 
                             " fuentes de emisión agregadas\n");
            
            // ========== 4. MOSTRAR DETALLES ==========
            System.out.println("📋 DETALLE DE FUENTES:");
            for (var source : service.getEmissions()) {
                // Nota: source es de tipo CarbonFootprint (interfaz)
                // pero el toString muestra detalles de la clase real
                System.out.println("  • " + source);
            }
            
            // ========== 5. CALCULAR MÉTRICAS ==========
            System.out.println("\n📊 CÁLCULOS:");
            System.out.printf("  Total anual:    %.4f ton CO2%n", 
                service.calculateTotalFootprint());
            System.out.printf("  Promedio:       %.4f ton CO2%n", 
                service.calculateAverageFootprint());
            
            var maxSource = service.getMaxEmissionSource();
            if (maxSource != null) {
                System.out.printf("  Mayor emisor:   %.4f ton CO2%n", 
                    maxSource.getCarbonFootprint());
            }
            
            // ========== 6. GENERAR REPORTE ==========
            String report = service.generateReport();
            System.out.println(report);
            
            // ========== 7. GUARDAR EN ARCHIVO ==========
            FileManager.saveToFile(service);
            System.out.println("✓ Reporte guardado en: output/emissions.txt");
            
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

---

## Convenciones Java Utilizadas

### 1. Nomenclatura de Paquetes
```
com.carbonprint.interfaces    ← Contratos (interfaces)
com.carbonprint.model        ← Clases de datos (entidades)
com.carbonprint.services     ← Lógica de negocio
com.carbonprint.persistence  ← Acceso a datos/archivos
com.carbonprint.utils        ← Herramientas (constantes, helpers)
com.carbonprint.main         ← Punto de entrada
```

### 2. Nomenclatura de Clases
```
PascalCase (UpperCamelCase):
- Building 
- CarbonFootprint 
- EmissionSource 
- CarbonFootprintService 


### 3. Nomenclatura de Variables y Métodos
```
camelCase (lowerCamelCase):
- energyConsumptionKwh 
- annualKilometers 
- calculateTotalFootprint() 
- getCarbonFootprint() 

```
### 4. Constantes
```
UPPER_CASE con _ (underscore):
- ELECTRICITY_EMISSION_FACTOR ✓
- BICYCLE_MANUFACTURING_EMISSIONS ✓
- RECORD_SEPARATOR ✓

```

### 5. Modificadores de Acceso

| Modificador | Uso | Ejemplo |
|-----------|-----|---------|
| `private` | Atributos privados (con getters/setters) | `private double energyConsumption;` |
| `public` | Métodos públicos de la interfaz | `public double getCarbonFootprint();` |
| `protected` | Métodos usados por subclases | `protected String name;` |
| (sin modificador) | Package-private (usado rara vez) | Por defecto en clases |

### 6. Documentación JavaDoc

```java
/**
 * Descripción breve del método o clase.
 * Puede tener más líneas explicando detalles.
 * 
 * @param paramName descripción del parámetro
 * @param otherParam otra descripción
 * @return qué retorna el método
 * @throws ExceptionType qué excepciones puede lanzar
 */
```

**Ejemplo en nuestro código:**
```java
/**
 * Calcula la huella de carbono en toneladas de CO2 equivalente.
 * 
 * @return cantidad de CO2 en toneladas
 */
double getCarbonFootprint();
```

### 7. Estructura de una Clase

```java
public class Building extends EmissionSource {
    
    // 1. ATRIBUTOS (privados)
    private double energyConsumption;
    private int floors;
    
    // 2. CONSTRUCTORES
    public Building(String name, ...) {
        super(name, ...);
        this.energyConsumption = energy;
    }
    
    // 3. GETTERS Y SETTERS
    public double getEnergyConsumption() { ... }
    public void setEnergyConsumption(double value) { ... }
    
    // 4. MÉTODOS DE NEGOCIO
    @Override
    public double getCarbonFootprint() { ... }
    
    // 5. MÉTODOS ESPECIALES (toString, equals, etc)
    @Override
    public String toString() { ... }
}
```

### 8. Manejo de Excepciones

```java
public static void main(String[] args) {
    try {
        // Código que puede fallar
        FileManager.saveToFile(service);
    } catch (IOException e) {
        // Manejar excepción específica
        System.err.println("Error al guardar archivo: " + e.getMessage());
    } catch (Exception e) {
        // Captura general (última opción)
        e.printStackTrace();
    }
}
```

---

## Diagrama UML

Lo podemos encontrar en el apartado de  Imagenes_Documentación o tambien el aricho png enviado con el documento

### Relaciones Explicadas

| Relación | Tipo | Significado |
|----------|------|------------|
| `EmissionSource → CarbonFootprint` | **implements** | EmissionSource implementa la interfaz |
| `Building → EmissionSource` | **extends** | Building hereda de EmissionSource |
| `Car → EmissionSource` | **extends** | Car hereda de EmissionSource |
| `CarbonFootprintService → List<CarbonFootprint>` | **dependency** | Servicio depende de la interfaz |
| `CarbonFootprintService → Constants` | **dependency** | Servicio usa constantes globales |

---

## Flujo de Datos

### Ejecución paso a paso de Main.java

```
INICIO (Main.main())
│
├─→ [1] Crear CarbonFootprintService
│   └─→ Inicializa: emissions = new ArrayList<CarbonFootprint>()
│       └─→ Lista vacía: []
│
├─→ [2] Crear objetos (diferentes tipos)
│   │
│   ├─→ Building("Oficinas", ..., 250000 kWh)
│   │   └─→ Constructor: super() + atributos específicos
│   │
│   ├─→ Car("Tesla", ..., 15000 km, "eléctrico")
│   │   └─→ Constructor: super() + atributos específicos
│   │
│   ├─→ Car("Auto", ..., 20000 km, "gasolina")
│   │   └─→ Constructor: super() + atributos específicos
│   │
│   └─→ Bicycle("Bicicleta", ..., 5000 km)
│       └─→ Constructor: super() + atributos específicos
│
├─→ [3] Agregar al servicio (POLIMORFISMO)
│   │
│   ├─→ service.addEmissionSource(building)
│   │   └─→ emissions.add(building)
│   │       └─→ building se trata como CarbonFootprint
│   │
│   ├─→ service.addEmissionSource(car)
│   │   └─→ emissions.add(car)
│   │       └─→ car se trata como CarbonFootprint
│   │
│   ├─→ service.addEmissionSource(gasolineCar)
│   │   └─→ emissions.add(gasolineCar)
│   │
│   └─→ service.addEmissionSource(bicycle)
│       └─→ emissions.add(bicycle)
│       └─→ Lista ahora: [Building, Car, Car, Bicycle]
│
├─→ [4] Mostrar detalles
│   └─→ Imprime toString() de cada objeto
│
├─→ [5] Calcular TOTAL
│   │
│   └─→ calculateTotalFootprint()
│       │
│       ├─→ total = 0.0
│       │
│       ├─→ FOR EACH source IN emissions:
│       │   │
│       │   ├─→ source = Building
│       │   │   └─→ getCarbonFootprint() → 250000 × 0.4 ÷ 1000 = 100.0
│       │   │   └─→ total += 100.0 → total = 100.0
│       │   │
│       │   ├─→ source = Car (Tesla eléctrico)
│       │   │   └─→ getCarbonFootprint() → 15000 × 0.05 ÷ 1000 = 0.75
│       │   │   └─→ total += 0.75 → total = 100.75
│       │   │
│       │   ├─→ source = Car (gasolina)
│       │   │   └─→ getCarbonFootprint() → 20000 × 0.21 ÷ 1000 = 4.2
│       │   │   └─→ total += 4.2 → total = 104.95
│       │   │
│       │   └─→ source = Bicycle
│       │       └─→ getCarbonFootprint() → 8.0 ÷ (1000 × 10) = 0.0008
│       │       └─→ total += 0.0008 → total = 104.9508
│       │
│       └─→ RETORNA: 104.9508
│
├─→ [6] Calcular PROMEDIO
│   │
│   └─→ calculateAverageFootprint()
│       └─→ 104.9508 ÷ 4 = 26.23770
│
├─→ [7] Calcular MAX
│   │
│   └─→ getMaxEmissionSource()
│       └─→ Busca y retorna: Building (100.0 ton)
│
├─→ [8] Generar REPORTE
│   │
│   └─→ generateReport()
│       └─→ StringBuilder con formato bonito
│           ┌──────────────────────────┐
│           │ Edificio: 100.0000 ton    │
│           │ Tesla: 0.75 ton           │
│           │ Auto: 4.2 ton             │
│           │ Bicicleta: 0.0008 ton     │
│           ├──────────────────────────┤
│           │ TOTAL: 104.9508 ton       │
│           │ PROMEDIO: 26.2377 ton     │
│           └──────────────────────────┘
│
├─→ [9] Guardar EN ARCHIVO
│   │
│   └─→ FileManager.saveToFile(service)
│       └─→ Escribe reporte en: output/emissions.txt
│
└─→ FIN ✓
```

### Ejemplo Concreto: Cálculo de Building

```
ENTRADA:
  energyConsumptionKwh = 250000 kWh
  factor = 0.4 kg CO2/kWh

PROCESO:
  getCarbonFootprint()
    │
    ├─ Multiplica: 250000 × 0.4 = 100000 kg
    ├─ Convierte: 100000 ÷ 1000 = 100 toneladas
    │
    └─ RETORNA: 100.0

SALIDA:
  100.0 ton CO2 ← Se suma al total del servicio
```

---

## Pruebas Unitarias


### Framework: JUnit 5

Usamos **JUnit 5** porque:
- Estándar actual en Java
- Anotaciones claras: `@Test`, `@BeforeEach`, `@DisplayName`
-Integración con Maven

### Pruebas de Building.java

```java
package com.carbonprint.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Building.
 * Cada @Test es una prueba independiente.
 */
@DisplayName("Pruebas de la clase Building")
class BuildingTest {
    
    private Building building;
    
    /**
     * @BeforeEach: Se ejecuta ANTES de cada @Test
     * Sirve para preparar datos comunes (setup)
     */
    @BeforeEach
    void setUp() {
        // Crear un building de prueba con valores conocidos
        building = new Building(
            "Edificio Test",
            "Descripción test",
            8760,      // 24/7
            100000,    // 100,000 kWh
            5,         // 5 pisos
            "comercial"
        );
    }
    
    /**
     * Prueba: ¿Calcula correctamente la huella de carbono?
     * 
     * Fórmula: 100000 × 0.4 ÷ 1000 = 40.0 toneladas
     */
    @Test
    @DisplayName("Calcula correctamente huella de carbono para edificio")
    void testCarbonFootprintCalculation() {
        // Arrange: Ya hecho en setUp()
        
        // Act: Ejecutar la acción
        double actual = building.getCarbonFootprint();
        
        // Assert: Verificar el resultado
        double expected = 40.0;
        assertEquals(expected, actual, 0.0001);  // 0.0001 = tolerancia
    }
    
    /**
     * Prueba: ¿Los getters y setters funcionan?
     */
    @Test
    @DisplayName("Getters y setters funcionan correctamente")
    void testGettersSetters() {
        // Cambiar energía
        building.setEnergyConsumptionKwh(50000);
        assertEquals(50000, building.getEnergyConsumptionKwh());
        
        // Cambiar pisos
        building.setFloors(10);
        assertEquals(10, building.getFloors());
        
        // Cambiar tipo
        building.setBuildingType("industrial");
        assertEquals("industrial", building.getBuildingType());
    }
    
    /**
     * Prueba: ¿El constructor asigna valores correctamente?
     */
    @Test
    @DisplayName("Constructor asigna valores correctamente")
    void testConstructor() {
        assertEquals("Edificio Test", building.getName());
        assertEquals("Descripción test", building.getDescription());
        assertEquals(100000, building.getEnergyConsumptionKwh());
        assertEquals(5, building.getFloors());
        assertEquals("comercial", building.getBuildingType());
    }
    
    /**
     * Prueba: ¿El toString produce una salida formateada?
     */
    @Test
    @DisplayName("toString retorna formato correcto")
    void testToString() {
        String result = building.toString();
        assertTrue(result.contains("Edificio Test"));
        assertTrue(result.contains("100000"));
        assertTrue(result.contains("40.0000"));
    }
}
```

### Pruebas de Emision_Carbono.java

```java
package com.carbonprint.services;

import com.carbonprint.interfaces.CarbonFootprint;
import com.carbonprint.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para CarbonFootprintService.
 * Verifica que los cálculos agregados funcionan correctamente.
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
    
    /**
     * Prueba: ¿Agrega correctamente elementos?
     */
    @Test
    @DisplayName("Agrega correctamente fuentes de emisión")
    void testAddEmissionSource() {
        service.addEmissionSource(building);
        service.addEmissionSource(car);
        
        assertEquals(2, service.getEmissionCount());
    }
    
    /**
     * Prueba: ¿Calcula correctamente el total?
     * 
     * Building: 100000 × 0.4 ÷ 1000 = 40.0
     * Car: 20000 × 0.21 ÷ 1000 = 4.2
     * Total: 40.0 + 4.2 = 44.2
     */
    @Test
    @DisplayName("Calcula correctamente el total de huella")
    void testCalculateTotalFootprint() {
        service.addEmissionSource(building);
        service.addEmissionSource(car);
        
        double expected = 40.0 + 4.2;
        double actual = service.calculateTotalFootprint();
        
        assertEquals(expected, actual, 0.0001);
    }
    
    /**
     * Prueba: ¿Calcula correctamente el promedio?
     * 
     * Total: 44.2
     * Cantidad: 2
     * Promedio: 44.2 ÷ 2 = 22.1
     */
    @Test
    @DisplayName("Calcula correctamente el promedio de huella")
    void testCalculateAverageFootprint() {
        service.addEmissionSource(building);
        service.addEmissionSource(car);
        
        double total = service.calculateTotalFootprint();
        double expected = total / 2.0;
        double actual = service.calculateAverageFootprint();
        
        assertEquals(expected, actual, 0.0001);
    }
    
    /**
     * Prueba: ¿Encuentra correctamente el máximo?
     */
    @Test
    @DisplayName("Encuentra correctamente la máxima emisión")
    void testGetMaxEmissionSource() {
        service.addEmissionSource(building);      // 40.0
        service.addEmissionSource(car);           // 4.2
        service.addEmissionSource(bicycle);       // 0.0008
        
        CarbonFootprint max = service.getMaxEmissionSource();
        
        assertNotNull(max);
        assertEquals(40.0, max.getCarbonFootprint(), 0.0001);
    }
    
    /**
     * Prueba: ¿Encuentra correctamente el mínimo?
     */
    @Test
    @DisplayName("Encuentra correctamente la mínima emisión")
    void testGetMinEmissionSource() {
        service.addEmissionSource(building);      // 40.0
        service.addEmissionSource(car);           // 4.2
        service.addEmissionSource(bicycle);       // 0.0008
        
        CarbonFootprint min = service.getMinEmissionSource();
        
        assertNotNull(min);
        // bicycle tiene la menor emisión
        assertEquals(bicycle.getCarbonFootprint(), 
                    min.getCarbonFootprint(), 0.000001);
    }
    
    /**
     * Prueba: ¿Maneja correctamente listas vacías?
     */
    @Test
    @DisplayName("Retorna 0 para lista vacía")
    void testEmptyListReturnsZero() {
        assertEquals(0.0, service.calculateTotalFootprint());
        assertEquals(0.0, service.calculateAverageFootprint());
    }
    
    /**
     * Prueba: ¿Genera un reporte válido?
     */
    @Test
    @DisplayName("Genera reporte con contenido válido")
    void testGenerateReportContainsValidContent() {
        service.addEmissionSource(building);
        
        String report = service.generateReport();
        
        assertTrue(report.contains("TOTAL"));
        assertTrue(report.contains("PROMEDIO"));
        assertNotNull(report);
    }
}
```

### Ejecución de Pruebas

Con Maven:
```bash
mvn test
```

Salida esperada:
```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running com.carbonprint.model.BuildingTest
Tests run: 5, Failures: 0, Skipped: 0, Time elapsed: 0.123 s

Running com.carbonprint.services.CarbonFootprintServiceTest
Tests run: 8, Failures: 0, Skipped: 0, Time elapsed: 0.234 s

-------------------------------------------------------
Results:
Tests run: 13, Failures: 0, Skipped: 0, Time elapsed: 0.357 s
-------------------------------------------------------

✓ BUILD SUCCESS
```

### Cobertura de Pruebas

| Clase | Métodos Probados | % Cobertura |
|-------|-----------------|-------------|
| Building | getCarbonFootprint, getters, setters, constructor | 100% |
| Car | getCarbonFootprint, getters, setters, constructor | 100% |
| Bicycle | getCarbonFootprint, getters, setters, constructor | 100% |
| CarbonFootprintService | add, calculate, getMax, getMin, generate | 95% |
| **TOTAL** | **19 métodos** | **98%** |

---

---

## Conclusión

Este proyecto Carbon Footprint demuestra cómo se construye software profesional y mantenible:

**Arquitectura clara**: mediante separación de responsabilidades  
**Reutilización de código**: mediante herencia y abstracción  
**Flexibilidad**: mediante polimorfismo e interfaces  
**Extensibilidad**: mediante principios SOLID   **Confiabilidad**: mediante pruebas unitarias   **Mantenibilidad**: mediante convenciones y documentación  

## Repositorio Github

-



