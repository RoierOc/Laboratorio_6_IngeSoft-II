# Prestamos-grupo-cualquiera

Laboratorio 6 de Ingeniería de Software II. Requiere JDK 17 o superior.

| Pieza | Herramienta |
|---|---|
| Pruebas | JUnit Jupiter |
| Dobles | Mockito |
| HTTP y JSON | Java HttpClient y Jackson |
| Servidor HTTP de prueba | HttpServer del JDK, en localhost |
| Cobertura de líneas | JaCoCo |

Desde PowerShell, en la raíz del proyecto:

```powershell
.\mvnw.cmd test
.\mvnw.cmd clean jacoco:prepare-agent test jacoco:report
```

Desde Linux o macOS, en la raíz del proyecto:

```bash
./mvnw test
./mvnw clean jacoco:prepare-agent test jacoco:report
```

El reporte HTML se genera en `target/site/jacoco/index.html`.

