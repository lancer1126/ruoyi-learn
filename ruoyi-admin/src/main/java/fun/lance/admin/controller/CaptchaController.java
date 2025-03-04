package fun.lance.admin.controller;

import fun.lance.admin.domain.vo.CaptchaVo;
import fun.lance.admin.service.CaptchaService;
import fun.lance.common.core.domain.model.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CaptchaController {

    private final CaptchaService captchaService;

    @GetMapping("/auth/code")
    public Result<CaptchaVo> getCaptcha() {
        return Result.success(captchaService.getCode());
    }
}
