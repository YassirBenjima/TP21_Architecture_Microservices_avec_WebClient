package ma.projet.service_car.web;

import ma.projet.service_car.entities.Client;
import ma.projet.service_car.services.ClientApi;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final ClientApi clientApi;

    public TestController(ClientApi clientApi) {
        this.clientApi = clientApi;
    }

    @GetMapping("/client/{id}")
    public Client testClient(@PathVariable Long id) {
        return clientApi.findClientById(id);
    }
}

