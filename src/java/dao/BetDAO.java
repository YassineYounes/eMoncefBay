/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import beans.Bet;


/**
 *
 * @author bbrayek
 */
public interface BetDAO {
    public void creer(Bet bet) throws DAOException;
    public void delete(Bet bet) throws DAOException;
    public void delete(int aid) throws DAOException;
    //returns max bet from bets
    public int checkValue(int aid) throws DAOException;
    //returns max bet owner
    public int checkOwner(int aid) throws DAOException;
}
