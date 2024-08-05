package com.silu.controller;

import com.silu.config.JwtProvider;
import com.silu.model.Cart;
import com.silu.model.USER_ROLE;
import com.silu.model.User;
import com.silu.repository.CartRepository;
import com.silu.repository.UserRepository;
import com.silu.request.LoginRequest;
import com.silu.responce.AuthResponse;
import com.silu.service.CustomerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    private CartRepository cartRepository;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {
        User isEmailExits = userRepository.findByEmail(user.getEmail());

        if (isEmailExits != null) {
            throw new Exception("Email is already used in another account");
        }

        User createdUser = User.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .password(passwordEncoder.encode(user.getPassword()))
                .build();
        User savedUser = userRepository.save(createdUser);

        Cart cart = Cart.builder()
                .customer(savedUser).build();

        cartRepository.save(cart);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = AuthResponse.builder()
                .jwt(jwt)
                .message("Register Success")
                .role(savedUser.getRole())
                .build();

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }


    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signing(@RequestBody LoginRequest request) {

        String username = request.getEmail();
        String password = request.getPassword();

        Authentication authentication = authenticate(username,password);

        String jwt = jwtProvider.generateToken(authentication);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
        AuthResponse authResponse = AuthResponse.builder()
                .jwt(jwt)
                .message("Login" + " Success")
                .role(USER_ROLE.valueOf(role))
                .build();

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username");
        }
        if (!passwordEncoder.matches(password,userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

    }
}

