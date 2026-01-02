package ma.projet.service_car.entities;

public class Client {

    private Long id;
    private String nom;
    private Float age;

    public Client() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Float getAge() { return age; }
    public void setAge(Float age) { this.age = age; }
}

