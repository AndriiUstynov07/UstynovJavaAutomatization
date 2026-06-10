package com.orm.app;

import com.orm.annotations.*;

/**
 * Example entity — a User.
 *
 * @Table  → RUNTIME: OrmSession reads the table name via reflection.
 * @GenerateRepository → SOURCE: consumed at compile-time to generate UserRepository.java.
 * @Validated → RUNTIME: EntityValidator checks @NotNull / @MaxLength before save.
 */
@Table(name = "users")
@GenerateRepository                 // processor generates UserRepository.java
@Validated(failFast = false)        // collect ALL violations, don't stop at first
public class User {

    @Id
    private Long id;

    @NotNull
    @MaxLength(50)
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @NotNull
    @MaxLength(100)
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "age")
    private Integer age;

    // ---- boilerplate ----
    public User() {}

    public User(String username, String email, Integer age) {
        this.username = username;
        this.email    = email;
        this.age      = age;
    }

    public Long    getId()       { return id; }
    public String  getUsername() { return username; }
    public String  getEmail()    { return email; }
    public Integer getAge()      { return age; }

    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email)       { this.email = email; }
    public void setAge(Integer age)          { this.age = age; }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', email='" + email + "', age=" + age + "}";
    }
}
