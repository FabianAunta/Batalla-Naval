package interfazCliente;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PanelDatosJuego extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
    private static final String CONECTAR = "Conectar";
    private static final String CANCELAR = "Cancelar";
    private DialogoConectar dialogo;
    private JLabel etiquetaNombre;
    private JLabel etiquetaServidor;
    private JLabel etiquetaPuerto;
    private JTextField txtNombre;
    private JTextField txtServidor;
    private JTextField txtPuerto;
    private JButton botonConectar;
    private JButton botonCancelar;

    public PanelDatosJuego( DialogoConectar dialogoConectar, String nombre, String direccion, int puerto ) {
        dialogo = dialogoConectar;

        setLayout( new GridBagLayout() );

        // Etiquetas
        GridBagConstraints gbc = new GridBagConstraints( 0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 5, 5, 5, 5 ), 0, 0 );
        etiquetaNombre = new JLabel( "Nombre del Jugador:" );
        add( etiquetaNombre, gbc );

        gbc = new GridBagConstraints( 0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 5, 5, 5, 5 ), 0, 0 );
        etiquetaServidor = new JLabel( "Dirección Servidor:" );
        add( etiquetaServidor, gbc );

        gbc = new GridBagConstraints( 0, 2, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 5, 5, 5, 5 ), 0, 0 );
        etiquetaPuerto = new JLabel( "Puerto:" );
        add( etiquetaPuerto, gbc );

        // Campos de texto
        gbc = new GridBagConstraints( 1, 0, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 5, 5, 5, 5 ), 0, 0 );
        txtNombre = new JTextField( nombre );
        add( txtNombre, gbc );

        gbc = new GridBagConstraints( 1, 1, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 5, 5, 5, 5 ), 0, 0 );
        txtServidor = new JTextField( direccion );
        add( txtServidor, gbc );

        gbc = new GridBagConstraints( 1, 2, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 5, 5, 5, 5 ), 0, 0 );
        txtPuerto = new JTextField( "" + puerto );
        add( txtPuerto, gbc );

        // Botones
        gbc = new GridBagConstraints( 1, 3, 1, 1, 0.5, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 5, 5, 5, 5 ), 0, 0 );
        botonConectar = new JButton( "Conectar" );
        botonConectar.setActionCommand( CONECTAR );
        botonConectar.addActionListener( this );
        add( botonConectar, gbc );

        gbc = new GridBagConstraints( 2, 3, 1, 1, 0.5, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 5, 5, 5, 5 ), 0, 0 );
        botonCancelar = new JButton( "Cancelar" );
        botonCancelar.setActionCommand( CANCELAR );
        botonCancelar.addActionListener( this );
        add( botonCancelar, gbc );

    }

    public void actionPerformed( ActionEvent evento ) {
        String comando = evento.getActionCommand();
        if( CANCELAR.equals( comando ) ) {
            dialogo.dispose( );
        }else if( CONECTAR.equals( comando ) ) {
            try{
                String nombre = txtNombre.getText();
                String direccion = txtServidor.getText();

                String strPuerto = txtPuerto.getText();
                int puerto = Integer.parseInt( strPuerto );

                dialogo.conectar( nombre, direccion, puerto );
            }catch( NumberFormatException nfe ) {
                JOptionPane.showMessageDialog( dialogo, "El número del puerto debe ser un número" );
            }
        }

    }

}
