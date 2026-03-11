package Session04.Ex6;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserProfile {
    String email;
    LocalDate birthDate;
    public UserProfile(String email, LocalDate birthDate) {
        this.email = email;
        this.birthDate = birthDate;
    }
}

class User {
    String email;
    UserProfile profile;
    public User(String email, UserProfile profile) {
        this.email = email;
        this.profile = profile;
    }
}

class ProfileService {
    public User updateProfile(User existingUser, UserProfile newProfile, List<User> allUsers) {
        if (newProfile.birthDate.isAfter(LocalDate.now())) return null;
        for (User u : allUsers) {
            if (u.email.equals(newProfile.email) && !u.email.equals(existingUser.email)) return null;
        }
        existingUser.profile = newProfile;
        existingUser.email = newProfile.email;
        return existingUser;
    }
}

public class Main {
    private ProfileService service;
    private List<User> allUsers;
    private User existingUser;

    @BeforeEach
    void setUp() {
        service = new ProfileService();
        allUsers = new ArrayList<>();
        existingUser = new User("me@test.com", new UserProfile("me@test.com", LocalDate.of(1990, 1, 1)));
        allUsers.add(existingUser);
    }

    @Test
    void testUpdate_Success() {
        UserProfile newProfile = new UserProfile("new@test.com", LocalDate.of(1995, 1, 1));
        User result = service.updateProfile(existingUser, newProfile, allUsers);
        assertNotNull(result);
    }

    @Test
    void testUpdate_FutureBirthDate_Fails() {
        UserProfile newProfile = new UserProfile("me@test.com", LocalDate.now().plusDays(1));
        User result = service.updateProfile(existingUser, newProfile, allUsers);
        assertNull(result);
    }

    @Test
    void testUpdate_DuplicateEmail_Fails() {
        allUsers.add(new User("other@test.com", new UserProfile("other@test.com", LocalDate.of(1990, 1, 1))));
        UserProfile newProfile = new UserProfile("other@test.com", LocalDate.of(1990, 1, 1));
        User result = service.updateProfile(existingUser, newProfile, allUsers);
        assertNull(result);
    }

    @Test
    void testUpdate_SameEmail_Success() {
        UserProfile newProfile = new UserProfile("me@test.com", LocalDate.of(1990, 1, 1));
        User result = service.updateProfile(existingUser, newProfile, allUsers);
        assertNotNull(result);
    }

    @Test
    void testUpdate_EmptyUserList_Success() {
        List<User> emptyList = new ArrayList<>();
        UserProfile newProfile = new UserProfile("new@test.com", LocalDate.of(1990, 1, 1));
        User result = service.updateProfile(existingUser, newProfile, emptyList);
        assertNotNull(result);
    }

    @Test
    void testUpdate_MultipleViolations_Fails() {
        allUsers.add(new User("other@test.com", new UserProfile("other@test.com", LocalDate.of(1990, 1, 1))));
        UserProfile newProfile = new UserProfile("other@test.com", LocalDate.now().plusDays(1));
        User result = service.updateProfile(existingUser, newProfile, allUsers);
        assertNull(result);
    }
}