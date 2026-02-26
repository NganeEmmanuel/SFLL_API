package com.socialapp.sfll.UserService.dto;

import com.socialapp.sfll.UserService.annotation.NotContainsSpecialChar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequest {
    @NonNull
    private Integer id;
    @NotContainsSpecialChar
    private String firstName;
    @NotContainsSpecialChar
    private String lastName;
}
