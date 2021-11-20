package pl.skrys.service;

import pl.skrys.app.SpVerifyToken;
import pl.skrys.app.UserRole;

import java.util.List;

public interface SpVerifyTokenService {

    void addVerifyToken(SpVerifyToken verifyToken);
    List<SpVerifyToken> listVerifyToken();
    SpVerifyToken getVerifyToken(long id);
    public SpVerifyToken getVerifyTokenByToken(String token);
}
