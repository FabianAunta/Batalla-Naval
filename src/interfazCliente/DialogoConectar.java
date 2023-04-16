package interfazCliente;

import javax.swing.JDialog;

public class DialogoConectar extends JDialog{
	
	private static final long serialVersionUID = 1L;
    private InterfazJugador principal;
    private PanelDatosJuego panelDatos;

    public DialogoConectar( InterfazJugador ventanaPrincipal, String nombre, String direccion, int puerto ) {
        super( ventanaPrincipal, true );

        principal = ventanaPrincipal;
        panelDatos = new PanelDatosJuego( this, nombre, direccion, puerto );
        getContentPane( ).add( panelDatos );

        setTitle( "Datos del jugador" );
        setSize( 400, 200 );
    }

    public void conectar( String nombre, String direccion, int puerto )
    {
        principal.conectar( this, nombre, direccion, puerto );
    }

}
