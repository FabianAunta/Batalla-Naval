package cliente;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

public class TableroFlota extends Tablero {
	  private static final int HORIZONTAL = 0;
	    private static final int VERTICAL = 1;
	    public static final int RESULTADO_AGUA = 0;
	    public static final int RESULTADO_IMPACTO = 1;
	    public static final int RESULTADO_HUNDIMIENTO = 2;
	    private List<Barco> barcos;

	    public TableroFlota() {
	        barcos = new LinkedList<Barco>();
	    }

	    public Barco localizarBarco( int fila, int columna ) {
	        return tablero[ fila ][ columna ].darBarco();
	    }

	    public boolean hayBarcos() {
	        return barcos.size() != 0;
	    }

	    public void inicializarBarcosTablero() {
	        barcos = new LinkedList<Barco>();
	        desplegarBarco( new Barco( "Portaviones", 5, new Color( 80, 80, 80 ) ) );
	        desplegarBarco( new Barco( "Destructor", 4, new Color( 100, 70, 70 ) ) );
	        desplegarBarco( new Barco( "Fragata", 3, new Color( 70, 70, 100 ) ) );
	        desplegarBarco( new Barco( "Fragata", 3, new Color( 70, 100, 70 ) ) );
	        desplegarBarco( new Barco( "Submarino", 2, new Color( 50, 100, 100 ) ) );
	    }
	    
	    public int atacarCasilla( int fila, int columna ) {
	        int resultado = 0;
	        Casilla casilla = tablero[ fila ][ columna ];
	        casilla.atacarCasilla();
	        if( casilla.darEstado() == Casilla.IMPACTADA ) {
	            Barco barcoAtacado = casilla.darBarco();

	            if( barcoAtacado.estaHundido() ) {
	                barcos.remove( barcoAtacado );
	                resultado = RESULTADO_HUNDIMIENTO;
	            }else {
	                resultado = RESULTADO_IMPACTO;
	            }
	        }else {
	            resultado = RESULTADO_AGUA;
	        }
	        return resultado;
	    }

	    private void desplegarBarco( Barco barco ) {
	        int orientacion = 0, fila = 0, columna = 0;

	        boolean posicionEncontrada = false;
	        while( !posicionEncontrada ) {
	        	
	            orientacion = ( Math.random( ) < 0.5 ) ? HORIZONTAL : VERTICAL;

	            fila = ( int ) ( Math.random( ) * 9 );
	            columna = ( int ) ( Math.random( ) * 9 );

	            if( fila == 9 ) {
	                fila--;
	            }
	            if( columna == 9 ) {
	                fila--;
	            }

	            posicionEncontrada = verificarBarcoCabe( fila, columna, orientacion, barco.darPuntos() );
	        }
	        colocarBarco( fila, columna, orientacion, barco );
	    }

	    private boolean verificarBarcoCabe( int fila, int columna, int orientacion, int largo ) {
	        if( orientacion == HORIZONTAL && columna + largo >= 9 ) {
	            return false;
	        }
	        if( orientacion == VERTICAL && fila + largo >= 9 ) {
	            return false;
	    	}
	        
	        boolean hayCasillaOcupada = false;
	        int[][] casillasBarco = calcularCasillasBarco( fila, columna, orientacion, largo );
	        for( int i = 0; i < largo && !hayCasillaOcupada; i++ ) {
	            int filaBarco = casillasBarco[ i ][ 0 ];
	            int columnaBarco = casillasBarco[ i ][ 1 ];

	            Casilla c = tablero[ filaBarco ][ columnaBarco ];
	            if( c.darEstado() != Casilla.VACIA ) {
	                hayCasillaOcupada = true;
	            }
	        }
	        return !hayCasillaOcupada;
	    }

	    private void colocarBarco( int fila, int columna, int orientacion, Barco barco ) {
	        int[][] casillasBarco = calcularCasillasBarco( fila, columna, orientacion, barco.darPuntos() );
	        for( int i = 0; i < barco.darPuntos(); i++ ) {
	            int filaBarco = casillasBarco[ i ][ 0 ];
	            int columnaBarco = casillasBarco[ i ][ 1 ];

	            Casilla c = tablero[ filaBarco ][ columnaBarco ];
	            c.cambiarBarco( barco );
	        }
	        barcos.add( barco );
	    }

	    private int[][] calcularCasillasBarco( int fila, int columna, int orientacion, int largo ) {
	        int deltaFilas = ( orientacion == HORIZONTAL ) ? 0 : 1;
	        int deltaColumnas = ( orientacion == HORIZONTAL ) ? 1 : 0;

	        int[][] casillasOcupadas = new int[largo][2];
	        for( int i = 0; i < largo; i++ ) {
	            casillasOcupadas[ i ][ 0 ] = fila + i * deltaFilas;
	            casillasOcupadas[ i ][ 1 ] = columna + i * deltaColumnas;
	        }
	        return casillasOcupadas;
	    }

}
