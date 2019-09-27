package jk.springblog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jk.springblog.model.enums.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    @Type(type = "text")
    private String content;
    @Enumerated
    //@Transient -ignoruje mapowanie orm dla tego pola
    private CategoryEnum category;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    private int like_number;
    private int not_like_number;


    public Post(String title, String content, CategoryEnum category, User user) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.user = user;
    }

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post")
    List<Comment> comments = new ArrayList<>();
}
