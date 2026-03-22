package com.bellator.bellator_barbearia.auth;

import com.bellator.bellator_barbearia.dto.UsuarioResponse;
import com.bellator.bellator_barbearia.exception.ApiException;
import com.bellator.bellator_barbearia.model.Usuarios;
import com.bellator.bellator_barbearia.repository.UsuarioRepository;
import com.bellator.bellator_barbearia.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepo, PasswordEncoder encoder,
                       AuthenticationManager authManager, JwtService jwtService) {
        this.usuarioRepo = usuarioRepo;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    public AuthResponse register(AuthRegisterRequest req) {
        if (usuarioRepo.existsByEmail(req.email)) {
            throw new ApiException(HttpStatus.CONFLICT, "Email já cadastrado");
        }
        Usuarios u = new Usuarios();
        u.setNome(req.nome);
        u.setEmail(req.email.toLowerCase());
        u.setTelefone(req.telefone);
        u.setSenhaHash(encoder.encode(req.senha));
        u.setRole(req.role != null ? req.role : com.bellator.bellator_barbearia.role.Role.CLIENTE); // valor padrão para role
        u = usuarioRepo.save(u);

        String token = jwtService.generate(u.getEmail(), Map.of(
                "role", u.getRole().name(),
                "uid", u.getId()
        ));

        return new AuthResponse(token, u.getRole(), new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(),u.getTelefone(), u.getRole()));
    }

    public AuthResponse login(AuthRequest req) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail().toLowerCase(), req.getSenha()));
        } catch (Exception ex) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }

        Usuarios u = usuarioRepo.findByEmail(req.getEmail().toLowerCase())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));

        String token = jwtService.generate(u.getEmail(), Map.of(
                "role", u.getRole().name(),
                "uid", u.getId()
        ));

        return new AuthResponse(token, u.getRole(), new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(),u.getTelefone(), u.getRole()));
    }
}
