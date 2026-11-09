package ma.ens.security.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {

    // Accessible aux rôles USER et ADMIN
    @GetMapping("/user/profile")
    public Map<String, Object> profile(Authentication authentication) {
        return Map.of(
                "username", authentication.getName(),
                "roles", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );
    }

    // Accessible uniquement au rôle ADMIN
    @GetMapping("/admin/dashboard")
    public Map<String, Object> adminDashboard(Authentication authentication) {
        return Map.of(
                "message", "Bienvenue dans l'espace administrateur",
                "username", authentication.getName()
        );
    }

    // Toute requête authentifiée (anyRequest().authenticated())
    @GetMapping("/secure/hello")
    public Map<String, String> hello(Authentication authentication) {
        return Map.of("message", "Bonjour " + authentication.getName() + ", votre token est valide");
    }
}
