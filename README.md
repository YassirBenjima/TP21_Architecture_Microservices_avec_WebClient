# TP 21 : Architecture Micro-services avec WebClient

## 📋 Description

Ce projet implémente une architecture microservices utilisant Spring Boot, Eureka Server pour la découverte de services, et WebClient pour la communication inter-services. Le projet comprend trois services principaux :

- **Eureka Server** : Serveur de découverte de services
- **Service-Client** : Microservice de gestion des clients
- **Service-Car** : Microservice de gestion des voitures avec enrichissement de données

## 🏗️ Architecture

```
┌─────────────────┐
│  Eureka Server   │ (Port 8761)
│  (Discovery)     │
└────────┬─────────┘
         │
    ┌────┴────┐
    │         │
┌───▼───┐ ┌──▼────┐
│Client │ │  Car  │
│:8081  │ │:8082  │
└───┬───┘ └───┬───┘
    │         │
    │         │ WebClient
    │         │ (LoadBalanced)
    └─────────┘
```

### Services

1. **Eureka Server** (Port 8761)

   - Serveur de découverte de services
   - Dashboard disponible sur : http://localhost:8761

2. **Service-Client** (Port 8081)

   - Gestion des clients
   - Base de données : `clientservicedb` (MySQL port 3308)
   - Enregistré dans Eureka sous le nom : `SERVICE-CLIENT`

3. **Service-Car** (Port 8082)
   - Gestion des voitures
   - Base de données : `carservicedb` (MySQL port 3308)
   - Enregistré dans Eureka sous le nom : `SERVICE-CAR`
   - Utilise WebClient pour appeler Service-Client et enrichir les données

## 🛠️ Technologies Utilisées

- **Spring Boot** 4.0.1
- **Spring Cloud** 2025.1.0
- **Spring Data JPA**
- **Spring WebFlux** (WebClient)
- **Eureka Server/Client** (Netflix)
- **MySQL** (Driver : mysql-connector-j)
- **Maven**
- **Java** 21

## 📦 Prérequis

- Java 21 ou supérieur
- Maven 3.6+
- MySQL Server (port 3308)
- IDE (IntelliJ IDEA, Eclipse, VS Code)

## 🚀 Installation et Configuration

### 1. Configuration MySQL

Assurez-vous que MySQL est en cours d'exécution sur le port **3308** avec :

- Username : `root`
- Password : (vide par défaut, modifiez dans `application.yml` si nécessaire)

Les bases de données seront créées automatiquement lors du premier démarrage grâce à `createDatabaseIfNotExist=true`.

### 2. Cloner/Préparer le Projet

Le projet contient trois modules Maven :

```
TP21_Architecture_Microservices_avec_WebClient/
├── eureka-server/
├── service-client/
└── service-car/
```

### 3. Configuration des Services

#### Eureka Server

- **Port** : 8761
- **Fichier** : `eureka-server/src/main/resources/application.yml`
- Aucune configuration supplémentaire requise

#### Service-Client

- **Port** : 8081
- **Fichier** : `service-client/src/main/resources/application.yml`
- **Base de données** : `clientservicedb` (MySQL port 3308)

#### Service-Car

- **Port** : 8082
- **Fichier** : `service-car/src/main/resources/application.yml`
- **Base de données** : `carservicedb` (MySQL port 3308)

## ▶️ Démarrage des Services

**Important** : Démarrez les services dans l'ordre suivant :

### 1. Démarrer Eureka Server

```bash
cd eureka-server
mvn spring-boot:run
```

Vérifier : http://localhost:8761

### 2. Démarrer Service-Client

```bash
cd service-client
mvn spring-boot:run
```

Vérifier dans Eureka Dashboard : `SERVICE-CLIENT` doit apparaître dans "Instances currently registered with Eureka"

### 3. Démarrer Service-Car

```bash
cd service-car
mvn spring-boot:run
```

Vérifier dans Eureka Dashboard : `SERVICE-CAR` doit apparaître dans "Instances currently registered with Eureka"

## 📡 API Endpoints

### Service-Client (Port 8081)

#### Créer un client

```http
POST http://localhost:8081/api/clients
Content-Type: application/json

{
  "nom": "Ahmed",
  "age": 30
}
```
<img width="1497" height="689" alt="Screenshot 2026-01-03 000005" src="https://github.com/user-attachments/assets/c63e2722-05a0-4ce9-906f-3a678fdd9668" />

