/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import beans.Article;
import beans.Category;
import java.util.List;

/**
 *
 * @author Yassine
 */
public interface CategoryDao {
    public List<Category> categories()throws DAOException;
}
