package model;

import java.util.Objects;
import java.util.UUID;

public abstract sealed class Utilizator permits Cursant, Profesor{
    protected String id;
    protected String nume;
    protected String prenume;
    protected String email;

    public Utilizator(String prenume, String nume, String email){
        this.id = UUID.randomUUID().toString();
        this.prenume = prenume;
        this.nume = nume;
        this.email = email;
    }

    public abstract String getRol();

    public abstract void afiseazaDashboard();

    public String getNume() {return nume;}
    public void setNume(String nume) { this.nume = nume;}

    public String getPrenume() {return prenume;}
    public void setPrenume(String prenume){ this.prenume = prenume;}

    public String getEmail() {return email;}

    public String getId() {return id;}

    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Utilizator u = (Utilizator) o;
        return Objects.equals(id, u.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

    @Override
    public String toString(){
        return "utilizator{" +
                "id = '" + id + '\'' +
                ", nume = '" + nume + '\'' +
                ", prenume = '" + prenume + '\'' +
                ", email = '" + email + '\'' +
                '}';
    }

}
