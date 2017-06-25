/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import beans.Article;
import java.util.List;

/**
 *
 * @author bbrayek
 */
public interface ArticleDao {
    public void creer(Article article) throws DAOException;
    public void bet(Article article, int bet) throws DAOException;
    public void delete(int aid) throws DAOException;
    public Article find(int id) throws DAOException;
    public List<Article> articlesByCID(int id) throws DAOException;
    public List<Article> articles()throws DAOException;
}
