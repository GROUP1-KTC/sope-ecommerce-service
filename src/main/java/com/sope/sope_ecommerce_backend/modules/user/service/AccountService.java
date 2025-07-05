package com.sope.sope_ecommerce_backend.modules.user.service;


import com.sope.sope_ecommerce_backend.modules.user.repository.AccountRepository;

import com.sope.sope_ecommerce_backend.modules.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OurUserDetailsService ourUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;



}
