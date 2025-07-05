package com.sope.sope_ecommerce_backend.modules.user.service;

import com.sope.sope_ecommerce_backend.modules.user.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OurUserDetailsService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("Account not found with email: " + username));
    }
}