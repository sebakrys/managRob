package pl.skrys.service;

public interface ReCaptchaService {
    boolean verify(String captcha);
}
