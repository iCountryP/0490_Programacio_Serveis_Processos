package com.project;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        ParkingLot parking = new ParkingLot(2);
        ExecutorService executor = Executors.newFixedThreadPool(4);

        Runnable coche1 = () -> {
            parking.entrarCoche("Coche 1");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            parking.salirCoche("Coche 1");
        };

        Runnable coche2 = () -> {
            parking.entrarCoche("Coche 2");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            parking.salirCoche("Coche 2");
        };

        Runnable coche3 = () -> {
            parking.entrarCoche("Coche 3");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            parking.salirCoche("Coche 3");
        };

        Runnable coche4 = () -> {
            parking.entrarCoche("Coche 4");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            parking.salirCoche("Coche 4");
        };

        // bucle para añadir mas coches y no tener q copiar el runnable 50 veces mas y ver mas claro lo que pasa
        Runnable coches = () -> {
            String coche = "Coche";
            int idCoche = 5;
            for (int i = 0; i < 10; i++) {
                coche = "Coche " + idCoche;
                parking.entrarCoche(coche);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                parking.salirCoche(coche);
                idCoche++;
            }
        };

        executor.submit(coche1);
        executor.submit(coche2);
        executor.submit(coche3);
        executor.submit(coche4);
        executor.submit(coches);
        executor.shutdown();

    }
}
