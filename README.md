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

## Notes sur l'implémentation
- Le secret et la durée du token sont lus depuis `application.properties` (`jwt.secret`, `jwt.expiration`).
  Le secret peut être surchargé par la variable d'environnement `JWT_SECRET`.
- Le filtre valide le token (signature + expiration) avant de lire le nom d'utilisateur, et ignore les comptes désactivés.
- Un `PasswordEncoder` BCrypt est déclaré : Spring Security l'utilise avec `CustomUserDetailsService` pour vérifier les mots de passe au login.
- CSRF est désactivé et les sessions sont `STATELESS`, comme attendu pour une API REST avec JWT.
