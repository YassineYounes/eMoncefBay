package dao;

import beans.Category;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class CategoryDaoImpl implements CategoryDao {
    private DAOFactory daoFactory;
    private static final String SQL_SELECT_ALL = "SELECT description,name,image,CID FROM category";

    public CategoryDaoImpl(DAOFactory daoFactory) {
            this.daoFactory= daoFactory ;
    }
    
    @Override
	public List<Category> categories() throws DAOException {
            Connection connexion = null;
	    Statement statement = null;
	    ResultSet resultSet = null;
	    Category category = null;
            List<Category> list =  new ArrayList<Category>();

	    try {
	        connexion = daoFactory.getConnection();
	        statement = connexion.createStatement();
	        resultSet = statement.executeQuery(SQL_SELECT_ALL);
	        while ( resultSet.next() ) {
	            category = map( resultSet );
                    list.add(category);
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermeturesSilencieuses( resultSet, statement, connexion );
	    }

	    return list;
	}
    
    
    
    /* Fermeture silencieuse du resultSet */
    public static void fermetureSilencieuse( ResultSet resultSet ) {
	    if ( resultSet != null ) {
	        try {
	            resultSet.close();
	        } catch ( SQLException e ) {
	            System.out.println( "échec de la fermeture du ResultSet : " + e.getMessage() );
	        }
	    }
    }

	/* Fermeture silencieuse du statement */
    public static void fermetureSilencieuse( Statement statement ) {
	    if ( statement != null ) {
	        try {
	            statement.close();
	        } catch ( SQLException e ) {
	            System.out.println( "échec de la fermeture du Statement : " + e.getMessage() );
	        }
	    }
    }

	/* Fermeture silencieuse de la connexion */
    public static void fermetureSilencieuse( Connection connexion ) {
	    if ( connexion != null ) {
	        try {
	            connexion.close();
	        } catch ( SQLException e ) {
	            System.out.println( "échec de la fermeture de la connexion : " + e.getMessage() );
	        }
	    }
    }

	/* Fermetures silencieuses du statement et de la connexion */
    public static void fermeturesSilencieuses( Statement statement, Connection connexion ) {
	    fermetureSilencieuse( statement );
	    fermetureSilencieuse( connexion );
    }

	/* Fermetures silencieuses du resultset, du statement et de la connexion */
    public static void fermeturesSilencieuses( ResultSet resultSet, Statement statement, Connection connexion ) {
	    fermetureSilencieuse( resultSet );
	    fermetureSilencieuse( statement );
	    fermetureSilencieuse( connexion );
    }
    private static Category map( ResultSet resultSet ) throws SQLException {
	    Category category = new Category();
	    category.setDescription( resultSet.getString( "description"));
	    category.setName( resultSet.getString( "name" ) );
	    category.setImage( resultSet.getString( "image"));
            category.setCID(resultSet.getInt( "CID"));
            
	    return category;
	}
    
}
