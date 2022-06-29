package es.udc.ws.app.restservice.dto;

public class RestExcursionDto {

	private Long excursionId;
	private String city;
	private String description;
	private String date;
	private float price;
	private int maxParticipants;
	private int numFree;

	public RestExcursionDto(Long excursionId, String city, String description, String date, float price, int maxParticipants, int numFree) {
		this.excursionId = excursionId;
		this.city = city;
		this.description = description;
		this.date = (date != null) ? date : null;
		this.price = price;
		this.maxParticipants=maxParticipants;
		this.numFree=numFree;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = (date != null) ? date : null;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
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

}