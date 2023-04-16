package servidor;

import java.text.DecimalFormat;

public class RegistroJugador {
	 private String nombre;
	    private int ganados;
	    private int perdidos;

	    public RegistroJugador( String nombreP, int ganadosP, int perdidosP ) {
	        nombre = nombreP;
	        ganados = ganadosP;
	        perdidos = perdidosP;
	    }

	    public String darNombreJugador( ) {
	        return nombre;
	    }

	    public int darEncuentrosGanados( ) {
	        return ganados;
	    }

	    public int darEncuentrosPerdidos( ) {
	        return perdidos;
	    }

	    public double darEfectividad( ) {
	        if( ganados + perdidos > 0 )
	            return ( ( double )ganados * 100.0 / ( double ) ( ganados + perdidos ) );
	        else
	            return 0.0;
	    }

	    public String toString( ) {
	        DecimalFormat df = new DecimalFormat( "0.00" );
	        String efectividad = df.format( darEfectividad( ) );
	        return nombre + ": " + ganados + " ganados / " + perdidos + " perdidos (" + efectividad + "%)";
	    }

}
