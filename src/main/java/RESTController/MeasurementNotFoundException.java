package hello;

class MeasurementNotFoundException extends RuntimeException {

	MeasurementNotFoundException(Long id) {
		super("Could not find measurement " + id);
	}
}
