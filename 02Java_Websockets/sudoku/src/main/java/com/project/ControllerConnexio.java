package com.project;

import com.clientFX.UtilsViews;
import com.clientFX.UtilsWS;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import org.json.JSONArray;
import org.json.JSONObject;

public class ControllerConnexio {
    @FXML
    private TextField txtServidor;

    @FXML
    private TextField txtNombre;

    // Te lleva a la ventana de Joc
    @FXML
    public void botonConectar() {

        if (txtServidor.getText().equals("")) {
            System.out.println("Este campo no puede estar vacío.");
        } else {
            Main.nombreJugador = txtNombre.getText();
            ControllerJoc controllerJoc = (ControllerJoc) UtilsViews.getController("Joc");
            UtilsWS websocket = UtilsWS.getSharedInstance(txtServidor.getText());
            controllerJoc.asignarWebsocket(websocket);

            websocket.onOpen(mensaje -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", "join");
                jsonObject.put("nombre", Main.nombreJugador);
                websocket.safeSend(jsonObject.toString());

                // para q no salte el error de javafx de pasarle
                // modificar interfaz al websocket
                Platform.runLater(() -> {
                    controllerJoc.actualizarNombre(Main.nombreJugador);
                    UtilsViews.setView("Joc");
                });

            });

            websocket.onError(mensaje -> {
                System.out.println(mensaje);
            });

            websocket.onMessage(mensaje -> {
                JSONObject jo = new JSONObject(mensaje);
                // {"type":"players","players":["Ivan","Marta"]}
                // tenemos algo parecido a esto. cuando leemos type leeremos "players"
                // luego "players" cuando es clave es una array. usamos json array
                String tipo = jo.getString("type"); // leemos el type de json
                if (tipo.equals("players")) {
                    JSONArray jugadores = jo.getJSONArray("players");
                    Platform.runLater(() -> {
                        controllerJoc.actualizarJugadores(jugadores);
                    });
                }

                else if (tipo.equals("correct")) {
                    int fila = jo.getInt("fila");
                    int columna = jo.getInt("columna");
                    int numero = jo.getInt("numero");
                    Platform.runLater(() -> {
                        controllerJoc.aplicaCasilla(fila, columna, numero);
                    });
                }

                // tests
                /*
                 * for (int i = 0; i < jugadores.length(); i++) {
                 * String nombreJugadores = jugadores.getString(i);
                 * System.out.println(nombreJugadores);
                 * }
                 */
            });
        }

    }
}
