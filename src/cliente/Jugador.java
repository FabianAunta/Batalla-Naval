package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.LinkedList;

import servidor.Encuentro;
import servidor.RegistroJugador;

public class Jugador {

	  public static final int SIN_CONECTAR = 0;
	    public static final int ESPERANDO_LOCAL = 1;
	    public static final int ESPERANDO_OPONENTE = 2;
	    public static final int ESPERANDO_RESPUESTA = 3;
	    private TableroFlota tableroFlota;
	    private Tablero tableroAtaque;
	    private int estadoJuego;
	    private boolean juegoTerminado;
	    private String nombreJugador;
	    private String servidor;
	    private int puerto;
	    private String nombreGanador;
	    private Socket canal;
	    private PrintWriter out;
	    private BufferedReader in;
	    private Collection<String> mensajesSinLeer;

	    public Jugador( ) {
	        tableroFlota = null;
	        tableroAtaque = null;
	        mensajesSinLeer = null;
	        nombreJugador = "";
	        servidor = "localhost";
	        puerto = 9999;
	        estadoJuego = SIN_CONECTAR;
	        juegoTerminado = true;
	        nombreGanador = "";
	    }

	    public String darNombreJugador( ) {
	        return nombreJugador;
	    }

	    public String darDireccionServidor() {
	        return servidor;
	    }

	    public int darPuertoServidor() {
	        return puerto;
	    }

	    public TableroFlota darTableroJuego() {
	        return tableroFlota;
	    }

	    public Tablero darTableroAtaque() {
	        return tableroAtaque;
	    }

	    public String darNombreGanador() {
	        return nombreGanador;
	    }

	    public Collection<String> darMensajesSinLeer() {
	        Collection<String> temp = mensajesSinLeer;
	        mensajesSinLeer = new LinkedList<String>();
	        return temp;
	    }

	    public int darEstadoJuego() {
	        return estadoJuego;
	    }

	    public boolean juegoTerminado() {
	        return juegoTerminado;
	    }

	    public void conectar( String nom, String dirServ, int puertoServ ) {
	        nombreJugador = nom;
	        servidor = dirServ;
	        puerto = puertoServ;
	        try{
	            // Conectar al servidor
	            canal = new Socket( dirServ, puertoServ );
	            out = new PrintWriter( canal.getOutputStream( ), true );
	            in = new BufferedReader( new InputStreamReader( canal.getInputStream( ) ) );
	            // iniciar el encuentro
	            iniciarEncuentro( );
	        }catch( UnknownHostException e ) {
	            e.printStackTrace( );
	        }catch( IOException e ) {
	            e.printStackTrace( );
	        }
	    }

	    private void iniciarEncuentro() throws IOException {
	        juegoTerminado = false;
	        nombreGanador = "";

	        tableroFlota = new TableroFlota();
	        tableroAtaque = new Tablero();
	        tableroFlota.inicializarBarcosTablero();
	        
	        mensajesSinLeer = new LinkedList<String>();

	        out.println( Encuentro.JUGADOR + ":" + nombreJugador );

	        String[] datosJugador = in.readLine( ).split( ":" );
	        RegistroJugador regJugador = new RegistroJugador( datosJugador[ 1 ], Integer.parseInt( datosJugador[ 2 ] ), Integer.parseInt( datosJugador[ 3 ] ) );

	        String[] datosOponente = in.readLine( ).split( ":" );
	        RegistroJugador regOponente = new RegistroJugador( datosOponente[ 1 ], Integer.parseInt( datosOponente[ 2 ] ), Integer.parseInt( datosOponente[ 3 ] ) );

	        mensajesSinLeer.add( "Iniciando encuentro: " + regJugador.darNombreJugador() + " vs. " + regOponente.darNombreJugador());
	        mensajesSinLeer.add( regJugador.toString());
	        mensajesSinLeer.add( regOponente.toString());

	        String turno = in.readLine();

	        if( Encuentro.PRIMER_TURNO.equals( turno ) ) {
	            estadoJuego = ESPERANDO_LOCAL;
	        }else {
	            estadoJuego = ESPERANDO_OPONENTE;
	        }
	    }

