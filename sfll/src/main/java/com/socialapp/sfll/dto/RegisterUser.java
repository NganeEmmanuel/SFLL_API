package com.socialapp.sfll.dto;

import com.socialapp.sfll.annotation.NotContainsSpecialChar;
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
