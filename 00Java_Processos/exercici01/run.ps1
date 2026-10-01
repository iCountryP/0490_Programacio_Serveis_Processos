# ============================================================
# Script para ejecutar el proyecto Maven desde PowerShell
# Uso normal:
#   .\run.ps1
#
# También podemos indicar otra clase Main:
#   .\run.ps1 "com.project.OtraClase"
# ============================================================


# Parámetro opcional con la clase que queremos ejecutar.
# Si no indicamos nada, utiliza "com.project.Main".
#
# CAMBIAR ESTO si algún día cambiamos nuestro package
# o queremos otra clase principal por defecto.
param (
    [string]$mainClass = "com.project.Main"
)


# Limpia compilaciones anteriores y vuelve a compilar el proyecto.
#
# clean   -> elimina la carpeta target anterior.
# compile -> compila el código de src/main/java.
mvn clean compile


# Ejecuta la clase indicada arriba.
#
# -Dexec.mainClass sobrescribe el Main configurado en el pom.xml
# para esta ejecución.
mvn exec:java "-Dexec.mainClass=$mainClass"