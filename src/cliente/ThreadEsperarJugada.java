package cliente;

import interfazCliente.InterfazJugador;

public class ThreadEsperarJugada extends Thread{

	private Jugador jugador;
	private InterfazJugador principal;

	public ThreadEsperarJugada( Jugador juego, InterfazJugador interfaz ) {
		jugador = juego;
		principal = interfaz;
	}

	public void run( ) {
		jugador.esperarJugada( );
		principal.actualizarInterfaz( );

		if( jugador.juegoTerminado( ) )
		{
			jugador.terminarEncuentro( );
			principal.actualizarInterfaz( );
			principal.mostrarGanador( );
		}
	}


}
