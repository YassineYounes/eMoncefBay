package dao;

import beans.Article;
import beans.Member;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;


public class ArticleDaoImpl implements ArticleDao {
    private DAOFactory daoFactory;
    private static final String SQL_INSERT = "INSERT INTO article (mid,description,title,deadline,image,CID,betPrice,directPrice) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM article WHERE aid = ?";
    private static final String SQL_SELECT_PAR_ID = "SELECT aid, mid,description,title,deadline,image,CID,betPrice,directPrice FROM article WHERE aid = ?";
    private static final String SQL_SELECT_PAR_CID = "SELECT aid, mid,description,title,deadline,image,CID,betPrice,directPrice FROM article WHERE cid = ?";
    private static final String SQL_SELECT_ALL = "SELECT aid, mid,description,title,deadline,image,CID,betPrice,directPrice FROM article order by AID";
    private static final String SQL_UPDATE_BET_PRICE = "UPDATE article SET betPrice = ? WHERE aid = ?";

    public ArticleDaoImpl(DAOFactory daoFactory) {
            this.daoFactory= daoFactory ;
    }
    
    @Override
    public void creer(Article article) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        //getting istance of context
        FacesContext facesContext = FacesContext.getCurrentInstance();
        //getting session to get the mid !! 
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        Member member =(Member) session.getAttribute("member");
	try {   
            connexion = daoFactory.getConnection();
	    preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, 
            member.getMid(), article.getDescription(), article.getTitle(), article.getDeadline(), article.getImage(),
            article.getCID(),article.getBetPrice(),article.getDirectPrice());
	    
            int statut = preparedStatement.executeUpdate();
	       
	    if ( statut == 0 ) {
                throw new DAOException( "échec de la création de l'user, aucune ligne ajoutée dans la table." );
	    }
	        
	    valeursAutoGenerees = preparedStatement.getGeneratedKeys();
	    if ( valeursAutoGenerees.next() ) {
                article.setAID( valeursAutoGenerees.getInt( 1 ) );
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
	public Article find(int id) throws DAOException {
            Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    Article article = null;

	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_PAR_ID, false, id );
	        resultSet = preparedStatement.executeQuery();
	        if ( resultSet.next() ) {
	            article = map( resultSet );
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermeturesSilencieuses( resultSet, preparedStatement, connexion );
	    }

	    return article;
	}
        @Override
	public List<Article> articlesByCID(int CID) throws DAOException {
            Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    Article article = null;
            List<Article> list =  new ArrayList<Article>();


	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_PAR_CID, false, CID );
	        resultSet = preparedStatement.executeQuery();
	        while ( resultSet.next() ) {
	            article = map( resultSet );
                    list.add(article);
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermeturesSilencieuses( resultSet, preparedStatement, connexion );
	    }

	    return list;
	}
    @Override
	public List<Article> articles() throws DAOException {
            Connection connexion = null;
	    Statement statement = null;
	    ResultSet resultSet = null;
	    Article article = null;
            List<Article> list =  new ArrayList<Article>();

	    try {
	        connexion = daoFactory.getConnection();
	        statement = connexion.createStatement();
	        resultSet = statement.executeQuery(SQL_SELECT_ALL);
	        while ( resultSet.next() ) {
	            article = map( resultSet );
                    list.add(article);
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermeturesSilencieuses( resultSet, statement, connexion );
	    }

	    return list;
	}
    @Override
    public void delete(int aid) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
	try {   
            connexion = daoFactory.getConnection();
	    preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE, true,aid);
	    
            int statut = preparedStatement.executeUpdate();
	       
	    if ( statut == 0 ) {
                throw new DAOException( "échec de la suppression de l'article, aucune ligne supprimé de la table." );
	    }
	    
	} 
        catch ( SQLException e ) {
	        throw new DAOException( e );
        } 
        finally {
	        fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
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
    private static Article map( ResultSet resultSet ) throws SQLException {
	    Article article = new Article();
	    article.setMid( resultSet.getInt( "mid"));
	    article.setDescription( resultSet.getString( "description"));
	    article.setTitle( resultSet.getString( "title" ) );
	    article.setDeadline( resultSet.getDate( "Deadline"));
	    article.setImage( resultSet.getString( "image"));
            article.setCID(resultSet.getInt( "CID"));
            article.setBetPrice(resultSet.getInt( "betprice"));
            article.setDirectPrice(resultSet.getInt( "directPrice"));
            article.setAID(resultSet.getInt( "aid" ));
            
	    return article;
    }
    @Override
    public void bet(Article article,int bet) throws DAOException{
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;
        //getting istance of context
        FacesContext facesContext = FacesContext.getCurrentInstance();
        //getting session to get the mid !! 
	try {   
            connexion = daoFactory.getConnection();
	    preparedStatement = initialisationRequetePreparee( connexion, SQL_UPDATE_BET_PRICE, true, 
            bet,article.getAID());
	    
            int statut = preparedStatement.executeUpdate();
	       
	    if ( statut == 0 ) {
                throw new DAOException( preparedStatement.toString() );
	    }
	        
	    valeursAutoGenerees = preparedStatement.getGeneratedKeys();
	    if ( valeursAutoGenerees.next() ) {
                article.setAID( valeursAutoGenerees.getInt( 1 ) );
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
}
