package com.valework.yingul.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Yng_Item {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "itemId", nullable = false, updatable = false)
    private Long itemId;
	private double price;
	private String money;
	private String description;
	private String name;
	private String horario;
	private String time;

	private java.lang.String principalImage;
	private String video;
	private int quantity;
	
	private String type;
	
	@Value("${some.key:false}")
	private boolean isOver;
	
	@Value("${some.key:true}")
	private boolean enabled;
	
	private int dayPublication;
	private int monthPublication;
	private int yearPublication;
	private String productPagoEnvio;
	private String logisticsName;
	private String condition;
	private String internationalDeliveries="";
	private int kilometer;
	private int itemYear;
	private int duildedArea;
	private int ambientes;
	//nuevos datos para mas seguridad
	private String ip;
    private String org;
    private String lat;
    private String lon;
    private String city;
    private String country;
    private String countryCode;
    private String regionName;
    private String zip;
    private String userAgent;
  //nuevos datos para mas seguridad
	@OneToOne
	private Yng_Ubication yng_Ubication;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Yng_User user;

	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)  
	@JsonBackReference(value="itemCategory")
    private Set<Yng_ItemCategory> itemCategory = new HashSet<>();
	
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)  
    @JsonBackReference(value="itemImage")
    private Set<Yng_ItemImage> itemImage = new HashSet<>();
    private double priceNormal;
    private double priceDiscount;
    
    public Yng_Item() {
    	
    }
    
	public Yng_Item(String name) {
		super();
		this.name = name;
	}


	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHorario() {
		return horario;
	}
	public void setHorario(String horario) {
		this.horario = horario;
	}
	public Yng_User getUser() {
		return user;
	}
	public void setUser(Yng_User user) {
		this.user = user;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public Yng_Ubication getYng_Ubication() {
		return yng_Ubication;
	}
	public void setYng_Ubication(Yng_Ubication yng_Ubication) {
		this.yng_Ubication = yng_Ubication;
	}
	public Set<Yng_ItemCategory> getItemCategory() {
		return itemCategory;
	}
	public void setItemCategory(Set<Yng_ItemCategory> itemCategory) {
		this.itemCategory = itemCategory;
	}
	public Set<Yng_ItemImage> getItemImage() {
		return itemImage;
	}
	public void setItemImage(Set<Yng_ItemImage> itemImage) {
		this.itemImage = itemImage;
	}
	public java.lang.String getPrincipalImage() {
		return principalImage;
	}

	public void setPrincipalImage(java.lang.String principalImage) {
		this.principalImage = principalImage;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public double getPriceNormal() {
		return priceNormal;
	}

	public void setPriceNormal(double priceNormal) {
		this.priceNormal = priceNormal;
	}

	public double getPriceDiscount() {
		return priceDiscount;
	}

	public void setPriceDiscount(double priceDiscount) {
		this.priceDiscount = priceDiscount;
	}

	public boolean isOver() {
		return isOver;
	}

	public void setOver(boolean isOver) {
		this.isOver = isOver;
	}

	public int getDayPublication() {
		return dayPublication;
	}

	public void setDayPublication(int dayPublication) {
		this.dayPublication = dayPublication;
	}

	public int getMonthPublication() {
		return monthPublication;
	}

	public void setMonthPublication(int monthPublication) {
		this.monthPublication = monthPublication;
	}

	public int getYearPublication() {
		return yearPublication;
	}

	public void setYearPublication(int yearPublication) {
		this.yearPublication = yearPublication;
	}

	public String getProductPagoEnvio() {
		return productPagoEnvio;
	}

	public void setProductPagoEnvio(String productPagoEnvio) {
		this.productPagoEnvio = productPagoEnvio;
	}

	public String getLogisticsName() {
		return logisticsName;
	}

	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getInternationalDeliveries() {
		return internationalDeliveries;
	}

	public void setInternationalDeliveries(String internationalDeliveries) {
		this.internationalDeliveries = internationalDeliveries;
	}
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getKilometer() {
		return kilometer;
	}

	public void setKilometer(int kilometer) {
		this.kilometer = kilometer;
	}

	public int getItemYear() {
		return itemYear;
	}

	public void setItemYear(int itemYear) {
		this.itemYear = itemYear;
	}

	public int getDuildedArea() {
		return duildedArea;
	}

	public void setDuildedArea(int duildedArea) {
		this.duildedArea = duildedArea;
	}

	public int getAmbientes() {
		return ambientes;
	}

	public void setAmbientes(int ambientes) {
		this.ambientes = ambientes;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}	
	
}
