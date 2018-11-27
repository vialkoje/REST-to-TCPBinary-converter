package hello;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.*;
import java.nio.ByteBuffer;
import java.net.*;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class MeasurementController {
  private Socket clientSocket;
  private PrintWriter out;
  private BufferedReader in;
	private final MeasurementRepository repository;

	MeasurementController(MeasurementRepository repository) {
		this.repository = repository;
	}

	// Aggregate root

	@GetMapping("/measurements")
	List<Measurement> all() {
		return repository.findAll();
	}

	@PostMapping("/measurement")
	Measurement newMeasurement(@RequestBody Measurement newMeasurement) {
		return repository.save(newMeasurement);
	}

  @RequestMapping("/measurement/current")
  public Measurement current() {
      String resp ="empty";
      byte number[] = new byte[2];
      byte rawinput[] = new byte[10];
      byte [] Delivery_Conf = {(byte)0x01, (byte)0x03, (byte)0x00, (byte)0x04,
                               (byte)0x00, (byte)0x01, (byte)0xc5, (byte)0xcb};

      try {
        InetAddress host = InetAddress.getByName("192.168.3.207");
        clientSocket = new Socket(host, 25000);
      }
      catch(UnknownHostException ex) {
        ex.printStackTrace();
      }
      catch(IOException e){
        e.printStackTrace();
      }
      try {
        DataInputStream dIn = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream dOut = new DataOutputStream(clientSocket.getOutputStream());
        dOut.write(Delivery_Conf);

        dIn.read(rawinput);
        number[0] = rawinput[3];
        number[1] = rawinput[4];
        ByteBuffer wrapped = ByteBuffer.wrap(number); // big-endian by default
        short num = wrapped.getShort(); // 1
        resp = String.format("%d",num);
        dIn.close();
        dOut.close();
        clientSocket.close();
      }
      catch(IOException e){
        e.printStackTrace();
      }
      Measurement measure = new Measurement ("level",resp);
      return measure;
  }

	// Single item
/*
	@GetMapping("/measurement/{id}")
	Measurement one(@PathVariable Long id) {

		return repository.findById(id);
    			.orElseThrow(() -> new MeasurementNotFoundException(id));
	}

	@PutMapping("/measurements/{id}")
	Measurement replaceMeasurement(@RequestBody Measurement newMeasurement, @PathVariable Long id) {

		return repository.findById(id);
			.map(measurement -> {
				measurement.setKey(newMeasurement.getKey());
				measurement.setValue(newMeasurement.getValue());
				return repository.save(measurement);
			})
			.orElseGet(() -> {
				newMeasurement.setId(id);
				return repository.save(newMeasurement);
			});
	}
*/
	@DeleteMapping("/measurements/{id}")
	void deleteMeasurement(@PathVariable Long id) {
		repository.deleteById(id);
	}
}
