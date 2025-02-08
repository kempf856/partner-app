package hu.fourig.partner.service;

import hu.fourig.partner.entity.Role;
import hu.fourig.partner.entity.User;
import hu.fourig.partner.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initUsers() {
        createDefaultUser("admin");
        createDefaultUser("user");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private void createDefaultUser(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            return;
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(username + "password"))
                .role(username.equalsIgnoreCase(Role.ADMIN.name()) ? Role.ADMIN : Role.USER)
                .build();
        userRepository.save(user);
    }
}