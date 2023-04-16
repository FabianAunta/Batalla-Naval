package servidor;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

public class BatallaNaval {

	 private ServerSocket receptor;
	    private Properties config;
	    private AdministradorResultados adminResultados;
	    private Socket socketJugadorEnEspera;
	    protected Collection encuentros;

	    public BatallaNaval( String archivo ) throws SQLException, Exception {
	        encuentros = new Vector( );

	        cargarConfiguracion( archivo );

	        adminResultados = new AdministradorResultados( config );
	        adminResultados.conectarABD( );
	        adminResultados.inicializarTabla( );
	    }

	    private void cargarConfiguracion( String archivo ) throws Exception {
	        FileInputStream fis = new FileInputStream( archivo );
	        config = new Properties();
	        config.load( fis );
	        fis.close();
	    }

	    public AdministradorResultados darAdministradorResultados( ) {
	        return adminResultados;
	    }

	    public Collection darListaActualizadaEncuentros() {
	        Collection listaActualizada = new Vector();

	        // Armar la lista actualizada con los encuentros que no han terminado
	        Iterator iter = encuentros.iterator();
	        while( iter.hasNext() ) {
	            Encuentro e = ( Encuentro )iter.next();
	            if( !e.encuentroTerminado() )
	                listaActualizada.add( e );
	        }

	        // Reemplazar la lista antigua con la lista actualizada
	        encuentros = listaActualizada;

	        return encuentros;
	    }

	    public void recibirConexiones() {
	        String aux = config.getProperty( "servidor.puerto" );
	        int puerto = Integer.parseInt( aux );
	        try
	        {
	            receptor = new ServerSocket( puerto );

	            while( true ){
	                // Esperar una nueva conexión
	                Socket socketNuevoCliente = receptor.accept( );

	                // Intentar iniciar un encuentro con el nuevo cliente
	                crearEncuentro( socketNuevoCliente );
	            }
	        }
	        catch( IOException e ) {
	            e.printStackTrace( );
	        }finally {
	            try{
	                receptor.close();
	            }catch( IOException e ) {
	                e.printStackTrace();
	            }
	        }
	    }

	    synchronized private void crearEncuentro( Socket socketNuevoCliente ) throws IOException {
	        if( socketJugadorEnEspera == null ) {
	            // No hay un oponente aún, así que hay que dejarlo en espera
	            socketJugadorEnEspera = socketNuevoCliente;
	        }else {
	            // Ya se tiene un oponente así que se puede empezar una partida
	            Socket jug1 = socketJugadorEnEspera;
	            Socket jug2 = socketNuevoCliente;

	            socketJugadorEnEspera = null;

	            try{
	                Encuentro nuevo = new Encuentro( jug1, jug2, adminResultados );
	                iniciarEncuentro( nuevo );
	            }catch( IOException e ) {
	                try{
	                    jug1.close( );
	                }
	                catch( IOException e1 )
	                {
	                    e.printStackTrace( );
	                }
	                try{
	                    jug2.close( );
	                }catch( IOException e2 ) {
	                    e.printStackTrace();
	                }

	                e.printStackTrace();
	            }
	        }

	    }

	    protected void iniciarEncuentro( Encuentro nuevoEncuentro ) {
	        encuentros.add( nuevoEncuentro );
	        nuevoEncuentro.start();
	    }
	    
}
