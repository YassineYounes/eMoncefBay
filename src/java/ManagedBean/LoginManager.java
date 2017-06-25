package ManagedBean;

import beans.Member;
import dao.DAOFactory;
import dao.MemberDaoImpl;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name="LoginManager")
@Dependent
public class LoginManager {

    private String email;
    private String password;
    private MemberDaoImpl dao;
    public static boolean sessionExist;
    
    public LoginManager() {
        this.dao= (MemberDaoImpl) DAOFactory.getInstance().getMemberDao();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    
    public String validation(){
        Member member = dao.trouver(email);
        if (member == null)
            return "error";
        if (member.getPassword().equals(password)){
            return LoginManager.createSession(member);
        }
        return "error";
    }
    
    public static String createSession(Member member){
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        session.setAttribute("member", member);
        LoginManager.sessionExist = true;
        return "index?faces-redirect=true";
    
    }
    
    public String destroySession(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        LoginManager.sessionExist = false;
        return "index?faces-redirect=true";
    }

    public boolean isSessionExist() {
        return !sessionExist;
    }
    public String addArticle(){
        return "article";
    }

}