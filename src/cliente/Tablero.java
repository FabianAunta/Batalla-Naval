package cliente;

public class Tablero {
	
	protected Casilla[][] tablero;

    public Tablero() {
        tablero = new Casilla[9][9];
        for( int i = 0; i < 9; i++ ) {
            for( int j = 0; j < 9; j++ ) {
                tablero[ i ][ j ] = new Casilla();
            }
        }
    }

    public Casilla[][] darTablero() {
        return tablero;
    }

    public Casilla darCasilla( int fila, int columna ) {
        return tablero[ fila ][ columna ];
    }

    public void marcarCasilla( int fila, int columna, int nuevoEstado ) {
        tablero[ fila ][ columna ].marcar( nuevoEstado );
    }

}
