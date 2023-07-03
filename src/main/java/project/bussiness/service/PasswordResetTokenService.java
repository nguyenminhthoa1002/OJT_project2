package project.bussiness.service;

import project.model.entity.PasswordResetToken;

public interface PasswordResetTokenService {
    PasswordResetToken saveOrUpdate(PasswordResetToken passwordResetToken);
    PasswordResetToken getLastTokenByUserId(int userId);
}
