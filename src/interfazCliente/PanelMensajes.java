package interfazCliente;

import java.awt.BorderLayout;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class PanelMensajes extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextArea txtArea;
 
	public PanelMensajes( ) {
        setLayout( new BorderLayout( ) );

        JScrollPane scroll = new JScrollPane();
        scroll.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );

        txtArea = new JTextArea( 6, 10 );
        txtArea.setWrapStyleWord( true );
        txtArea.setLineWrap( true );
        txtArea.setEditable( false );

        scroll.setOpaque( false );
        scroll.getViewport( ).add( txtArea );
        add( scroll );

    }

    public void agregarMensajes( Collection<String> mensajes ) {
        Iterator<String> iter = mensajes.iterator();
        while( iter.hasNext() ) {
            String mensaje = ( String )iter.next();
            txtArea.append( mensaje + "\n" );
            txtArea.setCaretPosition( txtArea.getText().length() );
        }
    }

	
}
