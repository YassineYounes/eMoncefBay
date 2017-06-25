package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import beans.Member;

public class MemberDaoImpl implements MemberDao {
    private DAOFactory daoFactory;
    private static final String SQL_SELECT_PAR_EMAIL = "SELECT mid, email,lastName,firstName, password, numT FROM member WHERE email = ?";
    private static final String SQL_INSERT = "INSERT INTO member (email, password, lastName, firstName, numT) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_PAR_ID = "SELECT mid, email, lastname, firstName, password, numT FROM member WHERE id = ?";

    
    
    
	public MemberDaoImpl(DAOFactory daoFactory) {
            this.daoFactory= daoFactory ;
	}
	
	@Override
	public void creer(Member member) throws DAOException {
            Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet valeursAutoGenerees = null;

	    try {
	        
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, member.getEmail(), member.getPassword(), member.getLastName(), member.getFirstName(), member.getNumT());
	        int statut = preparedStatement.executeUpdate();
	       
	        if ( statut == 0 ) {
	            throw new DAOException( "�chec de la cr�ation de l'user, aucune ligne ajout�e dans la table." );
	        }
	        
	        valeursAutoGenerees = preparedStatement.getGeneratedKeys();
	        if ( valeursAutoGenerees.next() ) {
                    member.setMid( valeursAutoGenerees.getInt( 1 ) );
	        } else {
	            throw new DAOException( "�chec de la cr�ation de l'user en base, aucun ID auto-g�n�r� retourn�." );
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
	    }

	}

	@Override
	public Member trouver(String email) throws DAOException {
            Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    Member member = null;

	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_PAR_EMAIL, false, email );
	        resultSet = preparedStatement.executeQuery();
	        if ( resultSet.next() ) {
	            member = map( resultSet );
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermeturesSilencieuses( resultSet, preparedStatement, connexion );
	    }

	    return member;
	}
	
	public static PreparedStatement initialisationRequetePreparee(
                    Connection connexion, String sql, boolean returnGeneratedKeys, Object... objets )throws SQLException 
	{
	    PreparedStatement preparedStatement = connexion.prepareStatement( sql, returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS );
	    for ( int i = 0; i < objets.length; i++ ) {
	        preparedStatement.setObject( i + 1, objets[i] );
	    }
	    return preparedStatement;
	}
	
	private static Member map( ResultSet resultSet ) throws SQLException {
	    Member member = new Member();
	    member.setMid( resultSet.getInt( "mid" ) );
	    member.setEmail( resultSet.getString( "email" ) );
	    member.setPassword( resultSet.getString( "password" ) );
	    member.setLastName( resultSet.getString( "lastName" ) );
	    member.setLastName( resultSet.getString( "firstName" ) );
            member.setNumT(resultSet.getString( "numT" ));
            
	    return member;
	}
	public static void fermetureSilencieuse( ResultSet resultSet ) {
	    if ( resultSet != null ) {
	        try {
	            resultSet.close();
	        } catch ( SQLException e ) {
	            System.out.println( "�chec de la fermeture du ResultSet : " + e.getMessage() );
	        }
	    }
	}

	/* Fermeture silencieuse du statement */
	public static void fermetureSilencieuse( Statement statement ) {
	    if ( statement != null ) {
	        try {
	            statement.close();
	        } catch ( SQLException e ) {
	            System.out.println( "�chec de la fermeture du Statement : " + e.getMessage() );
	        }
	    }
	}

	/* Fermeture silencieuse de la connexion */
	public static void fermetureSilencieuse( Connection connexion ) {
	    if ( connexion != null ) {
	        try {
	            connexion.close();
	        } catch ( SQLException e ) {
	            System.out.println( "�chec de la fermeture de la connexion : " + e.getMessage() );
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



	@Override
	public Member trouver(int id) throws DAOException {
            Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    Member member = null;

	    try {
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_PAR_ID, false, id );
	        resultSet = preparedStatement.executeQuery();
	        if ( resultSet.next() ) {
	            member = map( resultSet );
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermeturesSilencieuses( resultSet, preparedStatement, connexion );
	    }

	    return member;
	}
}
