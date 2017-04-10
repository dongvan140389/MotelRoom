package com.example.vonga.model;

import java.io.Serializable;

/**
 * Created by VoNga on 1/7/2017.
 */

public class QuanLy implements Serializable{
        private String username;
        private String password;
        private String email;

        public QuanLy(String username, String password, String email) {
            this.username = username;
            this.password = password;
            this.email = email;
        }

        public QuanLy() {
        }

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

        public String getEmail() {
            return email;
        }

        public void setEmail(String phone) {
            this.email = phone;
        }

}
