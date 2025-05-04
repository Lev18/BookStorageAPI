package com.bookstore.users.security;

import com.bookstore.users.entity.User;
import com.bookstore.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user  = userRepository.findByEmail(username)
                .orElseThrow(()->new BadCredentialsException("Bad credentials"));

        if (!user.isEnabled()) {
            throw  new LockedException("User is locked");
        }
        return  user;
    }
}
