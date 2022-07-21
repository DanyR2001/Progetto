package Model;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.Objects;

public class Utente {
    private int ID;
    private String Nome;
    private String Cognome;
    private String Mail;
    private String Passwordhash;
    private Date DataNascita;
    private boolean admin_bool;
    private String Via;
    private int Cap;
    private int Civico;

    static String CapitalizeName(String name){
        String x[]=name.split(" ");
        System.out.println("capName "+x.length+" "+name);
        if(x.length==1){
            return name.substring(0,1).toUpperCase()+name.substring(1,name.length()).toLowerCase();
        }
        else{
            String returns ="";
            for(int i=0;i<x.length;i++){
                returns+=x[i].substring(0,1).toUpperCase()+x[i].substring(1,x[i].length()).toLowerCase();
                if(i!=x.length-1)
                    returns+=" ";
            }
            return returns;
        }
    }

    public Utente() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String name) {
        Nome = CapitalizeName(name);
    }

    public String getCognome() {
        return Cognome;
    }

    public void setCognome(String cognome) {
        Cognome = CapitalizeName(cognome);
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail.toLowerCase();
    }

    public String getPasswordhash() {
        return Passwordhash;
    }

    public Date getDataNascita() {
        return DataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        DataNascita = dataNascita;
    }

    public boolean isAdmin_bool() {
        return admin_bool;
    }

    public void setAdmin_bool(boolean admin_bool) {
        this.admin_bool = admin_bool;
    }

    public void setPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(password.getBytes(StandardCharsets.UTF_8));
            this.Passwordhash = String.format("%040x", new
                    BigInteger(1, digest.digest()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPasswordFromDB(String password) {
        Passwordhash=password;
    }

    public String getVia() {
        return Via;
    }

    public void setVia(String via) {
        Via = via.substring(0,1).toUpperCase()+via.substring(1,via.length()).toLowerCase();
    }

    public int getCap() {
        return Cap;
    }

    public void setCap(int cap) {
        Cap = cap;
    }

    public int getCivico() {
        return Civico;
    }

    public void setCivico(int civico) {
        Civico = civico;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utente)) return false;
        Utente utente = (Utente) o;
        return ID == utente.ID && admin_bool == utente.admin_bool && Cap == utente.Cap && Civico == utente.Civico && Nome.equals(utente.Nome) && Cognome.equals(utente.Cognome) && Mail.equals(utente.Mail) && Passwordhash.equals(utente.Passwordhash) && DataNascita.equals(utente.DataNascita) && Via.equals(utente.Via);
    }

    @Override
    public String toString() {
        return "Utente{" +
                "ID=" + ID +
                ", Nome='" + Nome + '\'' +
                ", Cognome='" + Cognome + '\'' +
                ", Mail='" + Mail + '\'' +
                ", Passwordhash='" + Passwordhash + '\'' +
                ", DataNascita=" + DataNascita +
                ", admin_bool=" + admin_bool +
                ", Via='" + Via + '\'' +
                ", Cap=" + Cap +
                ", Civico=" + Civico +
                '}';
    }
}
