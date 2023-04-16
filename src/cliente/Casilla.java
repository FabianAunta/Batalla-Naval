package cliente;

public class Casilla {
	
	public static final int VACIA = 0;
    public static final int OCUPADA = 1;
    public static final int ATACADA = 2;
    public static final int IMPACTADA = 3;
    private int estadoCasilla;
    private Barco barcoCasilla;

    public Casilla( ) {
        estadoCasilla = VACIA;
        barcoCasilla = null;
    }

    public int darEstado() {
        return estadoCasilla;
    }

    public void atacarCasilla() {
        if( estadoCasilla == VACIA ) {
            estadoCasilla = ATACADA;
        }else if( estadoCasilla == OCUPADA ) {
            estadoCasilla = IMPACTADA;
            barcoCasilla.atacar();
        }
    }

    public void marcar( int nuevoEstado ) {
        estadoCasilla = nuevoEstado;
    }

    public void cambiarBarco( Barco barco ) {
        barcoCasilla = barco;
        estadoCasilla = (barcoCasilla != null ? OCUPADA : VACIA);
    }

    public Barco darBarco() {
        return barcoCasilla;
    }

}
