package com.orm.app;

import com.orm.runtime.ValidationException;
import com.orm.session.OrmSession;

import java.util.List;
import java.util.Optional;

/**
 * Demo application.
 *
 * UserRepository and ProductRepository are GENERATED at compile-time
 * by RepositoryProcessor.  The @Validated / @NotNull / @MaxLength
 * constraints are evaluated at RUNTIME inside OrmSession.
 */
public class Main {

    public static void main(String[] args) {
        OrmSession session = new OrmSession();

        // --- Generated repositories (created by annotation processor) ---
        UserRepository    userRepo    = new UserRepository(session);
        ProductRepository productRepo = new ProductRepository(session);

        System.out.println("=== Saving valid users ===");
        User alice = userRepo.save(new User("alice", "alice@example.com", 30));
        User bob   = userRepo.save(new User("bob",   "bob@example.com",   25));
        System.out.println("Saved: " + alice);
        System.out.println("Saved: " + bob);

        System.out.println("\n=== FindAll users ===");
        List<User> allUsers = userRepo.findAll();
        allUsers.forEach(System.out::println);

        System.out.println("\n=== FindById ===");
        Optional<User> found = userRepo.findById(1L);
        found.ifPresent(u -> System.out.println("Found: " + u));

        System.out.println("\n=== Delete user id=2 ===");
        userRepo.delete(2L);
        System.out.println("After delete: " + userRepo.findAll());

        System.out.println("\n=== Products ===");
        Product p = productRepo.save(new Product("Laptop", 999.99));
        System.out.println("Saved: " + p);

        System.out.println("\n=== Runtime validation — null username ===");
        try {
            userRepo.save(new User(null, "no-name@example.com", 20));
        } catch (ValidationException e) {
            System.out.println("Caught ValidationException: " + e.getMessage());
            e.getViolations().forEach(v -> System.out.println("  -> " + v));
        }

        System.out.println("\n=== Runtime validation — too-long username + null email ===");
        try {
            String longName = "a".repeat(60);           // exceeds @MaxLength(50)
            userRepo.save(new User(longName, null, 20)); // also null email
        } catch (ValidationException e) {
            System.out.println("Caught ValidationException (failFast=false, all violations):");
            e.getViolations().forEach(v -> System.out.println("  -> " + v));
        }

        System.out.println("\n=== Runtime validation — null product title (failFast=true) ===");
        try {
            productRepo.save(new Product(null, 10.0));
        } catch (ValidationException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }
}
