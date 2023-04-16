package interfazCliente;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import cliente.Casilla;
import cliente.Tablero;
import cliente.TableroFlota;

public class PanelTableros extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;
    private static final Color COLOR_VACIA = new Color( 0, 0, 0, 0 );
    private static final Color COLOR_ATACADA = new Color( 253, 191, 70 );
    private static final Color COLOR_IMPACTADA = new Color( 255, 0, 0 );
    private InterfazJugador principal;
    private JButton[][] botonesJugador;
    private JButton[][] botonesOponente;

    public PanelTableros( InterfazJugador ventanaPrincipal ) {
        principal = ventanaPrincipal;
        inicializarPanel( );
    }

    private void inicializarPanel() {
        botonesJugador = new JButton[9][9];
        botonesOponente = new JButton[9][9];

        JPanel panelJugador = new JPanel( new GridLayout( 9, 9 ) );
        JPanel panelOponente = new JPanel( new GridLayout( 9, 9 ) );

        panelJugador.setPreferredSize( new Dimension( 35 * 9, 35 * 9 ) );
        panelOponente.setPreferredSize( new Dimension( 35 * 9, 35 * 9 ) );

        panelJugador.setOpaque( false );
        panelOponente.setOpaque( false );

        for( int i = 0; i < 9; i++ ) {
            for( int j = 0; j < 9; j++ ) {
                JButton botonJugador = new JButton( );
                botonJugador.setEnabled( false );
                botonJugador.setOpaque( false );
                // botonJugador.setBackground( COLOR_VACIA );
                botonJugador.setIcon( new ImageIcon( "./data/vacia.png" ) );
                botonJugador.setBorder( new LineBorder( Color.BLACK ) );
                botonesJugador[ i ][ j ] = botonJugador;
                panelJugador.add( botonesJugador[ i ][ j ] );

                JButton botonOponente = new JButton( );
                botonOponente.setBackground( COLOR_VACIA );
                botonOponente.setBorder( new LineBorder( Color.BLACK ) );
                botonOponente.setIcon( new ImageIcon( "./data/vacia.png" ) );
                botonOponente.setOpaque( false );
                botonOponente.setFocusPainted( false );
                botonOponente.setActionCommand( i + ":" + j );
                botonOponente.addActionListener( this );
                botonesOponente[ i ][ j ] = botonOponente;
                panelOponente.add( botonesOponente[ i ][ j ] );
            }
        }

        setLayout( new GridBagLayout() );

        GridBagConstraints gbc = new GridBagConstraints( 0, 0, 1, 1, 1, 0, GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets( 15, 15, 5, 15 ), 10, 10 );
        add( panelJugador, gbc );

        gbc.gridx = 1;
        add( panelOponente, gbc );
    }

    public void reinicializarTablero() {
        for( int i = 0; i < 9; i++ ) {
            for( int j = 0; j < 9; j++ ) {
                JButton botonJugador = botonesJugador[ i ][ j ];
                botonJugador.setOpaque( false );
                botonJugador.setIcon( new ImageIcon( "./data/vacia.png" ) );
                botonJugador.setBorder( new LineBorder( Color.BLACK ) );

                JButton botonOponente = botonesOponente[ i ][ j ];
                botonOponente.setEnabled( true );
                botonOponente.setBackground( COLOR_ATACADA );
                botonOponente.setIcon( new ImageIcon( "./data/vacia.png" ) );
                botonOponente.setOpaque( false );
            }
        }
    }

    public void actualizarTableros( TableroFlota tableroJugador, Tablero tableroOponente ) {
        for( int i = 0; i < 9; i++ ) {
            for( int j = 0; j < 9; j++ ) {
                JButton botonJugador = botonesJugador[ i ][ j ];
                if( tableroJugador.darCasilla( i, j ).darEstado( ) == Casilla.IMPACTADA ) {
                    botonJugador.setOpaque( true );
                    botonJugador.setBackground( COLOR_IMPACTADA );
                }else if( tableroJugador.darCasilla( i, j ).darEstado( ) == Casilla.OCUPADA ) {
                    botonJugador.setOpaque( true );
                    botonJugador.setBackground( tableroJugador.darCasilla( i, j ).darBarco( ).darColor( ) );
                }else if( tableroJugador.darCasilla( i, j ).darEstado( ) == Casilla.ATACADA ) {
                    botonJugador.setOpaque( true );
                    botonJugador.setBackground( COLOR_ATACADA );
                }

                JButton botonOponente = botonesOponente[ i ][ j ];
                if( tableroOponente.darCasilla( i, j ).darEstado() == Casilla.IMPACTADA ){
                    botonOponente.setIcon( null );
                    botonOponente.setBackground( COLOR_IMPACTADA );
                    botonOponente.setOpaque( true );
                    botonOponente.setEnabled( false );
                }else if( tableroOponente.darCasilla( i, j ).darEstado() == Casilla.ATACADA ) {
                    botonOponente.setIcon( null );
                    botonOponente.setBackground( COLOR_ATACADA );
                    botonOponente.setOpaque( true );
                    botonOponente.setEnabled( false );
                }
            }
        }
    }

    public void actionPerformed( ActionEvent evento ) {
        String comando = evento.getActionCommand();
        String[] coordenadas = comando.split( ":" );
        int fila = Integer.parseInt( coordenadas[ 0 ] );
        int columna = Integer.parseInt( coordenadas[ 1 ] );

        principal.jugar( fila, columna );
    }

	
}
