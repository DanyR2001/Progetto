package Model;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;

public class utente {
    private int ID;
    private String Nome;
    private String Cognome;
    private String Mail;
    private String Passwordhash;
    private Date DataNascita;
    private boolean admin_bool;

    public utente(){};

    public utente(int id, String name,String cognome, String email, Date dataNascita,String passwordhash, boolean admin_bool) {
        ID = id;
        Nome = name;
        Cognome=cognome;
        Mail = email;
        DataNascita= dataNascita;
        this.Passwordhash=passwordhash;
        this.admin_bool = admin_bool;
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

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getCognome() {
        return Cognome;
    }

    public void setCognome(String cognome) {
        Cognome = cognome;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
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

    public void setPassword(String password) { // password è inserita dall’utente
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

}
