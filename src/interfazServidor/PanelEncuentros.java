package interfazServidor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

public class PanelEncuentros extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;
    private static final String REFRESCAR = "Refrescar";
    private InterfazBatallaNaval principal;
    private JList listaEncuentros;
    private JButton botonRefrescar;
    
    public PanelEncuentros( InterfazBatallaNaval ventanaPrincipal ) {
        principal = ventanaPrincipal;
        inicializarPanel( );
    }

    private void inicializarPanel( ) {
        setLayout( new BorderLayout( ) );
        setSize(new Dimension(500,200));

        JScrollPane scroll = new JScrollPane( );
        scroll.setPreferredSize(new Dimension(500, 150));
        listaEncuentros = new JList( );
        scroll.getViewport( ).add( listaEncuentros );
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
        gbc.insets= new Insets(5,0,0,0);
        panelRefrescar.add( botonRefrescar, gbc );
        add(panelRefrescar, BorderLayout.SOUTH);
        setBorder( new TitledBorder( "Encuentros" ) );
    }

    public void actualizarEncuentros( Collection encuentros ) {
        listaEncuentros.setListData( encuentros.toArray() );
    }

    public void actionPerformed( ActionEvent evento ) {
        String comando = evento.getActionCommand();

        if( REFRESCAR.equals( comando ) ) {
            principal.actualizarEncuentros();
        }
    }

}
