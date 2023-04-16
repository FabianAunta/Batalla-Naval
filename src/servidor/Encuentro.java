package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

public class Encuentro extends Thread {
	
    public static final String JUGADOR = "JUGADOR";
    public static final String INFO_JUGADOR = "INFO";
    public static final String PRIMER_TURNO = "1";
    public static final String SEGUNDO_TURNO = "2";
    public static final String JUGADA = "JUGADA";
    public static final String AGUA = "AGUA";
    public static final String IMPACTO = "IMPACTO";
    public static final String FIN_JUEGO = "FIN_JUEGO";
    public static final String GANADOR = "GANADOR";
    private Socket socketJugador1;
    private PrintWriter out1;
    private BufferedReader in1;
    private Socket socketJugador2;
    private PrintWriter out2;
    private BufferedReader in2;
    protected JugadorRemoto jugador1;
    protected JugadorRemoto jugador2;
    private boolean finJuego;
    private AdministradorResultados adminResultados;

    public Encuentro( Socket canal1, Socket canal2, AdministradorResultados administrador ) throws IOException {
        adminResultados = administrador;

        out1 = new PrintWriter( canal1.getOutputStream(), true );
        in1 = new BufferedReader( new InputStreamReader( canal1.getInputStream() ) );
        socketJugador1 = canal1;

        out2 = new PrintWriter( canal2.getOutputStream(), true );
        in2 = new BufferedReader( new InputStreamReader( canal2.getInputStream() ) );
        socketJugador2 = canal2;

        finJuego = false;
    }

    public Socket darSocketJugador1() {
        return socketJugador1;
    }

    public Socket darSocketJugador2() {
        return socketJugador2;
    }

    public AdministradorResultados darAdministradorResultados() {
        return adminResultados;
    }

    public boolean encuentroTerminado() {
        return finJuego;
    }

    private RegistroJugador consultarJugador( String info ) {
        if( info.startsWith( JUGADOR ) ) {
            String nombre = info.split( ":" )[ 1 ];
            try{
                RegistroJugador reg1 = adminResultados.consultarRegistroJugador( nombre );
                return reg1;
            }catch( SQLException e ) {
            	e.printStackTrace();
            }
        }
		return null;
    }

    private void enviarInformacion( PrintWriter out, RegistroJugador reg ) {
        String cadena = INFO_JUGADOR + ":" + reg.darNombreJugador() + ":" + reg.darEncuentrosGanados() + ":" + reg.darEncuentrosPerdidos();
        out.println( cadena );
    }

    public void run() {
        try{
            iniciarEncuentro();

            // Iniciar el juego
            int atacante = 1;

            while( !finJuego ) {
                procesarJugada( atacante );

                if( finJuego ) {
                    terminarEncuentro( );
                }else {
                    atacante = ( atacante == 1 ) ? 2 : 1;
                }
            }
        }catch( Exception e ) {
            finJuego = true;

            try{
                in1.close();
                out1.close();
                socketJugador1.close();
            }catch( IOException e2 ) {
                e2.printStackTrace();
            }

            try{
                in2.close();
                out2.close();
                socketJugador2.close();
            }catch( IOException e2 ) {
                e2.printStackTrace();
            }
        }
    }

    protected void iniciarEncuentro() throws IOException {
        // Leer la información sobre los jugadores
        String info1 = in1.readLine();
        RegistroJugador reg1 = consultarJugador( info1 );
        jugador1 = new JugadorRemoto( reg1 );

        String info2 = in2.readLine( );
        RegistroJugador reg2 = consultarJugador( info2 );
        jugador2 = new JugadorRemoto( reg2 );

        // Enviar a cada jugador la información sobre su registro y el del oponente (en ese orden)
        enviarInformacion( out1, jugador1.darRegistroJugador() );
        enviarInformacion( out1, jugador2.darRegistroJugador() );

        enviarInformacion( out2, jugador2.darRegistroJugador() );
        enviarInformacion( out2, jugador1.darRegistroJugador() );

        // Enviar a cada jugador la información sobre en qué orden deben jugar: siempre empieza el jugador 1
        out1.println( PRIMER_TURNO );
        out2.println( SEGUNDO_TURNO );
    }

    private void terminarEncuentro() throws IOException {
        // Actualizar el registro de los jugadores
        RegistroJugador ganador = null;
        RegistroJugador perdedor = null;
        if( jugador1.darPuntosPartida() > jugador2.darPuntosPartida() ) {
            ganador = jugador1.darRegistroJugador();
            perdedor = jugador2.darRegistroJugador();
        }else {
            ganador = jugador2.darRegistroJugador();
            perdedor = jugador1.darRegistroJugador();
        }
        try{
            adminResultados.registrarVictoria( ganador.darNombreJugador() );
            adminResultados.registrarDerrota( perdedor.darNombreJugador() );
        }catch( SQLException e ) {
        	e.printStackTrace();
        }
        // Enviar un mensaje indicando el fin del juego y el ganador
        String cadenaGanador = GANADOR + ":" + ganador.darNombreJugador();
        out1.println( cadenaGanador );
        out2.println( cadenaGanador );

        // Cerrar los canales de los jugadores
        in1.close();
        out1.close();
        out2.close();
        in2.close();
        socketJugador1.close();
        socketJugador2.close();
    }

    private void procesarJugada( int atacante ) throws IOException{
        PrintWriter atacanteOut = ( atacante == 1 ) ? out1 : out2;
        PrintWriter atacadoOut = ( atacante == 1 ) ? out2 : out1;

        BufferedReader atacanteIn = ( atacante == 1 ) ? in1 : in2;
        BufferedReader atacadoIn = ( atacante == 1 ) ? in2 : in1;

        // Leer la jugada del atacante que indica donde se va a hacer el ataque
        String lineaAtaque = atacanteIn.readLine();

        if(lineaAtaque != null) {
            // Reenviar el ataque al jugador atacado
            atacadoOut.println( lineaAtaque );

            // Leer la información sobre el resultado del ataque que envía el jugador atacado
            String lineaResultado = atacadoIn.readLine( );

            // Revisar el resultado para saber si el encuentro termina y actualizar los puntajes
            if( lineaResultado.startsWith( IMPACTO ) ) {
                JugadorRemoto jugadorAtacante = ( atacante == 1 ) ? jugador1 : jugador2;
                jugadorAtacante.aumentarPuntosEncuentro( 1 );
            }else if( lineaResultado.startsWith( FIN_JUEGO ) ) {
                JugadorRemoto jugadorAtacante = ( atacante == 1 ) ? jugador1 : jugador2;
                jugadorAtacante.aumentarPuntosEncuentro( 20 );

                finJuego = true;
            }

            // Enviar el resultado del disparo al jugador atacante
            atacanteOut.println( lineaResultado );
        }
    }

    public String toString() {
        RegistroJugador j1 = jugador1.darRegistroJugador();
        RegistroJugador j2 = jugador2.darRegistroJugador();

        String cadena = j1.darNombreJugador() + "( " + jugador1.darPuntosPartida() + " ) - " + j2.darNombreJugador() + "( " + jugador2.darPuntosPartida() + " )";
        return cadena;
    }

}
