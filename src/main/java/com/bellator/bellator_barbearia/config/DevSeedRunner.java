package com.bellator.bellator_barbearia.config;

import com.bellator.bellator_barbearia.role.Role;
import com.bellator.bellator_barbearia.model.Servicos;
import com.bellator.bellator_barbearia.model.Usuarios;
import com.bellator.bellator_barbearia.repository.ServicoRepository;
import com.bellator.bellator_barbearia.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DevSeedRunner implements CommandLineRunner {

    private final UsuarioRepository usuarioRepo;
    private final ServicoRepository servicoRepo;
    private final PasswordEncoder encoder;

    public DevSeedRunner(UsuarioRepository usuarioRepo, ServicoRepository servicoRepo, PasswordEncoder encoder) {
        this.usuarioRepo = usuarioRepo;
        this.servicoRepo = servicoRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        seedUser("Admin", "admin@bellator.dev", "123456", Role.ADMIN);
        seedUser("Carlos", "barber@bellator.dev", "123456", Role.BARBEIRO);
        seedUser("Cliente", "client@bellator.dev", "123456", Role.CLIENTE);

        seedServico("Corte Tradicional", 30.0, 30);
        seedServico("Corte + Barba", 45.0, 45);
        seedServico("Barba Completa", 25.0, 25);
        seedServico("Acabamento", 15.0, 15);
    }

    private void seedUser(String nome, String email, String senha, Role role) {
        if (usuarioRepo.existsByEmail(email)) return;
        Usuarios u = new Usuarios();
        u.setNome(nome);
        u.setEmail(email);
        u.setSenhaHash(encoder.encode(senha));
        u.setRole(role);
        usuarioRepo.save(u);
    }

    private void seedServico(String nome, double preco, int duracao) {
        boolean exists = servicoRepo.findAll().stream().anyMatch(s -> s.getNome().equalsIgnoreCase(nome));
        if (exists) return;
        Servicos s = new Servicos();
        s.setNome(nome);
        s.setPreco(preco);
        s.setDuracaoMinutos(duracao);
        servicoRepo.save(s);
    }
}
