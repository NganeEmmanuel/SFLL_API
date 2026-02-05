package com.socialapp.sfll.dto;

import com.socialapp.sfll.validator.NotContainsSpecialChar;
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
