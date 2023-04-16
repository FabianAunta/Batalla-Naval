
package interfazServidor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import servidor.BatallaNaval;

public class InterfazBatallaNaval extends JFrame{
	
	private static final long serialVersionUID = 1L;
    private BatallaNaval servidorBatallaNaval;
    private PanelJugadores panelJugadores;
    private PanelEncuentros panelEncuentros;

    public InterfazBatallaNaval( BatallaNaval servidor ) {
        servidorBatallaNaval = servidor;
        inicializarVentana( );
    }

    private void inicializarVentana( ) {
        setLayout( new GridBagLayout( ) );
        setSize( 531, 534 );
        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        setTitle( "Servidor Batalla Naval" );
        setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints( 0, 0, 1, 1, 1, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 5, 5, 5, 5 ), 0, 0 );
        panelEncuentros = new PanelEncuentros( this );
        add( panelEncuentros, gbc );

        gbc = new GridBagConstraints( 0, 1, 1, 1, 1, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 5, 5, 5, 5 ), 0, 0 );
        panelJugadores = new PanelJugadores( this );
        add( panelJugadores, gbc );
    }

    public void actualizarEncuentros( ) {
        Collection encuentros = servidorBatallaNaval.darListaActualizadaEncuentros( );
        panelEncuentros.actualizarEncuentros( encuentros );
    }

    public void actualizarJugadores( )
    {
        try
        {
            Collection jugadores = servidorBatallaNaval.darAdministradorResultados( ).consultarRegistrosJugadores( );
            panelJugadores.actualizarJugadores( jugadores );
        }
        catch( SQLException e )
        {
            JOptionPane.showMessageDialog( this, "Hubo un error consultando la lista de jugadores:\n" + e.getMessage( ), "Error", JOptionPane.ERROR_MESSAGE );
        }
    }

    public void dispose( ) {
        super.dispose( );
        try
        {
            servidorBatallaNaval.darAdministradorResultados( ).desconectarBD( );
        }
        catch( SQLException e )
        {
            e.printStackTrace( );
        }
        System.exit( 0 );
    }

   

    public static void main( String[] args )
    {
        try
        {
            String archivoPropiedades = "./data/servidor.properties";
            BatallaNaval servidorBatallaNaval = new BatallaNaval( archivoPropiedades );

            InterfazBatallaNaval interfaz = new InterfazBatallaNaval( servidorBatallaNaval );
            interfaz.setVisible( true );

            servidorBatallaNaval.recibirConexiones( );
        }
        catch( Exception e )
        {
            e.printStackTrace( );
        }
    }

}
