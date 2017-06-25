/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import beans.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author bbrayek
 */
public class CheckerDAOImpl implements CheckerDAO {
    private final DAOFactory daoFactory;
    private final ArticleDaoImpl dao;
    private final BetDAOImpl betDAO;
    private final SalesDAOImpl salesDAO;
     
    private static final String SQL_CHECK ="select aid from article where deadline = CURTIME()";
    
    public CheckerDAOImpl(DAOFactory daoFactory) {
        this.daoFactory= daoFactory ;
        this.dao= (ArticleDaoImpl) DAOFactory.getInstance().getArticleDao();
        this.betDAO=(BetDAOImpl) DAOFactory.getInstance().getBetDAO();
        this.salesDAO = (SalesDAOImpl) DAOFactory.getInstance().getSalesDAO();
    }
    @Override
    public void check() throws DAOException{
        Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
            int aid = 0;
            int finalPrice = 0;
            int owner = 0;
            Sales sale = new Sales();
            try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_CHECK, false );
	        resultSet = preparedStatement.executeQuery();
	        while ( resultSet.next() ) {
	            //aid contains articles that are sold and should be removed
                    aid = resultSet.getInt("aid");
                    // insertion in sales table
                    //gettin the finl price from Bet table
                    finalPrice = this.betDAO.checkValue(aid);
                    owner = this.betDAO.checkOwner(aid);
                    sale.setAID(aid);
                    sale.setFinalPrice(finalPrice);
                    sale.setMID(owner);
                    this.salesDAO.creer(sale);
                    this.dao.delete(aid);
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermeturesSilencieuses( resultSet, preparedStatement, connexion );
	    }
	}
   
    
    
    
    public static PreparedStatement initialisationRequetePreparee(Connection connexion, String sql, boolean returnGeneratedKeys, Object... objets )throws SQLException {
        PreparedStatement preparedStatement = connexion.prepareStatement( sql, returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS );
        for ( int i = 0; i < objets.length; i++ ) {
            preparedStatement.setObject( i + 1, objets[i] );
        }
        return preparedStatement;
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
   
}
