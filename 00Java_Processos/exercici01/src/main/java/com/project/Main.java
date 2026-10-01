package com.project;

import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) {

        CompletableFuture<Integer> numeroFuturo1; // Declaramos variable pero no asignamos nada. Su valor será
        // un entero que obtendremos de una operación asíncrona + adelante.

        // Asignamos al CompletableFuture una tarea asíncrona que devolverá un valor
        numeroFuturo1 = CompletableFuture.supplyAsync(
                () -> {
                    System.out.println("Validando datos...");
                    return 100;
                });

        // Multiplicamos el número que nos llega de numeroFuturo1 * 5
        // y devolvemos el nuevo resultado
        CompletableFuture<Integer> numeroFuturo2 = numeroFuturo1.thenApply(resultado -> {
            System.out.println("Multiplicando x5 al numeroFuturo1");
            return resultado * 5;
        });

        // Aceptamos el número y sabemos que no va a haber mas cambios y lo mostramos
        CompletableFuture<Void> numeroFinal = numeroFuturo2.thenAccept(resultado -> {
            System.out.println("El número final es: " + resultado);
        });

        numeroFinal.join(); // Espera a que acaben todas las operaciones asíncronas
    }
}