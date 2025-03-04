package fun.lance.admin.domain.vo;

import lombok.Data;

@Data
public class CaptchaVo {

    private Boolean captchaEnabled = true;
    private String uuid;
    /**
     * 验证码图片
     */
    private String img;
}
