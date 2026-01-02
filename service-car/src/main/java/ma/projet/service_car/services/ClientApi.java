package ma.projet.service_car.services;

import ma.projet.service_car.entities.Client;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ClientApi {
    private final WebClient.Builder builder;

    public ClientApi(WebClient.Builder builder) {
        this.builder = builder;
    }

    public Client findClientById(Long id) {
        return builder.build()
                .get()
                .uri("http://SERVICE-CLIENT/api/clients/" + id)
                .retrieve()
                .bodyToMono(Client.class)
                .block();
    }
}

