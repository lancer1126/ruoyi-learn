package fun.lance.admin.controller;

import fun.lance.admin.service.AuthService;
import fun.lance.common.core.domain.model.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/binding/{source}")
    public Result<String> oauthBinding(@PathVariable String source,
                                       @RequestParam String tenantId,
                                       @RequestParam String domain) {
        return Result.success(authService.oauthBinding(source, tenantId, domain));
    }
}
