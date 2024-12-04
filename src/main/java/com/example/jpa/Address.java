package com.example.jpa;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ADDRESS")
public class Address {
  @Id
  private Long id;
  private String street;
  private String number;
  private String city;

  public Address() {

  }

  public Address(Long id, String street, String number, String city) {
    this.id = id;
    this.street = street;
    this.number = number;
    this.city = city;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

}