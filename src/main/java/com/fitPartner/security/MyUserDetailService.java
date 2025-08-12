package com.fitPartner.security;

import com.fitPartner.entity.user.MyUser;
import com.fitPartner.repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class MyUserDetailService implements UserDetailsService {

    private final MyUserRepository repo;

    @Autowired
    public MyUserDetailService(MyUserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MyUser user = repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new User(user.getEmail(),user.getPassword(),List.of(new SimpleGrantedAuthority(user.getRole())));
    }
}
