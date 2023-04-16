package cliente;

import interfazCliente.InterfazJugador;

public class ThreadEnviarJugada extends Thread {

	private Jugador jugador;
	private InterfazJugador principal;
	private int fila;
	private int columna;

	public ThreadEnviarJugada( Jugador juego, InterfazJugador interfaz, int filaJugada, int columnaJugada ) {

		jugador = juego;
		principal = interfaz;
		fila = filaJugada;
		columna = columnaJugada;
	}

	public void run() {
		jugador.enviarJugada( fila, columna );
		principal.actualizarInterfaz();

		if( jugador.juegoTerminado() ) {
			jugador.terminarEncuentro();
			principal.actualizarInterfaz();
			principal.mostrarGanador();
		}
		else
		{
			principal.esperarJugada();
		}


	}

}
