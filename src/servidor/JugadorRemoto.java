package servidor;

public class JugadorRemoto {
	
	private RegistroJugador registroJugador;
    private int puntosEncuentro;

    public JugadorRemoto( RegistroJugador registro ) {
        registroJugador = registro;
        puntosEncuentro = 0;
    }

    public RegistroJugador darRegistroJugador() {
        return registroJugador;
    }

    public int darPuntosPartida() {
        return puntosEncuentro;
    }

    public void aumentarPuntosEncuentro( int cantidad ) {
        puntosEncuentro += cantidad;
    }


}
