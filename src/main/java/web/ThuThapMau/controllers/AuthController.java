package web.ThuThapMau.controllers;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.ThuThapMau.Util.EmailService;
import web.ThuThapMau.dtos.EmailDto;
import web.ThuThapMau.dtos.PasswordDto;
import web.ThuThapMau.entities.User;
import web.ThuThapMau.services.UserService;

@RestController
@RequestMapping("/public")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user, HttpServletResponse response) {
        try {
            User existedUser = userService.login(user, response);
            if (existedUser != null) {
                return ResponseEntity.status(200).body(existedUser);
            } else {
                return ResponseEntity.status(400).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<User> CreateUser(@RequestBody User payload){
        try{
            System.out.println(payload);
            payload.setUser_image_url("https://as2.ftcdn.net/v2/jpg/04/10/43/77/1000_F_410437733_hdq4Q3QOH9uwh0mcqAhRFzOKfrCR24Ta.jpg");
            User newuser = userService.createUser(payload);
            return ResponseEntity.status(200).body(newuser);
        } catch (Exception e){
            return ResponseEntity.status(500).body(null);
        }
    }


    @PostMapping("/sendEmail/forget-password")
    public ResponseEntity<String> sendEmail(@RequestBody EmailDto emailDto) {
        try {
            System.out.println(emailDto);
            User user = userService.getUserByEmail(emailDto.getUser_email());
            emailService.sendEmail(emailDto.getUser_email(), user.getUser_id());
            return ResponseEntity.status(200).body("Gui mail thanh cong");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @PatchMapping("/reset-password")
    public  ResponseEntity<String> updatePassword(@RequestParam Long user_id, @RequestBody PasswordDto passwordDto){
        try{
            userService.updatePassword(user_id, passwordDto.getNewPassword());
            return  ResponseEntity.status(200).body("Cập nhật thành công");
        } catch (Exception e){
            return  ResponseEntity.status(200).body(null);
        }
    }
}
