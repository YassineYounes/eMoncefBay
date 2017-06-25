/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import beans.Bet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author bbrayek
 */
public class BetDAOImpl implements BetDAO {
    
    private final DAOFactory daoFactory;
    private static final String SQL_INSERT = "INSERT INTO bet (value,mid,aid) VALUES (?, ?, ?)";
    private static final String SQL_DELETE_BET = "DELETE FROM bet WHERE bid = ?";
    private static final String SQL_DELETE_ALL_BETS = "DELETE FROM bet WHERE aid = ?";
    private static final String SQL_SELECT_PAR_BID = "SELECT max(value) as max FROM bet WHERE aid = ?";
    private static final String SQL_SELECT_OWNER_PAR_BID = "SELECT mid FROM bet WHERE aid = ? AND value = "
            + "(SELECT max(value) as max FROM bet WHERE aid = ?)";
    
    public BetDAOImpl(DAOFactory daoFactory) {
            this.daoFactory= daoFactory ;
    }
    
    
    @Override
    public void creer(Bet bet) throws DAOException{
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        
	try {   
            connexion = daoFactory.getConnection();
	    preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, 
                    bet.getValue(),bet.getMID(),bet.getAID());
	    
            int statut = preparedStatement.executeUpdate();
	       
	    if ( statut == 0 ) {
                throw new DAOException( "échec de la création du bet, aucune ligne ajoutée dans la table." );
	    }
            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
	    if ( valeursAutoGenerees.next() ) {
                bet.setBid( valeursAutoGenerees.getInt( 1 ) );
	    } else {
	        throw new DAOException( "échec de la création de l'user en base, aucun ID auto-généré retourné." );
	    }
	   
	} 
        catch ( SQLException e ) {
	        throw new DAOException( e );
        } 
        finally {
	        fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
    }
    
    @Override
    public void delete(Bet bet) throws DAOException{
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
	try {   
            connexion = daoFactory.getConnection();
	    preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_BET, true, bet.getBid());
	    
            int statut = preparedStatement.executeUpdate();
	       
	    if ( statut == 0 ) {
                throw new DAOException( "échec de la suppression du bet, aucune ligne supprimé de la table." );
	    }
	    
	} 
        catch ( SQLException e ) {
	        throw new DAOException( e );
        } 
        finally {
	        fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
    }
    @Override
    public void delete(int aid) throws DAOException{
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
	try {   
            connexion = daoFactory.getConnection();
	    preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_ALL_BETS, true, aid);
	    
            int statut = preparedStatement.executeUpdate();
	       
	    if ( statut == 0 ) {
                throw new DAOException( "échec de la suppression du bet, aucune ligne supprimé de la table." );
	    }
	    
	} 
        catch ( SQLException e ) {
	        throw new DAOException( e );
        } 
        finally {
	        fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
    }
    @Override
    public int checkValue(int aid) throws DAOException{
        Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
            int value = 0;
	    
            try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_PAR_BID, false, aid );
	        resultSet = preparedStatement.executeQuery();
	        if ( resultSet.next() ) {
	            value = resultSet.getInt( "max");
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermeturesSilencieuses( resultSet, preparedStatement, connexion );
	    }

	    return value;
    }
    @Override
    public int checkOwner(int aid) throws DAOException {
       Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
            int mid = 0;
	    
            try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_OWNER_PAR_BID, false,aid, aid );
	        resultSet = preparedStatement.executeQuery();
	        if ( resultSet.next() ) {
	            mid = resultSet.getInt( "mid");
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermeturesSilencieuses( resultSet, preparedStatement, connexion );
	    }

	    return mid;
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
    public static PreparedStatement initialisationRequetePreparee(Connection connexion, String sql, boolean returnGeneratedKeys, Object... objets )throws SQLException {
    PreparedStatement preparedStatement = connexion.prepareStatement( sql, returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS );
    for ( int i = 0; i < objets.length; i++ ) {
        preparedStatement.setObject( i + 1, objets[i] );
    }
    return preparedStatement;
}

    
}
