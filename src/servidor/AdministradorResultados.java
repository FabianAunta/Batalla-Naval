package servidor;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;

public class AdministradorResultados {
	
	private Connection conexion;
    private Properties config;

    public AdministradorResultados( Properties propiedades ) {
        config = propiedades;

        File data = new File( config.getProperty( "admin.db.path" ) );
        System.setProperty( "derby.system.home", data.getAbsolutePath() );
    }

    @SuppressWarnings("deprecation")
	public void conectarABD() throws SQLException, Exception {
        String driver = config.getProperty( "admin.db.driver" );
        Class.forName( driver ).newInstance();

        String url = config.getProperty( "admin.db.url" );
        conexion = DriverManager.getConnection( url );
    }

    public void desconectarBD() throws SQLException {
        conexion.close();
        String down = config.getProperty( "admin.db.shutdown" );
        try {
            DriverManager.getConnection( down );
        }
        catch( SQLException e ) {
        	e.printStackTrace();
        }
    }

    public void inicializarTabla( ) throws SQLException {
        Statement s = conexion.createStatement( );

        boolean crearTabla = false;
        try {
            s.executeQuery( "SELECT * FROM resultados WHERE 1=2" );
        }catch( SQLException se ) {
            crearTabla = true;
        }
        if( crearTabla ) {
            s.execute( "CREATE TABLE resultados (nombre varchar(32), ganados int, perdidos int, PRIMARY KEY (nombre))" );
        }

        s.close();
    }

    public RegistroJugador consultarRegistroJugador( String nombre ) throws SQLException {
        RegistroJugador registro = null;

        String sql = "SELECT ganados, perdidos FROM resultados WHERE nombre ='" + nombre + "'";

        Statement st = conexion.createStatement();
        ResultSet resultado = st.executeQuery( sql );

        if( resultado.next( ) ) {
            int ganados = resultado.getInt( 1 );
            int perdidos = resultado.getInt( 2 );

            registro = new RegistroJugador( nombre, ganados, perdidos );

            resultado.close();
        }else {
            resultado.close();

            String insert = "INSERT INTO resultados (nombre, ganados, perdidos) VALUES ('" + nombre + "', 0,0)";
            st.execute( insert );

            registro = new RegistroJugador( nombre, 0, 0 );
        }
        st.close();
        return registro;
    }

    public Collection consultarRegistrosJugadores() throws SQLException {
        Collection registros = new LinkedList();

        String sql = "SELECT nombre, ganados, perdidos FROM resultados";

        Statement st = conexion.createStatement();
        ResultSet resultado = st.executeQuery( sql );

        while( resultado.next() ) {
            String nombre = resultado.getString( 1 );
            int ganados = resultado.getInt( 2 );
            int perdidos = resultado.getInt( 3 );

            RegistroJugador registro = new RegistroJugador( nombre, ganados, perdidos );
            registros.add( registro );
        }

        resultado.close();
        st.close();

        return registros;
    }
    public void registrarVictoria( String nombre ) throws SQLException {
        String sql = "UPDATE resultados SET ganados = ganados+1 WHERE nombre ='" + nombre + "'";

        Statement st = conexion.createStatement();
        st.executeUpdate( sql );
        st.close();
    }

    public void registrarDerrota( String nombre ) throws SQLException {
        String sql = "UPDATE resultados SET perdidos = perdidos+1 WHERE nombre ='" + nombre + "'";

        Statement st = conexion.createStatement( );
        st.executeUpdate( sql );
        st.close();
    }


}