#### Récupérer tous les clients

```http
GET http://localhost:8081/api/clients
```
<img width="1540" height="1010" alt="Screenshot 2026-01-03 000025" src="https://github.com/user-attachments/assets/cc1068d5-7a82-4496-bea5-06f468889565" />

#### Récupérer un client par ID

```http
GET http://localhost:8081/api/clients/{id}
```
<img width="1502" height="1024" alt="Screenshot 2026-01-03 000111" src="https://github.com/user-attachments/assets/b2626d28-f850-4f6d-881d-66cf507329be" />

### Service-Car (Port 8082)

#### Créer une voiture

```http
POST http://localhost:8082/api/cars
Content-Type: application/json

{
  "marque": "Toyota",
  "modele": "Corolla",
  "clientId": 1
}
```

#### Récupérer toutes les voitures (avec enrichissement client)

```http
GET http://localhost:8082/api/cars
```
<img width="1523" height="1027" alt="Screenshot 2026-01-03 000153" src="https://github.com/user-attachments/assets/d0156311-f6a6-45eb-8709-82da779f7242" />

#### Récupérer les voitures d'un client

```http
GET http://localhost:8082/api/cars/byClient/{clientId}
```

#### Test WebClient (endpoint de test)

```http
GET http://localhost:8082/api/test/client/{id}
```

## 🧪 Tests Pratiques des Endpoints

Cette section présente un scénario de test complet pour valider le fonctionnement de l'architecture microservices avec enrichissement de données.

### Scénario de Test Complet

#### Étape 1 : Créer un client

**Requête :**

```http
POST http://localhost:8081/api/clients
Content-Type: application/json

{
  "nom": "Salma",
  "age": 22
}
```

**Réponse attendue :**

```json
{
  "id": 1,
  "nom": "Salma",
  "age": 22.0
}
```

> **Note** : Noter l'`id` retourné (ex: `1`) pour l'utiliser dans les étapes suivantes.

#### Étape 2 : Vérifier la création du client

**Requête :**

```http
GET http://localhost:8081/api/clients
```

**Réponse attendue :**

```json
[
  {
    "id": 1,
    "nom": "Salma",
    "age": 22.0
  }
]
```

#### Étape 3 : Créer une voiture liée au client

**Requête :**

```http
POST http://localhost:8082/api/cars
Content-Type: application/json

{
  "marque": "Toyota",
  "modele": "Yaris",
  "clientId": 1
}
```

**Réponse attendue :**

```json
{
  "id": 1,
  "marque": "Toyota",
  "modele": "Yaris",
  "clientId": 1,
  "client": null
}
```

> **Note** : Le champ `client` est `null` lors de la création car l'enrichissement se fait uniquement lors de la lecture.

#### Étape 4 : Lire les voitures enrichies

**Requête :**

```http
GET http://localhost:8082/api/cars
```

**Réponse attendue :**

```json
[
  {
    "id": 1,
    "marque": "Toyota",
    "modele": "Yaris",
    "clientId": 1,
    "client": {
      "id": 1,
      "nom": "Salma",
      "age": 22.0
    }
  }
]
```

> **✅ Validation** : La réponse contient l'objet `client` enrichi avec les données récupérées depuis `SERVICE-CLIENT` via WebClient. Cela démontre le pattern d'enrichissement inter-services.

### Explication du Résultat

Dans la réponse de l'étape 4, on observe que :

- Les données de la voiture (`id`, `marque`, `modele`, `clientId`) proviennent de la base de données locale `carservicedb`
- L'objet `client` complet est enrichi via un appel HTTP à `SERVICE-CLIENT` utilisant WebClient avec `@LoadBalanced`
- Le nom de service `SERVICE-CLIENT` est résolu automatiquement par Eureka

### Tests avec cURL (Alternative)

Si vous préférez utiliser cURL en ligne de commande :

```bash
# Étape 1 : Créer un client
curl -X POST http://localhost:8081/api/clients \
  -H "Content-Type: application/json" \
  -d '{"nom": "Salma", "age": 22}'

# Étape 2 : Récupérer tous les clients
curl http://localhost:8081/api/clients

# Étape 3 : Créer une voiture
curl -X POST http://localhost:8082/api/cars \
  -H "Content-Type: application/json" \
  -d '{"marque": "Toyota", "modele": "Yaris", "clientId": 1}'

# Étape 4 : Récupérer toutes les voitures enrichies
curl http://localhost:8082/api/cars
```

