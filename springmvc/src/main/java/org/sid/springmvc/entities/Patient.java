package org.sid.springmvc.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Entity
@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class Patient {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)	
 private Long id;
  //annotation de validation
  @NotNull
  @Size(min = 2, max = 14)
 // @Size(min = 2, max = 14, message="Non valide")
  private String name;
  @Temporal(TemporalType.DATE)
  @DateTimeFormat(pattern ="yyyy-MM-dd")
  private Date dateNaissance;
  private boolean malade;
  //validation du score cad pas valeur sup a 4
  @DecimalMin("4")
  private int score;
 
}
