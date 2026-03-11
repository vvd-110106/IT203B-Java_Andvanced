package Session04.Ex5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertAll;

enum Role { ADMIN, MODERATOR, USER }
enum Action { DELETE_USER, LOCK_USER, VIEW_PROFILE }

class User {
    Role role;
    public User(Role role) { this.role = role; }
}

class AccessControl {
    public boolean canPerformAction(User user, Action action) {
        if (user.role == Role.ADMIN) return true;
        if (user.role == Role.MODERATOR) return action != Action.DELETE_USER;
        return action == Action.VIEW_PROFILE;
    }
}

public class Main {
    private AccessControl ac;
    @BeforeEach
    void setUp() {
        ac = new AccessControl();
    }
    @Test
    void testAdminPermissions() {
        User admin = new User(Role.ADMIN);
        assertAll("ADMIN Actions",
                () -> assertTrue(ac.canPerformAction(admin, Action.DELETE_USER)),
                () -> assertTrue(ac.canPerformAction(admin, Action.LOCK_USER)),
                () -> assertTrue(ac.canPerformAction(admin, Action.VIEW_PROFILE))
        );
    }
    @Test
    void testModeratorPermissions() {
        User moderator = new User(Role.MODERATOR);
        assertAll("MODERATOR Actions",
                () -> assertFalse(ac.canPerformAction(moderator, Action.DELETE_USER)),
                () -> assertTrue(ac.canPerformAction(moderator, Action.LOCK_USER)),
                () -> assertTrue(ac.canPerformAction(moderator, Action.VIEW_PROFILE))
        );
    }
    @Test
    void testUserPermissions() {
        User user = new User(Role.USER);
        assertAll("USER Actions",
                () -> assertFalse(ac.canPerformAction(user, Action.DELETE_USER)),
                () -> assertFalse(ac.canPerformAction(user, Action.LOCK_USER)),
                () -> assertTrue(ac.canPerformAction(user, Action.VIEW_PROFILE))
        );
    }
}