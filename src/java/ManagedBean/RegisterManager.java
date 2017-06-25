    package ManagedBean;

import beans.Member;
import dao.DAOFactory;
import dao.MemberDaoImpl;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;

@ManagedBean(name="RegisterManager")
@Dependent
public class RegisterManager {

    private Member member;
    private MemberDaoImpl dao;
    
    public RegisterManager() {
        this.dao= (MemberDaoImpl) DAOFactory.getInstance().getMemberDao();
        this.member = new Member();
    }
    /**
     * @return the member
     */
    public Member getMember() {
        return member;
    }

    /**
     * @param member the member to set
     */
    public void setMember(Member member) {
        this.member = member;
    }
    
    public String validation(){
        dao.creer(member);
        return LoginManager.createSession(member);
        
    }
}
