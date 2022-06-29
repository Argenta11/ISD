package es.udc.ws.app.model.excursion;

import java.time.LocalDateTime;
import java.util.Objects;

public class Excursion {

	private Long excursionId;
	private String city;
	private String description;
	private LocalDateTime date;
	private float price;
	private LocalDateTime creationDate;
	private int maxParticipants;
	private int numFree;

	public Excursion(String city, String description, float price, int maxParticipants, int numFree, LocalDateTime date) {
		this.city = city;
		this.description = description;
		this.price = price;
		this.maxParticipants=maxParticipants;
		this.numFree=numFree;
		this.date = (date != null) ? date.withNano(0) : null;
		this.creationDate=LocalDateTime.now();
	}

	public Excursion(Long excursionId, String city,String description, float price, int maxParticipants,int numFree, LocalDateTime date) {
		this(city, description, price, maxParticipants,numFree, date);
		this.excursionId = excursionId;
		this.creationDate=LocalDateTime.now();
	}

	public Excursion(Long excursionId, String city, String description, float price, int maxParticipants, LocalDateTime creationDate, int numFree, LocalDateTime date) {
		this(excursionId, city, description, price, maxParticipants,numFree, date);
		this.creationDate = (creationDate != null) ? creationDate.withNano(0) : null;
	}

	public Long getExcursionId() { return excursionId; }

	public void setExcursionId(Long id) {
		this.excursionId = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public LocalDateTime getCreationDate() {
		return creationDate.withNano(0);
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = (creationDate != null) ? creationDate.withNano(0) : null;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = (date != null) ? date.withNano(0) : null;
	}

	public int getMaxParticipants(){
		return maxParticipants;
	}

	public void setMaxParticipants(int maxParticipants){
		this.maxParticipants=maxParticipants;
	}

	public int getNumFree(){
		return numFree;
	}

	public void setNumFree(int numFree){
		this.numFree=numFree;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Excursion excursion = (Excursion) o;
		return Double.compare(excursion.price, price) == 0 &&
				maxParticipants == excursion.maxParticipants &&
				numFree == excursion.numFree &&
				Objects.equals(excursionId, excursion.excursionId) &&
				Objects.equals(city, excursion.city) &&
				Objects.equals(description, excursion.description) &&
				Objects.equals(date, excursion.date) &&
				Objects.equals(creationDate, excursion.creationDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(excursionId, city, description, date, price, creationDate, maxParticipants, numFree);
	}
}