package es.studium.artistic;

public class Artista extends Usuario{
    private String tlf;
    private String instagram;
    private String web;
    private String biografia;

    public Artista(){
        super();
        tlf = "";
        instagram = "";
        web = "";
        biografia = "";
    }
    public Artista(int id,int tipoU,String usu,String email,String n,String insta,String w, String bio){
        super(usu,email,id,tipoU);
        this.tlf = n;
        this.instagram = insta;
        this.web = w;
        this.biografia = bio;
    }

    public String getTlf() {
        return tlf;
    }

    public void setTlf(String tlf) {
        this.tlf = tlf;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

}
