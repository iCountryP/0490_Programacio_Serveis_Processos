package com.project;

import org.json.JSONArray;
import org.json.JSONObject;

import com.clientFX.UtilsViews;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ControllerClassificacio {

    // Vuelve a la ventana de Joc
    @FXML
    public void btnVolverAJugar() {
        ControllerJoc controllerJoc = (ControllerJoc) UtilsViews.getController("Joc");
        controllerJoc.reiniciarPartida();
        UtilsViews.setView("Joc");
    }

    @FXML
    private VBox listaClassificacio;

    public void actualizarClasificacion(JSONArray jugadores) {
        listaClassificacio.getChildren().clear();
        // metodo de la burbuja
        for (int i = 0; i < jugadores.length() - 1; i++) {
            for (int j = 0; j < jugadores.length() - 1 - i; j++) {
                JSONObject jugador1 = jugadores.getJSONObject(j); // devuelve el jgdr completo
                JSONObject jugador2 = jugadores.getJSONObject(j + 1);
                // filtramos solo por puntos para comparar los puntos
                // si esto se cumple, se cambia el orden del array, si no, no intercambiamos
                // sy se quedan igual
                if (jugador1.getInt("puntos") < jugador2.getInt("puntos")) {
                    jugadores.put(j, jugador2);
                    jugadores.put(j + 1, jugador1);
                }
            }
        }

        for (int i = 0; i < jugadores.length(); i++) {
            JSONObject jugador = jugadores.getJSONObject(i);

            String nombre = jugador.getString("nombre");
            int puntos = jugador.getInt("puntos");
            Label jugadoresEnLista = new Label();
            jugadoresEnLista.setText(nombre + " - " + puntos);
            // System.out.println(nombre + " - " + puntos);
            listaClassificacio.getChildren().add(jugadoresEnLista);
        }
    }

}
