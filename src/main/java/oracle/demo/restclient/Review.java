package oracle.demo.restclient;

public class Review{

    private String id;
    private int star;
    private String comment;

    public Review(){}

    public Review(String id, int star, String comment){
        this.id = id;
        this.star = star;
        this.comment = comment;
    }
    public Review(int star, String comment){
        this.star = star;
        this.comment = comment;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }


    public void setStar(int star){
        this.star = star;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public int getStar(){
        return star;
    }

    public String getComment(){
        return comment;
    }

    public String toString(){
        return String.format("[id=%s,star=%d,comment=%s]", id, star, comment);
    }


}