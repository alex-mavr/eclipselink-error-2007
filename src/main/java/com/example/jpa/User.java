package com.example.jpa;

import static com.example.jpa.User.BY_NAME_AND_EMAIL;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheIndex;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.config.QueryType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.QueryHint;
import jakarta.persistence.Table;

@Entity
@NamedQuery(name = BY_NAME_AND_EMAIL, query = "SELECT o FROM User o WHERE o.name = ?1 AND o.email = ?2",
            hints = { @QueryHint(name = QueryHints.QUERY_TYPE, value = QueryType.ReadObject) })
@Table(name = "\"USER\"")
@Cache(type = CacheType.FULL)
@CacheIndex(columnNames = { "name", "email" })
public class User {
  protected final static String BY_NAME_AND_EMAIL = "BY_NAME_AND_EMAIL";

  @Id
  private Long id;
  private String name;
  private String email;

  @OneToMany(fetch = FetchType.EAGER)
  @JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ID")
  private Set<Address> addresses;

  public User() {

  }

  public User(Long id, String name, String email) {
    this.id = id;
    this.name = name;
    this.email = email;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Set<Address> getAddresses() {
    if (addresses == null) {
      addresses = new HashSet<>();
    }
    return addresses;
  }

  public void setAddresses(Set<Address> addresses) {
    this.addresses = addresses;
  }

  @Override
  public String toString() {
    return "User{" + "id=" + id + ", name='" + name + '\'' + ", email='" + email + '\'' + ", addresses=" + addresses.size() + '}';
  }
}