package hello;

import org.springframework.data.jpa.repository.JpaRepository;

interface MeasurementRepository extends JpaRepository<Measurement, Long> {

}
