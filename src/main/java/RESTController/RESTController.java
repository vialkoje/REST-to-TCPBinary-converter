package hello;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
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
	private final MeasurementRepository repository;

	MeasurementController(MeasurementRepository repository) {
		this.repository = repository;
	}

	// Aggregate root

	@GetMapping("/api/measurements")
	List<Measurement> all() {
		return repository.findAll();
	}

	@PostMapping("/api/measurement")
	Measurement newMeasurement(@RequestBody Measurement newMeasurement) {
		return repository.save(newMeasurement);
	}

  @RequestMapping("/api/temperatures/current")
  public Temperatures current() {
      String Host="192.168.3.11";
      String ApplicationToken="83b9926dcf6d3d4a8f5928b44b0904d0763507fe117e36052fedc0afbc817869";
      int Port = 8080;

//    https://192.168.3.11:8080/json/system/loginApplication?loginToken=83b9926dcf6d3d4a8f5928b44b0904d0763507fe117e36052fedc0afbc817869
//    https://192.168.3.11:8080/json/apartment/getSensorValues
    /*  byte number[] = new byte[2];
      byte rawinput[] = new byte[10];
      byte [] Delivery_Conf = {(byte)0x01, (byte)0x03, (byte)0x00, (byte)0x04,
                               (byte)0x00, (byte)0x01, (byte)0xc5, (byte)0xcb};
      short num=0;

      try {
        InetAddress host = InetAddress.getByName(Host);
        clientSocket = new Socket(host, Port);
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
        num = (short)( (rawinput[3] & 0xFF) << 8 | (rawinput[4]&0xFF));
        //System.out.println(String.format("%d %s %s",num,String.format("%02X", rawinput[3]),String.format("%02X", rawinput[4])));

        dIn.close();
        dOut.close();
        clientSocket.close();
      }
      catch(IOException e){
        e.printStackTrace();
      }
      Measurement measure = new Measurement ("level",Short.toString(num),num/(size/100));*/
      return measure;
  }

  @RequestMapping("/api/measurement/current")
  public Measurement current() {
    // 5,8 cm Volumen, max Höhe 2600-620=1980mm
      String Host="192.168.3.207";
      int Port = 25000;
      double size=1742;

      byte number[] = new byte[2];
      byte rawinput[] = new byte[10];
      byte [] Delivery_Conf = {(byte)0x01, (byte)0x03, (byte)0x00, (byte)0x04,
                               (byte)0x00, (byte)0x01, (byte)0xc5, (byte)0xcb};
      short num=0;

      try {
        InetAddress host = InetAddress.getByName(Host);
        clientSocket = new Socket(host, Port);
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
        num = (short)( (rawinput[3] & 0xFF) << 8 | (rawinput[4]&0xFF));
        //System.out.println(String.format("%d %s %s",num,String.format("%02X", rawinput[3]),String.format("%02X", rawinput[4])));

        dIn.close();
        dOut.close();
        clientSocket.close();
      }
      catch(IOException e){
        e.printStackTrace();
      }
      Measurement measure = new Measurement ("level",Short.toString(num),num/(size/100));
      return measure;
  }

	// Single item

	@GetMapping("/api/measurement/{id}")
	Measurement one(@PathVariable Long id) {

		return repository.findById(id)
    			.orElseThrow(() -> new MeasurementNotFoundException(id));
	}

	@PutMapping("/api/measurements/{id}")
	Measurement replaceMeasurement(@RequestBody Measurement newMeasurement, @PathVariable Long id) {

		return repository.findById(id)
			.map(measurement -> {
				measurement.setName(newMeasurement.getName());
				measurement.setValue(newMeasurement.getValue());
				return repository.save(measurement);
			})
			.orElseGet(() -> {
				newMeasurement.setId(id);
				return repository.save(newMeasurement);
			});
	}

	@DeleteMapping("/api/measurements/{id}")
	void deleteMeasurement(@PathVariable Long id) {
		repository.deleteById(id);
	}
}
