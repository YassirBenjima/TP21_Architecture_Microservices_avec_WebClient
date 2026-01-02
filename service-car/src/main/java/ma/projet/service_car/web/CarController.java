package ma.projet.service_car.web;

import ma.projet.service_car.entities.Car;
import ma.projet.service_car.repositories.CarRepository;
import ma.projet.service_car.services.ClientApi;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarRepository repo;
    private final ClientApi clientApi;

    public CarController(CarRepository repo, ClientApi clientApi) {
        this.repo = repo;
        this.clientApi = clientApi;
    }

    @PostMapping
    public Car save(@RequestBody Car car) {
        // Sauvegarde uniquement la voiture (DB locale carservicedb)
        return repo.save(car);
    }

    @GetMapping
    public List<Car> findAll() {
        List<Car> cars = repo.findAll();

        // Enrichissement : pour chaque voiture, récupérer le client
        for (Car car : cars) {
            if (car.getClientId() != null) {
                car.setClient(clientApi.findClientById(car.getClientId()));
            }
        }
        return cars;
    }

    @GetMapping("/byClient/{clientId}")
    public List<Car> findByClient(@PathVariable Long clientId) {
        List<Car> cars = repo.findByClientId(clientId);

        // Même client pour toutes les voitures de ce clientId
        for (Car car : cars) {
            car.setClient(clientApi.findClientById(clientId));
        }
        return cars;
    }
}

