package com.authorize.model.entity;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(unique = true)
	private String name;
	private String password;
	private String city;
	@ManyToOne
	@JoinColumn(name = "reporting_id")
	private User reportingTo;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	@JsonIgnore
	private List<Role> role;
	@ManyToOne
	@JoinColumn(name = "org_id")
	private Organization organization;

	public User(String name, String password, String city, List<Role> role) {
		super();
		this.name = name;
		this.password = password;
		this.city = city;
		this.role = role;
	}

	public User(User user) {
		this.city = user.city;
		this.name = user.name;
		this.password = user.password;
		this.role = user.getRole();
		this.organization = user.getOrganization();
	}
	
	@Override
	  public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    User user = (User) o;
	    return Objects.equals(id, user.id)
	        && Objects.equals(name, user.name)
	        && Objects.equals(password, user.password);
	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(id, name, password);
	  }
}
