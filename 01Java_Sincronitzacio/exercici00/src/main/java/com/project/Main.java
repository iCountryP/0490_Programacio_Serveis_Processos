package com.project;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    public static void main(String[] args) {

        // Creamos los contenedores para almacenar los datos (IMPORTANTE QUE ESTÉN ANTES
        // DE CREAR LA BARRERA, SI NO NO PODREMOS ACCEDER A ELLOS)
        AtomicReference<Double> resultadoSuma = new AtomicReference<>(0.0);
        AtomicReference<Double> resultadoMedia = new AtomicReference<>(0.0);
        AtomicReference<Double> resultadoDesviacion = new AtomicReference<>(0.0);

        // Creamos una barrera para 3 hilos
        CyclicBarrier barrier = new CyclicBarrier(3, new Runnable() {
            @Override
            public void run() {
                System.out.println("Se han hecho todos los cálculos. Mostrando los resultados...");

                System.out.println("Suma: " + resultadoSuma.get());
                System.out.println("Media: " + resultadoMedia.get());
                System.out.println("Desviación estándar: " + resultadoDesviacion.get());
            }

        });

        // Creamos los hilos
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // DATOS
        double[] datos = { 10, 20, 30, 40, 50 };

        // Tarea 1 : SUMA
        Runnable suma = () -> {
            try {
                System.out.println("Procesando la suma...");
                double totalSumas = 0;
                for (int i = 0; i < datos.length; i++) {
                    totalSumas += datos[i];
                }
                resultadoSuma.set(totalSumas);
                Thread.sleep(1000);
                System.out.println("Suma completada.");
                barrier.await(); // Este comando hace que se espere a que el resto acabe.
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        };

        // Tarea 2: MEDIA
        Runnable media = () -> {
            try {
                System.out.println("Procesando la media...");
                double totalMedia = 0;
                for (int i = 0; i < datos.length; i++) {
                    totalMedia += datos[i];
                }
                totalMedia = totalMedia / datos.length;
                resultadoMedia.set(totalMedia);
                Thread.sleep(4000);
                System.out.println("Media completada.");
                barrier.await(); // Este comando hace que se espere a que el resto acabe.
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        };

        // Tarea 3 : DESVIACION
        // FORMULA (chatGPT):
        /*
         * 1. Necesito conocer/calcular la media.
         * 2. Creo una variable acumuladora = 0.
         * 3. Recorro datos[].
         * 4. Para cada dato calculo: datos[i] - media.
         * 5. Multiplico esa diferencia por sí misma.
         * 6. La voy sumando al acumulador.
         * 7. Divido el acumulador entre datos.length.
         * 8. Hago Math.sqrt().
         * 9. Guardo el resultado en resultadoDesviacion.
         */
        Runnable desviacion = () -> {
            try {
                System.out.println("Procesando la desviación estándar...");
                // 1.Necesito conocer/calcular la media.
                double totalMedia = 0;
                for (int i = 0; i < datos.length; i++) {
                    totalMedia += datos[i];
                }
                totalMedia = totalMedia / datos.length;
                // 2. Creo una variable acumuladora = 0.
                double acumular = 0;
                // 3. Recorro datos[].
                for (int i = 0; i < datos.length; i++) {
                    // 4. Para cada dato calculo: datos[i] - media. / 5. Multiplico esa diferencia
                    // por sí misma. / 6. La voy sumando al acumulador.
                    double diferencia = datos[i] - totalMedia;
                    diferencia = diferencia * diferencia;
                    acumular += diferencia;
                }
                // 7. Divido el acumulador entre datos.length. / 8. Hago Math.sqrt().
                resultadoDesviacion.set(Math.sqrt(acumular / datos.length));
                Thread.sleep(7000);
                System.out.println("Desviación completada.");
                barrier.await(); // Este comando hace que se espere a que el resto acabe.
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        };

        executor.submit(suma);
        executor.submit(media);
        executor.submit(desviacion);

        executor.shutdown();
    }
}