## ✅ Tests et Validation

### Validation 1 : Eureka Server

1. Accéder à http://localhost:8761
2. Vérifier que le dashboard Eureka s'affiche correctement

### Validation 2 : Enregistrement Service-Client dans Eureka

1. Démarrer service-client
2. Vérifier dans le dashboard Eureka : `SERVICE-CLIENT` apparaît dans "Instances currently registered"

### Validation 3 : API Client

1. Créer un client :
   ```bash
   POST http://localhost:8081/api/clients
   Body: {"nom": "Ahmed", "age": 30}
   ```
2. Récupérer tous les clients :
   ```bash
   GET http://localhost:8081/api/clients
   ```
3. Vérifier que le client est retourné avec un `id` généré

### Validation 4 : Enregistrement Service-Car dans Eureka

1. Démarrer service-car
2. Vérifier dans le dashboard Eureka : `SERVICE-CAR` apparaît dans "Instances currently registered"
3. Les deux services (`SERVICE-CLIENT` et `SERVICE-CAR`) doivent être visibles

### Validation 5 : Test WebClient

1. Créer un client dans service-client (récupérer son `id`, ex: 1)
2. Tester WebClient :
   ```bash
   GET http://localhost:8082/api/test/client/1
   ```
3. **Résultat attendu** : JSON du client depuis service-client

### Validation 6 : Enrichissement de données

1. Créer une voiture avec un `clientId` existant :
   ```bash
   POST http://localhost:8082/api/cars
   Body: {"marque": "Toyota", "modele": "Corolla", "clientId": 1}
   ```
2. Récupérer toutes les voitures :
   ```bash
   GET http://localhost:8082/api/cars
   ```
3. **Résultat attendu** : La voiture retournée contient l'objet `client` enrichi

## 🔍 Structure des Entités

### Client (Service-Client)

```java
- id: Long
- nom: String
- age: Float
```

### Car (Service-Car)

```java
- id: Long
- marque: String
- modele: String
- clientId: Long (référence vers client)
- client: Client (transient, enrichi via WebClient)
```

## 🔧 Dépannage

### Erreur : "No instances available for SERVICE-CLIENT"

- Vérifier que `@LoadBalanced` est présent sur `WebClient.Builder`
- Vérifier que la dépendance `spring-cloud-starter-loadbalancer` est dans `pom.xml`
- Vérifier que `SERVICE-CLIENT` est bien enregistré dans Eureka

### Erreur : "404 Not Found" sur les endpoints

- Vérifier que le service est démarré
- Vérifier le port dans `application.yml`
- Vérifier le chemin de l'endpoint (`/api/clients` ou `/api/cars`)

### Erreur de connexion MySQL

- Vérifier que MySQL est en cours d'exécution
- Vérifier le port MySQL (3308)
- Vérifier `username` et `password` dans `application.yml`
- Vérifier que l'utilisateur MySQL a les droits de création de base de données

### Service non visible dans Eureka

- Vérifier que `spring.application.name` est correct dans `application.yml`
- Vérifier que `defaultZone` pointe vers `http://localhost:8761/eureka`
- Vérifier que la dépendance `spring-cloud-starter-netflix-eureka-client` est présente
- Attendre quelques secondes pour l'enregistrement

## 📝 Notes Importantes

1. **Pattern d'enrichissement** : Service-Car récupère d'abord les données de sa base locale, puis appelle Service-Client via WebClient pour enrichir la réponse JSON.

2. **@LoadBalanced** : Essentiel pour que WebClient puisse résoudre le nom de service `SERVICE-CLIENT` via Eureka.

3. **Bases de données séparées** : Chaque microservice a sa propre base de données (microservices → DB séparées).

4. **@Transient** : Le champ `client` dans l'entité `Car` est annoté `@Transient` car il n'est pas persisté en base, mais utilisé uniquement pour l'enrichissement JSON.

## 👥 Auteur

TP réalisé dans le cadre du cours d'Architecture Microservices.

## 📄 Licence

Ce projet est un travail pédagogique.
