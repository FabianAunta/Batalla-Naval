package interfazCliente;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class BarraMenu extends JMenuBar implements ActionListener{

	private static final long serialVersionUID = 1L;
    private static final String INICIAR_JUEGO = "IniciarJuego";
    private static final String SALIR = "salir";
    private InterfazJugador principal;
    private JMenu menuInicio;
    private JMenuItem itemIniciarJuego;
    private JMenuItem itemSalir;

    public BarraMenu( InterfazJugador ventanaPrincipal ) {
        principal = ventanaPrincipal;

        menuInicio = new JMenu( "Inicio" );
        add( menuInicio );

        itemIniciarJuego = new JMenuItem( "Iniciar Juego" );
        itemIniciarJuego.setActionCommand( INICIAR_JUEGO );
        itemIniciarJuego.addActionListener( this );
        menuInicio.add( itemIniciarJuego );

        itemSalir = new JMenuItem( "Salir" );
        itemSalir.setActionCommand( SALIR );
        itemSalir.addActionListener( this );
        menuInicio.add( itemSalir );
    }

    public void actionPerformed( ActionEvent evento ) {
        String comando = evento.getActionCommand( );

        if( SALIR.equals( comando ) ) {
            principal.dispose( );
        }else if( INICIAR_JUEGO.equals( comando ) ) {
            principal.iniciarConexion( );
        }
    }

	
}