	    public void esperarJugada() {
	        try {
	            String datosJugada[] = in.readLine().split( ":" );
	            int fila = Integer.parseInt( datosJugada[ 1 ] );
	            int columna = Integer.parseInt( datosJugada[ 2 ] );

	            int resultadoAtaque = tableroFlota.atacarCasilla( fila, columna );
	            if( resultadoAtaque == TableroFlota.RESULTADO_AGUA ) {
	                mensajesSinLeer.add( "El oponente ha fallado el tiro" );
	                out.println( Encuentro.AGUA );
	            }else if( resultadoAtaque == TableroFlota.RESULTADO_IMPACTO ) {
	                Barco barcoAtacado = tableroFlota.localizarBarco( fila, columna );
	                mensajesSinLeer.add( "El oponente ha impactado el " + barcoAtacado.darTipoBarco( ));
	                out.println( Encuentro.IMPACTO + ":" + barcoAtacado.darTipoBarco( ) + ":false" );
	            }else if( resultadoAtaque == TableroFlota.RESULTADO_HUNDIMIENTO ) {
	                Barco barcoAtacado = tableroFlota.localizarBarco( fila, columna );

	                if( !tableroFlota.hayBarcos( ) ) {
	                    juegoTerminado = true;
	                    mensajesSinLeer.add( "¡Nos han derrotado!" );
	                    out.println( Encuentro.FIN_JUEGO );
	                }else {
	                    mensajesSinLeer.add(barcoAtacado.darTipoBarco( ) + " ha caido" );
	                    out.println( Encuentro.IMPACTO + ":" + barcoAtacado.darTipoBarco( ) + ":true" );
	                }
	            }
	            estadoJuego = ESPERANDO_LOCAL;
	        }
	        catch( IOException e ) {
	        	System.out.println(e);
	        }
	    }

	    public void enviarJugada( int fila, int columna ) {
	        try {
	            out.println( Encuentro.JUGADA + ":" + fila + ":" + columna );
	            estadoJuego = ESPERANDO_RESPUESTA;

	            String respuesta = in.readLine( );

	            if( respuesta.startsWith( Encuentro.AGUA ) ) {
	                tableroAtaque.marcarCasilla( fila, columna, Casilla.ATACADA );
	                mensajesSinLeer.add( "Tiro fallido" );
	            }else if( respuesta.startsWith( Encuentro.IMPACTO ) ) {
	                tableroAtaque.marcarCasilla( fila, columna, Casilla.IMPACTADA );
	                String datosDisparo[] = respuesta.split( ":" );

	                if( datosDisparo[ 2 ].equals( "true" ) ) {
	                    mensajesSinLeer.add( "¡Excelente! El barco " + datosDisparo[ 1 ] + " fue hundido!" );
	            	}else {
	                    mensajesSinLeer.add( "¡Buen disparo! Le dimos al " + datosDisparo[ 1 ] );
	            	}
	            }else if( respuesta.startsWith( Encuentro.FIN_JUEGO ) ) {
	                // FIN_JUEGO
	                mensajesSinLeer.add( "¡Victoria! El último barco enemigo fue hundido!" );
	                juegoTerminado = true;
	            }
	            estadoJuego = ESPERANDO_OPONENTE;
	        }
	        catch( IOException e ) {
	        	System.out.println(e);
	        }
	    }

	    public void terminarEncuentro() {
	        try {
	            String mensajeFin = in.readLine();
	            nombreGanador = mensajeFin.split( ":" )[ 1 ];
	            estadoJuego = SIN_CONECTAR;

	            // Cerrar la conexión al servidor
	            out.close();
	            in.close();
	            canal.close();

	            out = null;
	            in = null;
	            canal = null;
	        }catch( IOException e ) {
	        	System.out.println(e);
	        }
	    }

	
}
