package ManagedBean;

import beans.Article;
import beans.Bet;
import beans.Member;
import beans.Sales;
import dao.ArticleDaoImpl;
import dao.BetDAOImpl;
import dao.DAOFactory;
import dao.SalesDAOImpl;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import normalClass.CookieHelper;

@ManagedBean(name = "ArticleManager")
@Dependent
public class ArticleManager {

    private Article article;
    private final ArticleDaoImpl dao;
    private final BetDAOImpl betDAO;
    private final SalesDAOImpl salesDAO;
    private List<Article> articles;
    private List<Sales> sale;
    private Part img;
    private int bet;
    private int betValue;
    private int directPrice;

    public int getBet() {
        return bet;
    }
    public int getMaxBet(int aid){
        return this.betDAO.checkValue(aid);
    }           
    public void setBet(int bet) {
        this.bet = bet;
    }
    
            
    public ArticleManager() {
        this.article = new Article();
        this.dao= (ArticleDaoImpl) DAOFactory.getInstance().getArticleDao();
        this.betDAO=(BetDAOImpl) DAOFactory.getInstance().getBetDAO();
        this.salesDAO = (SalesDAOImpl) DAOFactory.getInstance().getSalesDAO();
    }

    /**
     * @return the article
     */
    public Article getArticle() {
        return article;
    }
    

    /**
     * @param article the article to set
     */
    public void setArticle(Article article) {
        this.article = article;
    }

    public String validation(){
        if (LoginManager.sessionExist){
        this.doUpload();
        dao.creer(article);
        return "index";}
        return "login";
    }
    public String delete(){
        dao.delete(article.getAID());
        return "index";
    }
    public List<Article> getArticles(){
        return dao.articles();
    }
    public List<Article> getArticlesByCID(int CID){
        return dao.articlesByCID(CID);
    }
    public List<Sales> getSalesByMID(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        Member member =(Member) session.getAttribute("member");
        return salesDAO.findByMid(member.getMid());
    }
    public Article find(int AID){
        Article newArticle = dao.find(AID);
        CookieHelper cookie = new CookieHelper();
        Cookie newCookie = cookie.getCookie(String.valueOf(newArticle.getCID()));
        int coeff;
        if (newCookie != null){
            coeff = Integer.parseInt(newCookie.getValue());
        }else{
            coeff = 0;
        }
        System.out.println(coeff);
        int newBetPrice = newArticle.getBetPrice()+coeff;
        System.out.println (newBetPrice);
        newArticle.setBetPrice(newBetPrice);
        int newDirectPrice = newArticle.getDirectPrice()+coeff;
        newArticle.setDirectPrice(newDirectPrice);
        return newArticle;
    }
    public void doUpload(){
        try{
            InputStream in = getImg().getInputStream();
            File f = new File("C:/Users/Yassine/Documents/NetBeansProjects/eMoncefBay/web/resources/images/uploaded/"+getImg().getSubmittedFileName());
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f);
            article.setImage("/resources/images/uploaded/"+getImg().getSubmittedFileName());
            byte[] buffer = new byte[1024];
            int length;
            while((length=in.read(buffer))>0){
                out.write(buffer, 0, length);
            }
            out.close();
            in.close();
            
         
        }catch(Exception e){
                    System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
    /**
     * @return the img
     */
    public Part getImg() {
        return img;
    }

    /**
     * @param img the img to set
     */
    public void setImg(Part img) {
        this.img = img;
    }
    
    public String makeBet(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String,String> params = facesContext.getExternalContext().getRequestParameterMap();
	int aid = Integer.parseInt( params.get("aid"));
        int value = this.bet;
       
        Bet newBet = new Bet();
        //getting istance of context
        
        //getting session to get the mid 
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        Member member =(Member) session.getAttribute("member");
        
        newBet.setMID(member.getMid());
        newBet.setAID(aid);
        newBet.setValue(value);
        this.betDAO.creer(newBet);
        return "index";
    }
    public int getBetValue(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String,String> params = facesContext.getExternalContext().getRequestParameterMap();
	int aid = Integer.parseInt( params.get("aid"));
        betValue = getMaxBet(aid);
        return betValue;
    }
    public int directPrice(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String,String> params = facesContext.getExternalContext().getRequestParameterMap();
	int aid = Integer.parseInt( params.get("aid"));
        directPrice= dao.find(aid).getDirectPrice();
        return directPrice;
    }
    public int priceValue(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String,String> params = facesContext.getExternalContext().getRequestParameterMap();
	int aid = Integer.parseInt( params.get("aid"));
        int betPrice = getMaxBet(aid);
        return betPrice;
    }
    
    public String makeSale(){
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String,String> params = facesContext.getExternalContext().getRequestParameterMap();
	int aid = Integer.parseInt( params.get("aid"));
        int value = dao.find(aid).getDirectPrice();
        String name = dao.find(aid).getTitle();
        Sales sale = new Sales();
        //getting session to get the mid 
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        Member member =(Member) session.getAttribute("member");
        
        sale.setMID(member.getMid());
        sale.setAID(aid);
        sale.setFinalPrice(value);
        sale.setName(name);
        this.salesDAO.creer(sale);
        this.dao.delete(aid);
        return "index";
    }
    public void validateSalePrice(FacesContext context,
            UIComponent componentToValidate,
            Object value)
            throws ValidatorException {
        if (article.getDirectPrice() <= article.getBetPrice()) {
            FacesMessage message =
                    new FacesMessage("Sale Price must be higher than  "
                    + "Purchase Price");
            throw new ValidatorException(message);
        }
    
    }
    public void validateBetPrice(FacesContext context,
            UIComponent componentToValidate,
            Object value)
            throws ValidatorException {
        if (bet <= getMaxBet(article.getAID())) {
            FacesMessage message =
                    new FacesMessage("Bet Price must be higher than the Older Bet Price");
            throw new ValidatorException(message);
        }
        else if (bet >= article.getDirectPrice()) {
            FacesMessage message =
                    new FacesMessage("Bet Price Must Be Lower Than The Direct Price");
            throw new ValidatorException(message);
        }
    }
    
 
}