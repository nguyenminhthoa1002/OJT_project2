package project.bussiness.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.bussiness.service.PasswordResetTokenService;
import project.model.entity.PasswordResetToken;
import project.repository.PasswordResetTokenRepository;

@Service
public class PasswordResetTokenServiceImple implements PasswordResetTokenService {
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public PasswordResetToken saveOrUpdate(PasswordResetToken passwordResetToken) {
        return passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public PasswordResetToken getLastTokenByUserId(int userId) {
        return passwordResetTokenRepository.getLastTokenByUserId(userId);
    }
}
