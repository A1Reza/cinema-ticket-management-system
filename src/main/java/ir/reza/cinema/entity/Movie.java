package ir.reza.cinema.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "movies")
@NamedQuery(
        name = "Movie.findAvailable",
        query = "SELECT m FROM Movie m WHERE m.status = :status"
)
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovieStatus status;

    public Movie() {
    }

    public Movie(String title, Double price, MovieStatus status) {
        this.title = title;
        this.price = price;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public MovieStatus getStatus() {
        return status;
    }

    public void setStatus(MovieStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", status=" + status +
                '}';
    }
}