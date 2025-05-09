package com.bookstore.users.security;

import com.bookstore.users.entity.User;
import com.bookstore.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class CustomUserDetailService implements UserDetailsService {
    private Long id;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user  = userRepository.findByEmail(username)
                .orElseThrow(()->new BadCredentialsException("Bad credentials"));

        if (!user.isEnabled()) {
            throw  new LockedException("User is locked");
        }
        return  user;
    }
}
