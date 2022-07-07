package Model;

public class Tag {
    private int id_tag;
    private String Nome;

    public int getId_tag() {
        return id_tag;
    }

    public void setId_tag(int id_tag) {
        this.id_tag = id_tag;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public boolean equals(Tag x){
        return x.getId_tag()==this.getId_tag();
    }

}
