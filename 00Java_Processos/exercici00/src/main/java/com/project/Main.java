package com.project;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) {
        ConcurrentHashMap<String, Double> datos = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // ** No he encontrado la manera de conseguir que estas tareas sean paralelas entre ellas.
        // La tarea2 depende de que la primera termine su flujo de trabajo para poder modificarlo, y la 3ª depende de que las dos primeras
        // hayan terminado para garantizar un resultado correcto.

        // Si hubiese una forma diferente o más correcta a la hora de hacerlo, por favor, indicarla en los comentarios! Gracias!
        
        Runnable tarea1 = new Runnable() {
            @Override
            public void run() {
                datos.put("saldo", 1500.0);
            }
        };

        Runnable tarea2 = new Runnable() {
            @Override
            public void run() {
                Double saldo = datos.get("saldo");
                Double comision = 50.0;
                saldo = saldo - comision;
                datos.put("saldo", saldo);

            }
        };

        Callable<Double> tarea3 = new Callable<Double>() {
            @Override
            public Double call() throws Exception {
                return datos.get("saldo");
            } 
        };

        // Enviamos la tarea1 al ExecutorService
        Future<?> futuroTarea1 = executor.submit(tarea1);

        // Usamos .get para esperar a que finalice la tarea antes de seguir. Capturamos posibles excepciones.
        try {
            futuroTarea1.get();
        } 
        
        catch (InterruptedException e) {
            System.out.println("La ejecución se ha interrumpido.");
        }
        
        catch(ExecutionException e) {
            System.out.println("La ejecución ha fallado.");
        }

        // Enviamos la tarea2 al ExecutorService
        Future<?> futuroTarea2 = executor.submit(tarea2);

        // Usamos .get para esperar a que finalice la tarea antes de seguir. Capturamos posibles excepciones.
        try {
            futuroTarea2.get();
        } 
        
        catch (InterruptedException e) {
            System.out.println("La ejecución se ha interrumpido.");
        }
        
        catch(ExecutionException e) {
            System.out.println("La ejecución ha fallado.");
        }

        // Enviamos la tarea3 al ExecutorService
        Future<Double> futuroTarea3 = executor.submit(tarea3);
        
        try {
            Double resultado = futuroTarea3.get();
            System.out.println("El saldo es: " + resultado);
        }
        
        catch (InterruptedException e) {
            System.out.println("La ejecución se ha interrumpido.");
        }
        
        catch(ExecutionException e) {
            System.out.println("La ejecución ha fallado.");
        }
        executor.shutdown();
    }
}