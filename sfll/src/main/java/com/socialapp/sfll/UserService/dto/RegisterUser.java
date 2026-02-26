package com.socialapp.sfll.UserService.dto;

import com.socialapp.sfll.UserService.annotation.NotContainsSpecialChar;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisterUser {
    @NonNull
    @NotContainsSpecialChar
    private String firstName;

    @NonNull
    @NotContainsSpecialChar
    private String lastName;

    @NonNull
    @NotContainsSpecialChar
    private String username;

    @NonNull
    @NotContainsSpecialChar
    private String email;

    @NonNull
    private String password;
}
