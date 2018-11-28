package hello;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
class Measurement {

	private @Id @GeneratedValue Long id;
	private String name;
	private String value;
	private double percent;

	Measurement(String name, String value, double percent) {
		this.name = name;
		this.value = value;
		this.percent = percent;
	}
}
