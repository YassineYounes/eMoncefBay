package ManagedBean;

import beans.Category;
import dao.CategoryDaoImpl;
import dao.DAOFactory;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "CategoryManager")
@Dependent
public class CategoryManager {

    private Category category;
    private CategoryDaoImpl dao;
    private List<Category> categories;
    
            
    public CategoryManager() {
        this.category = new Category();
        this.dao= (CategoryDaoImpl) DAOFactory.getInstance().getCategoryDao();
    }

    
    public Category getCategory() {
        return category;
    }

    
    public void setCategory(Category category) {
        this.category = category;
    }
    public List<Category> getCategories(){
        return dao.categories();
    }
    public String redirect(){
        return "category";
    }
    
}
