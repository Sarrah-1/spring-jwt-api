# TP 12 – API REST sécurisée avec JWT

Projet Spring Boot 3.3 / Java 17 : API REST stateless, authentification par token JWT (jjwt 0.11.5),
utilisateurs et rôles en base MySQL, mots de passe hachés avec BCrypt.

## Prérequis
- Java 17+
- MySQL (ou MariaDB) démarré sur `localhost:3306`, utilisateur `root` sans mot de passe
  (à adapter dans `src/main/resources/application.properties`)
- La base `security_jwt` est créée automatiquement.

## Lancement
Depuis IntelliJ : exécuter `SpringJwtApiApplication`.
Ou en ligne de commande (si Maven est installé) : `mvn spring-boot:run`

## Comptes créés au démarrage
| Utilisateur | Mot de passe | Rôles                 |
|-------------|--------------|-----------------------|
| admin       | 1234         | ROLE_ADMIN, ROLE_USER |
| user        | 1111         | ROLE_USER             |

## Endpoints
| Méthode | URL                    | Accès            |
|---------|------------------------|------------------|
| POST    | `/api/auth/login`      | Public           |
| GET     | `/api/user/profile`    | USER ou ADMIN    |
| GET     | `/api/admin/dashboard` | ADMIN            |
| GET     | `/api/secure/hello`    | Tout utilisateur authentifié |

## Tests
Le fichier `requests.http` (client HTTP d'IntelliJ) contient tous les scénarios. Avec Postman ou curl :

```bash
# 1. Obtenir un token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"1234"}'

# 2. Appeler un endpoint protégé (remplacer <TOKEN>)
curl http://localhost:8080/api/user/profile -H "Authorization: Bearer <TOKEN>"
```

Résultats attendus :
- login correct : 200 + `{"token": "...", "username": "admin", "type": "Bearer"}`
- login incorrect : 401
- sans token ou token invalide : 403 `{"error": "Forbidden", "message": "Access Denied"}`
- token `user` sur `/api/admin/**` : 403

Astuce : colle un token sur https://jwt.io pour voir le header, le payload (`sub`, `iat`, `exp`) et la signature.

## Structure
```
src/main/java/ma/ens/security
├── SpringJwtApiApplication.java
├── config/         SecurityConfig, DatabaseInitializer
├── jwt/            JwtUtil, JwtAuthorizationFilter
├── entities/       User, Role
├── repositories/   UserRepository, RoleRepository
├── services/       CustomUserDetailsService
└── web/            AuthController, TestController
```


## Screen

<img width="869" height="343" alt="image" src="https://github.com/user-attachments/assets/d6532360-7a02-4665-91b3-67c3299635b7" />

<img width="526" height="170" alt="image" src="https://github.com/user-attachments/assets/77a22f26-0449-468f-b1ee-8303b6cf202c" />


