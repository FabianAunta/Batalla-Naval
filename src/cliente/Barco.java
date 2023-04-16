package cliente;

import java.awt.Color;

public class Barco {
	
	private String tipoBarco;
    private int puntosRestantes;
    private Color colorBarco;

    public Barco( String tipo, int puntos, Color color ) {
        tipoBarco = tipo;
        puntosRestantes = puntos;
        colorBarco = color;
    }

    public String darTipoBarco( )
    {
        return tipoBarco;
    }

    public void atacar() {
        puntosRestantes--;
    }

    public int darPuntos() {
        return puntosRestantes;
    }

    public boolean estaHundido() {
        return puntosRestantes == 0;
    }

    public Color darColor() {
        return colorBarco;
    }
	
}
