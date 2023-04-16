package interfazServidor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

public class PanelJugadores extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
    private static final String REFRESCAR = "Refrescar";
    private InterfazBatallaNaval principal;
    private JList listaJugadores;
    private JButton botonRefrescar;

    public PanelJugadores( InterfazBatallaNaval ventanaPrincipal ) {
        principal = ventanaPrincipal;
        inicializarPanel();
    }
    
    private void inicializarPanel() {
        setLayout( new BorderLayout() );

        JScrollPane scroll = new JScrollPane( );
        scroll.setPreferredSize(new Dimension(500, 150));
        listaJugadores = new JList();
        scroll.getViewport( ).add( listaJugadores );
        add(scroll, BorderLayout.CENTER);

        JPanel panelRefrescar= new JPanel();
        panelRefrescar.setLayout(new GridBagLayout());
        botonRefrescar = new JButton( "Refrescar" );
        botonRefrescar.addActionListener( this );
        botonRefrescar.setActionCommand( REFRESCAR );
        GridBagConstraints gbc= new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.fill= GridBagConstraints.BOTH;
        panelRefrescar.add( botonRefrescar, gbc );

        add(panelRefrescar, BorderLayout.SOUTH);
        setBorder( new TitledBorder( "Registro Jugadores" ) );
    }
    
    public void actualizarJugadores( Collection jugadores ) {
        listaJugadores.setListData( jugadores.toArray() );
    }

    public void actionPerformed( ActionEvent evento ) {
        String comando = evento.getActionCommand( );

        if( REFRESCAR.equals( comando ) ) {
            principal.actualizarJugadores();
        }
    }


}
