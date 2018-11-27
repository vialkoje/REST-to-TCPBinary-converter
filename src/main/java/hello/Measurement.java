package hello;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
class Measurement {

	private @Id @GeneratedValue Long id;
	private String key;
	private String value;

	Measurement(String key, String value) {
		this.key = key;
		this.value = value;
	}
}
