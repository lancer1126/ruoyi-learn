package fun.lance.admin.service.impl;

import fun.lance.admin.domain.vo.CaptchaVo;
import fun.lance.admin.service.CaptchaService;
import org.springframework.stereotype.Service;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Override
    public CaptchaVo getCode() {
        // todo 获取验证码
        CaptchaVo captchaVo = new CaptchaVo();
        captchaVo.setImg("xxxx");
        return captchaVo;
    }
}
