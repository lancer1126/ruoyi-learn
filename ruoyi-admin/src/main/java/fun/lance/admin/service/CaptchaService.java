package fun.lance.admin.service;

import fun.lance.admin.domain.vo.CaptchaVo;

public interface CaptchaService {
    /**
     * 获取验证码
     */
    CaptchaVo getCode();
}
