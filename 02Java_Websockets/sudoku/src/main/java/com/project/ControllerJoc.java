package com.project;

import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import com.clientFX.UtilsViews;
import com.clientFX.UtilsWS;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ControllerJoc implements Initializable {

    // El tablero del sudoku
    @FXML
    private GridPane gridSudoku;

    @FXML
    private Label lblPuntuacion;

    @FXML
    private Label lblJugador;

    @FXML
    private VBox listaJugadores;

    UtilsWS websocket;

    private int[][] matrizSudoku = {
            { 5, 3, 0, 0, 7, 0, 0, 0, 0 },
            { 6, 0, 0, 1, 9, 5, 0, 0, 0 },
            { 0, 9, 8, 0, 0, 0, 0, 6, 0 },
            { 8, 0, 0, 0, 6, 0, 0, 0, 3 },
            { 4, 0, 0, 8, 0, 3, 0, 0, 1 },
            { 7, 0, 0, 0, 2, 0, 0, 0, 6 },
            { 0, 6, 0, 0, 0, 0, 2, 8, 0 },
            { 0, 0, 0, 4, 1, 9, 0, 0, 5 },
            { 0, 0, 0, 0, 8, 0, 0, 7, 9 }
    };

    private int[][] matrizSolucion = {
            { 5, 3, 4, 6, 7, 8, 9, 1, 2 },
            { 6, 7, 2, 1, 9, 5, 3, 4, 8 },
            { 1, 9, 8, 3, 4, 2, 5, 6, 7 },
            { 8, 5, 9, 7, 6, 1, 4, 2, 3 },
            { 4, 2, 6, 8, 5, 3, 7, 9, 1 },
            { 7, 1, 3, 9, 2, 4, 8, 5, 6 },
            { 9, 6, 1, 5, 3, 7, 2, 8, 4 },
            { 2, 8, 7, 4, 1, 9, 6, 3, 5 },
            { 3, 4, 5, 2, 8, 6, 1, 7, 9 }
    };

    private TextField[][] matriz = new TextField[9][9];

    private int puntuacion = 0;
    private JSONArray jugadoresActuales;
    private boolean actualizandoDesdeServidor = false;
    private int huecosRestantes; // nº de huecos que hay el empezar la partida
    private int casillasResueltas; // nº de casillas que han resuelto los jugadores
    // si casillasResueltas == huecosRestantes, entonces se completa sudoku

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        huecosRestantes = 0;
        casillasResueltas = 0;
        crearTablero();

    }

    public void crearTablero() {
        gridSudoku.getColumnConstraints().clear();
        gridSudoku.getRowConstraints().clear();
        for (int columna = 0; columna < 9; columna++) {
            for (int fila = 0; fila < 9; fila++) {

                TextField casilla = new TextField();
                matriz[columna][fila] = casilla;
                casilla.setMinSize(32, 32);
                casilla.setPrefSize(32, 32);
                casilla.setMaxSize(32, 32);
                casilla.setAlignment(javafx.geometry.Pos.CENTER);
                gridSudoku.add(casilla, fila, columna);

                if (matrizSudoku[columna][fila] != 0) {
                    casilla.setText(String.valueOf(matrizSudoku[columna][fila]));
                    casilla.setEditable(false);
                }

                else {
                    huecosRestantes++;
                    int miFila = fila;
                    int miColumna = columna;
                    casilla.textProperty().addListener((observable, valorAnterior, valorNuevo) -> {
                        // Si está vacío, le dejamos y no comprobamos nada para que pueda borrar
                        // casilla.
                        if (valorNuevo.equals("")) {

                        }

                        else {
                            try {
                                if (actualizandoDesdeServidor == true) {

                                }

                                else {
                                    // Intentamos convertir el valor introducido por el usuario a numero
                                    int numero = Integer.parseInt(valorNuevo);

                                    // Si es un numero, pero es menor que 1 o mayor que 9
                                    if (numero < 1 || numero > 9) {
                                        System.out.println("El valor introducido supera el límite permitido.");
                                        casilla.setText(valorAnterior); // Dejamos el valor que estaba antes
                                    }

                                    // Si el numero es correcto, marcamos en verde y dejamos la casilla block.
                                    else if (numero == matrizSolucion[miColumna][miFila]) {
                                        System.out.println("Correcto");
                                        casilla.setStyle("-fx-background-color: lightgreen;");
                                        casilla.setEditable(false);
                                        puntuacion += 2;
                                        enviarPuntuacion();
                                        lblPuntuacion.setText("Puntos: " + puntuacion);
                                        enviarCasillas(miFila, miColumna, numero);

                                    }
                                    // Si es un numero que encaja, pero no es el de la solución, marcamos
                                    // incorrecto.
                                    else if (numero != matrizSolucion[miColumna][miFila]) {
                                        System.out.println("Incorrecto.");
                                        casilla.setStyle("-fx-background-color: lightcoral;");
                                        puntuacion -= 1;
                                        enviarPuntuacion();
                                        lblPuntuacion.setText("Puntos: " + puntuacion);

                                    }
                                }

                                // Si no es un número
                            } catch (Exception e) {
                                System.out.println("El valor introducido no era un numero.");
                                casilla.setText(valorAnterior); // Dejamos el valor que estaba antes.
                            }
                        }
                    });
                } // Fin del Else

            }
        }
    }

    public void actualizarNombre(String nombreJugador) {
        lblJugador.setText("Juga: " + nombreJugador);

    }

    // actualiza la lista del vbox de los jugadores
    public void actualizarJugadores(JSONArray jsonArray) {
        // limpia la lista de jugadores para evitar q los dupliquemos
        listaJugadores.getChildren().clear();
        jugadoresActuales = jsonArray;

        for (int i = 0; i < jsonArray.length(); i++) {
            String nombre = jsonArray.getJSONObject(i).getString("nombre");
            int puntos = jsonArray.getJSONObject(i).getInt("puntos");

            Label nombreLabel = new Label();
            nombreLabel.setText(nombre + " - " + puntos);
            listaJugadores.getChildren().add(nombreLabel);
        }
    }

    public void asignarWebsocket(UtilsWS ws) {
        websocket = ws;
    }

    public void enviarPuntuacion() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "score");
        jsonObject.put("nombre", Main.nombreJugador);
        jsonObject.put("puntos", puntuacion);

        websocket.safeSend(jsonObject.toString());
    }

    public void enviarCasillas(int fila, int columna, int numero) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "correct");
        jsonObject.put("fila", fila);
        jsonObject.put("columna", columna);
        jsonObject.put("numero", numero);

        websocket.safeSend(jsonObject.toString());
    }

    public void aplicaCasilla(int fila, int columna, int numero) {
        actualizandoDesdeServidor = true;
        // cogemos el textielfd que esta en esa posicion
        TextField casilla = matriz[columna][fila];
        casilla.setText(String.valueOf(numero));
        casilla.setStyle("-fx-background-color: lightgreen;");
        casilla.setEditable(false);
        casillasResueltas++;
        actualizandoDesdeServidor = false;

        if (casillasResueltas == huecosRestantes) {
            System.out.println("Partida terminada");
            ControllerClassificacio controllerClassificacio = (ControllerClassificacio) UtilsViews
                    .getController("Classificacio");
            controllerClassificacio.actualizarClasificacion(jugadoresActuales);
            UtilsViews.setView("Classificacio");
        }
    }

    public void reiniciarPartida() {
        puntuacion = 0;
        enviarPuntuacion();
        casillasResueltas = 0;
        huecosRestantes = 0;

        lblPuntuacion.setText("Puntos:0");
        gridSudoku.getChildren().clear();

        crearTablero();
    }
}
