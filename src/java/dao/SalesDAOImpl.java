/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import beans.Sales;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import normalClass.CookieHelper;

/**
 *
 * @author bbrayek
 */
public class SalesDAOImpl implements SalesDAO {
    
    private final DAOFactory daoFactory;
    
    private static final String SQL_INSERT = "INSERT INTO Sales (finalPrice,mid,aid,name) VALUES (?, ?, ?,?)";
    private static final String SQL_SELECT_PAR_MID = "SELECT name,sid, mid,finalprice,aid FROM sales WHERE mid = ?";
    
    
    
    public SalesDAOImpl(DAOFactory daoFactory) {
            this.daoFactory= daoFactory ;
    }
    @Override
    public void creer(Sales sales) throws DAOException{
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        
	try {   
            connexion = daoFactory.getConnection();
	    preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, 
                    sales.getFinalPrice(),sales.getMID(),sales.getAID(),sales.getName());
	    
            int statut = preparedStatement.executeUpdate();
	       
	    if ( statut == 0 ) {
                throw new DAOException( "échec de la création du sale, aucune ligne ajoutée dans la table." );
	    }
            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
	    if ( valeursAutoGenerees.next() ) {
                sales.setSID( valeursAutoGenerees.getInt( 1 ) );
	    } else {
	        throw new DAOException( "échec de la création de sale en base, aucun ID auto-généré retourné." );
	    }
	   
	} 
        catch ( SQLException e ) {
	        throw new DAOException( e );
        } 
        finally {
	        fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
        }
        CookieHelper cookie = new CookieHelper();
        ArticleDaoImpl articleDao = new ArticleDaoImpl(this.daoFactory);
        int category = articleDao.find(sales.getAID()).getCID();
        Cookie newCookie = cookie.getCookie(String.valueOf(category));
        if (newCookie == null){
            cookie.setCookie(String.valueOf(category),"1" , 66*666*666);
        }else{
            cookie.setCookie(String.valueOf(category),String.valueOf(Integer.parseInt(newCookie.getValue())+1) , 66*666*666);
        }
    
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
    public List<Sales> findByMid(int MID) throws DAOException {
            Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    Sales sale = null;
            List<Sales> list =  new ArrayList<Sales>();


	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_PAR_MID, false, MID );
	        resultSet = preparedStatement.executeQuery();
	        while ( resultSet.next() ) {
	            sale = map( resultSet );
                    list.add(sale);
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermeturesSilencieuses( resultSet, preparedStatement, connexion );
	    }

	    return list;
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
    
     private static Sales map( ResultSet resultSet ) throws SQLException {
	    Sales sale = new Sales ();
	    sale.setMID(resultSet.getInt( "mid"));
            sale.setSID(resultSet.getInt( "SID"));
            sale.setFinalPrice(resultSet.getInt( "finalprice"));
            sale.setName(resultSet.getString( "name"));
            sale.setAID(resultSet.getInt( "aid" ));
            
	    return sale;
    }
}
