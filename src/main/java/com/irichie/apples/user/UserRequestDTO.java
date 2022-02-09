package com.irichie.apples.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class UserRequestDTO {

    @JsonProperty
    private String username;

    @JsonProperty
    private String password;

    @JsonProperty
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRequestDTO)) return false;
        return ((UserRequestDTO) o).getUsername() != null
                && ((UserRequestDTO) o).getPassword() != null
                && ((UserRequestDTO) o).getConfirmPassword() != null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword(), getConfirmPassword());
    }
}
