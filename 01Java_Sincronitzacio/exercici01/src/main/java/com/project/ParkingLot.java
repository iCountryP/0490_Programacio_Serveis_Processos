package com.project;

import java.util.concurrent.Semaphore;

public class ParkingLot {
    private final Semaphore semaphore; // Final no cambia. (no se podra hacer semaphore = new Semaphore(100))

    public ParkingLot(int capacidad) {
        this.semaphore = new Semaphore(capacidad); // Le asignamos la capacidad a semaphore. Se usa el new pq es un
                                                   // objeto.
    }

    public void entrarCoche(String coche) {
        try {
            // Comprobamos si quedan permisos disponibles.
            // Si hay 0 permisos disponibles, el parking está lleno.
            if (semaphore.availablePermits() == 0) {
                System.out.println(coche + " está esperando porque el parking está lleno.");
            }

            // Maneja si entra o no entra. Si queda permiso disponible, deja pasar. Si no,
            // bloquea
            // hasta que haya un release.
            semaphore.acquire();
            System.out.println(coche + " ha entrado al parking.");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void salirCoche(String coche) {
        semaphore.release(); // Sale y devuelve un permiso
        System.out.println(coche + " ha salido del parking.");
    }

}