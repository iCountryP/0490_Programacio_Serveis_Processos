package com.server;

import java.net.InetSocketAddress;
import java.util.HashMap;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONArray;
import org.json.JSONObject;

public class Server extends WebSocketServer {
    // almacenará el websocket y el nombre del jugador q envia ese webscoket
    HashMap<WebSocket, String> map = new HashMap<WebSocket, String>();
    // almacena nombre jugador + puntuacion
    HashMap<String, Integer> puntuacion = new HashMap<String, Integer>();
    // me he dado cuenta de un bug: cuando el cliente se conectaba a mitad de
    // partida no se veian
    // las casillas q ya habian puesto otros jugadores. guardamos las correctas aqui
    private JSONArray casillasCorrectas = new JSONArray();

    public Server(InetSocketAddress address) {
        super(address);
    }

    // Que pasa cuando se desconecta
    @Override
    public void onClose(WebSocket arg0, int arg1, String arg2, boolean arg3) {
        map.remove(arg0);
        prepararListaJugadores();
    }

    // Que pasa si hay un fallo
    @Override
    public void onError(WebSocket arg0, Exception arg1) {
        System.out.println("Ha habido un error: " + arg1);
    }

    // Que pasa cuando llega un mensaje
    @Override
    public void onMessage(WebSocket arg0, String arg1) {
        JSONObject jsonObject = new JSONObject(arg1);
        String tipo = jsonObject.getString("type");
        if (tipo.equals("join")) {
            String nombre = jsonObject.getString("nombre");
            map.put(arg0, nombre);
            puntuacion.put(nombre, 0);
            prepararListaJugadores();

            // bucle para enviar las casillas correctas para que se impriman
            // si un jugador entra a mitad de partida
            for (int i = 0; i < casillasCorrectas.length(); i++) {
                JSONObject casilla = casillasCorrectas.getJSONObject(i);
                arg0.send(casilla.toString());
            }

        }

        else if (tipo.equals("score")) {
            String nombre = jsonObject.getString("nombre");
            puntuacion.put(nombre, jsonObject.getInt("puntos"));
            prepararListaJugadores();
        }

        else if (tipo.equals("correct")) {
            casillasCorrectas.put(jsonObject); // para que se vean las casillas si alguein entra a mitad d partida

            broadcast(jsonObject.toString());
        }
    }

    // Que pasa cuando entra un cliente
    @Override
    public void onOpen(WebSocket arg0, ClientHandshake arg1) {
        System.out.println("Se ha conectado un cliente: " + arg0);
    }

    // Que pasa cuando arranca el servidor
    @Override
    public void onStart() {
        System.out.println("Servidor iniciado.");
    }

    // metodo para meter todos los nombres de los jugadoers en un array tipo json
    public void prepararListaJugadores() {
        JSONArray listaJugadores = new JSONArray();
        for (String nombre : map.values()) {
            JSONObject jugador = new JSONObject();
            jugador.put("nombre", nombre);
            jugador.put("puntos", puntuacion.get(nombre));
            listaJugadores.put(jugador);
        }
        JSONObject jo = new JSONObject();
        jo.put("type", "players");
        jo.put("players", listaJugadores);

        broadcast(jo.toString());
    }
}
