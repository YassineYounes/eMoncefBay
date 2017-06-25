package dao;

import beans.Member;

public interface MemberDao {

    void creer( Member utilisateur ) throws DAOException;

    Member trouver( String email ) throws DAOException;
    
    Member trouver( int id ) throws DAOException;

}