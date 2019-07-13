package jk.springblog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank (message = "Name cannot be blank")
    private String name;
    @NotBlank (message = "E-mail cannot be blank")
    @Email(message = "Invalid e-mail address")
    private String email;
    @NotBlank(message = "Phone cannot be blank")
    @Size(min = 9, message = "Invalid phone number")
    private String phone;
    @NotBlank
    @Type(type = "text")
    private String message;
    private LocalDateTime date_added = LocalDateTime.now();
    private boolean status = false;


}
