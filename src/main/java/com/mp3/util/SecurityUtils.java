package com.mp3.util;

import com.mp3.entity.User;
import com.mp3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserRepository userRepository;

    /**
     * Lấy user hiện tại từ SecurityContext
     * @return Optional<User> - User hiện tại hoặc empty nếu chưa đăng nhập
     */
    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        String email = authentication.getName();
        if (email == null || "anonymousUser".equals(email)) {
            return Optional.empty();
        }

        return userRepository.findByEmail(email);
    }

    /**
     * Lấy user hiện tại, throw exception nếu không tìm thấy
     * @return User hiện tại
     * @throws IllegalStateException nếu user chưa đăng nhập
     */
    public User getCurrentUserOrThrow() {
        return getCurrentUser()
                .orElseThrow(() -> new IllegalStateException("User not authenticated"));
    }

    /**
     * Lấy ID của user hiện tại
     * @return Optional<String> - ID của user hiện tại
     */
    public Optional<String> getCurrentUserId() {
        return getCurrentUser().map(User::getId);
    }

    /**
     * Lấy email của user hiện tại
     * @return Optional<String> - Email của user hiện tại
     */
    public Optional<String> getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        String email = authentication.getName();
        if (email == null || "anonymousUser".equals(email)) {
            return Optional.empty();
        }

        return Optional.of(email);
    }

    /**
     * Kiểm tra user hiện tại có phải là admin không
     * @return true nếu là admin, false nếu không
     */
    public boolean isCurrentUserAdmin() {
        return getCurrentUser()
                .map(user -> "ADMIN".equals(user.getRole().name()))
                .orElse(false);
    }

    /**
     * Kiểm tra user hiện tại có đang đăng nhập không
     * @return true nếu đã đăng nhập, false nếu chưa
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
               authentication.isAuthenticated() &&
               !"anonymousUser".equals(authentication.getName());
    }
}
