package cliente;

import interfazCliente.InterfazJugador;

public class ThreadConectar extends Thread{

	private Jugador jugador;
	private InterfazJugador principal;
	private String nombre;
	private String servidor;
    private int puerto;

    public ThreadConectar( Jugador juego, InterfazJugador interfaz, String nombreJugador, String direccionServidor, int puertoServidor ) {
        jugador = juego;
        principal = interfaz;
        nombre = nombreJugador;
        servidor = direccionServidor;
        puerto = puertoServidor;
    }

    public void run( ) {
          jugador.conectar( nombre, servidor, puerto );

            principal.actualizarInterfaz( );

            if( jugador.darEstadoJuego( ) == Jugador.ESPERANDO_OPONENTE ) {
                principal.esperarJugada();
            }
    }

	
}
