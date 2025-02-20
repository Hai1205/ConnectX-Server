package com.Server.service.api;

import com.Server.dto.*;
import com.Server.entity.*;
import com.Server.exception.OurException;
import com.Server.repo.*;
import com.Server.service.EmailService;
import com.Server.utils.JWTUtils;
import com.Server.utils.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthApi {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public Response register(Request request) {
        Response response = new Response();

        try {
            String username = request.getUsername();
            String email = request.getEmail();
            String password = request.getPassword();
            String fullName = request.getFullName();

            if (userRepository.existsByUsername(username)) {
                throw new OurException("Username Already Exists");
            }

            if (userRepository.existsByEmail(email)) {
                throw new OurException("Email Already Exists");
            }

            User user = new User(username, fullName, email);
            user.setPassword(passwordEncoder.encode(password));

            User savedUser = userRepository.save(user);
            UserDTO userDTO = UserMapper.mapEntityToDTOFull(savedUser);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDTO);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
            System.out.println(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            System.out.println(e.getMessage());
        }

        return response;
    }

    public Response login(Request request) {
        Response response = new Response();

        try {
            String username = request.getUsername();
            String password = request.getPassword();
            String userIdentifier = (username != null) ? username : request.getEmail();

            UserDetails userDetail = (username != null)
                    ? userRepository.findByUsername(username).orElseThrow(() -> new OurException("User not found"))
                    : userRepository.findByEmail(userIdentifier).orElseThrow(() -> new OurException("User not found"));

            User user = userRepository.findByUsername(username).orElseThrow(() -> new OurException("User not found"));
            UserDTO userDTO = UserMapper.mapEntityToDTOFull(user);

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDetail.getUsername(), password));
            var token = jwtUtils.generateToken(userDetail);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setToken(token);
            response.setUser(userDTO);
            response.setExpirationTime("7 days");
        } catch (BadCredentialsException e) {
            response.setStatusCode(401);
            response.setMessage(e.getMessage());
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
            System.out.println(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            System.out.println(e.getMessage());
        }

        return response;
    }
}
