package com.learning.main.model;

import java.util.Set;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;

// This class represents a core business entity, holding data and relationships relevant to sponsors.
// Data Mapper Pattern (via JPA annotations)
// JPA annotations are used to map this object to the 'sponsors' table in the database.

@Entity
@Table(name = "sponsors")
public class Sponsor {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long sponsorId;

	    private String sponsorName;
	    private String industry;
	    private String contactPerson;
	    private String contactEmail;
	    private String phone; // Renamed from contactPhone to align with controller
	    private String website; // Added missing field
	    private String logoUrl;

	    @OneToMany(mappedBy = "sponsor")
	    private Set<Sponsorship> sponsorships;

	    // Getters and Setters

	    public Long getSponsorId() {
	        return sponsorId;
	    }

	    public void setSponsorId(Long sponsorId) {
	        this.sponsorId = sponsorId;
	    }

	    public String getSponsorName() {
	        return sponsorName;
	    }

	    public void setSponsorName(String sponsorName) {
	        this.sponsorName = sponsorName;
	    }

	    public String getIndustry() {
	        return industry;
	    }

	    public void setIndustry(String industry) {
	        this.industry = industry;
	    }

	    public String getContactPerson() {
	        return contactPerson;
	    }

	    public void setContactPerson(String contactPerson) {
	        this.contactPerson = contactPerson;
	    }

	    public String getContactEmail() {
	        return contactEmail;
	    }

	    public void setContactEmail(String contactEmail) {
	        this.contactEmail = contactEmail;
	    }

	    // New/Updated Getter and Setter for phone
	    public String getPhone() {
	        return phone;
	    }

	    public void setPhone(String phone) {
	        this.phone = phone;
	    }

	    // New Getter and Setter for website
	    public String getWebsite() {
	        return website;
	    }

	    public void setWebsite(String website) {
	        this.website = website;
	    }

	    public String getLogoUrl() {
	        return logoUrl;
	    }

	    public void setLogoUrl(String logoUrl) {
	        this.logoUrl = logoUrl;
	    }

	    public Set<Sponsorship> getSponsorships() {
	        return sponsorships;
	    }

	    public void setSponsorships(Set<Sponsorship> sponsorships) {
	        this.sponsorships = sponsorships;
	    }
	}