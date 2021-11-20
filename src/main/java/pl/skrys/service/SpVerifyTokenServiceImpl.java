package pl.skrys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.skrys.app.SpVerifyToken;
import pl.skrys.dao.SpVerifyTokenRepository;

import java.util.List;

@Service("verifyTokenService")
public class SpVerifyTokenServiceImpl implements SpVerifyTokenService {

    private SpVerifyTokenRepository spVerifyTokenRepository;

    @Autowired
    public SpVerifyTokenServiceImpl(SpVerifyTokenRepository spVerifyTokenRepository) {
        this.spVerifyTokenRepository = spVerifyTokenRepository;
    }

    @Transactional
    public void addVerifyToken(SpVerifyToken verifyToken) {

        spVerifyTokenRepository.save(verifyToken);
    }

    @Transactional
    public void editVerifyToken(SpVerifyToken verifyToken) {

        spVerifyTokenRepository.save(verifyToken);
    }

    @Transactional
    public List<SpVerifyToken> listVerifyToken() {
        return spVerifyTokenRepository.findAll();
    }

    @Transactional
    public SpVerifyToken getVerifyToken(long id) {
        return spVerifyTokenRepository.findById(id);
    }

    @Transactional
    public SpVerifyToken getVerifyTokenByToken(String token) {
        return spVerifyTokenRepository.findByVerifyToken(token);
    }

    @Transactional
    public void removeVerifyToken(long id) {
        spVerifyTokenRepository.delete(id);
    }



}
