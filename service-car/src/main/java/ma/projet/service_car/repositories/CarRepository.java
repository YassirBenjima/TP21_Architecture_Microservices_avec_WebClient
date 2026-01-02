package ma.projet.service_car.repositories;

import ma.projet.service_car.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByClientId(Long clientId);
}

