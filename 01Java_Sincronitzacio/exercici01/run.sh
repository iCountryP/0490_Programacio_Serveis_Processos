#!/bin/bash

# ============================================================
# Script para ejecutar el proyecto Maven desde Linux/macOS
# Uso normal:
#   ./run.sh
#
# También podemos indicar otra clase Main:
#   ./run.sh "com.project.OtraClase"
# ============================================================


# Clase que queremos ejecutar.
#
# $1 = primer parámetro recibido por el script.
# Si no recibimos ninguno, usa "com.project.Main".
#
# CAMBIAR ESTO si algún día cambiamos nuestro package
# o queremos otra clase principal por defecto.
MAIN_CLASS=${1:-"com.project.Main"}


# Limpia compilaciones anteriores y vuelve a compilar.
#
# clean   -> elimina la carpeta target anterior.
# compile -> compila src/main/java.
mvn clean compile


# Ejecuta la clase indicada arriba.
#
# -Dexec.mainClass permite indicar qué Main queremos ejecutar.
mvn exec:java -Dexec.mainClass="$MAIN_CLASS"