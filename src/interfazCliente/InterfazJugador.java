package interfazCliente;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cliente.Jugador;
import cliente.ThreadConectar;
import cliente.ThreadEnviarJugada;
import cliente.ThreadEsperarJugada;

public class InterfazJugador extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private Jugador batallaNaval;
    private BarraMenu barraMenu;
    private PanelMensajes panelMensajes;
    private PanelTableros panelTableros;

    public InterfazJugador() {
        batallaNaval = new Jugador();
        inicializarVentana();
    }

    private void inicializarVentana( ) {
        getContentPane( ).setLayout( new BorderLayout( ) );
        setSize( 819, 612 );
        setResizable( false );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setTitle( "Batalla Naval: esperando conexión...." );

        barraMenu = new BarraMenu( this );
        setJMenuBar( barraMenu );

        panelTableros = new PanelTableros( this );
        getContentPane( ).add( panelTableros, BorderLayout.CENTER );

        panelMensajes = new PanelMensajes( );
        getContentPane( ).add( panelMensajes, BorderLayout.SOUTH );
    }

    public void iniciarConexion( ) {
        DialogoConectar dialogo = new DialogoConectar( this, batallaNaval.darNombreJugador( ), batallaNaval.darDireccionServidor( ), batallaNaval.darPuertoServidor( ) );
        dialogo.setVisible( true );
    }

    public void conectar( DialogoConectar dialogo, String nombre, String direccion, int puerto ) {
        dialogo.dispose( );
        Thread t = new ThreadConectar( batallaNaval, this, nombre, direccion, puerto );
        t.start( );
    }

    public void esperarJugada( ) {
        if( batallaNaval.darEstadoJuego( ) == Jugador.ESPERANDO_OPONENTE )
        {
            Thread t = new ThreadEsperarJugada( batallaNaval, this );
            t.start( );
        }
    }

    public void jugar( int fila, int columna ) {
        if( batallaNaval.darEstadoJuego( ) == Jugador.ESPERANDO_LOCAL )
        {
            Thread t = new ThreadEnviarJugada( batallaNaval, this, fila, columna );
            t.start( );
        }
    }

    public void mostrarGanador( ) {
        JOptionPane.showMessageDialog( this, batallaNaval.darNombreGanador( ).toUpperCase( ) + "ha ganado!", "Fin del Juego", JOptionPane.INFORMATION_MESSAGE );
        panelTableros.reinicializarTablero( );
        validate( );
    }
    
    public void actualizarInterfaz( ) {
        panelMensajes.agregarMensajes( batallaNaval.darMensajesSinLeer( ) );
        panelTableros.actualizarTableros( batallaNaval.darTableroJuego( ), batallaNaval.darTableroAtaque( ) );

        if( batallaNaval.darEstadoJuego( ) == Jugador.SIN_CONECTAR )
            setTitle( "Batalla Naval: sin conexión" );
        else if( batallaNaval.darEstadoJuego( ) == Jugador.ESPERANDO_LOCAL )
            setTitle( "Batalla Naval: jugando" );
        else if( batallaNaval.darEstadoJuego( ) == Jugador.ESPERANDO_OPONENTE )
            setTitle( "Batalla Naval: esperando jugada" );
    }

    public static void main( String[] args ) {

        InterfazJugador interfaz = new InterfazJugador( );
        interfaz.setVisible( true );
    }


}
