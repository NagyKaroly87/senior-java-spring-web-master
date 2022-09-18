package hu.ponte.hr.services;

import hu.ponte.hr.config.ConfigNameConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
@Slf4j
public class SignService {
    public static final String SIGN_ALGORITHMS = "SHA256withRSA";
    @Autowired
    Environment environment;

    public String sign(byte[] data) throws SignatureException {

        try {
            byte[] filePrivateKey = Files.readAllBytes(Paths.get(environment.getProperty(ConfigNameConst.PRIVATE_KEY_PATH)));

            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                    filePrivateKey);
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(data);

            byte[] signed = signature.sign();
            isValidByPublicKey(signed, data);

            return Base64.getEncoder().encodeToString(signed);

        } catch (Exception e) {
            log.error("Something went wrong: sign inputs ", e);
            throw new SignatureException(e);
        }
    }


    // Not part of the task
    public boolean isValidByPublicKey(byte[] signed, byte[] data) {
        boolean ret = false;
        try {
            byte[] filePublicKey = Files.readAllBytes(Paths.get(environment.getProperty(ConfigNameConst.PUBLIC_KEY_PATH)));

            X509EncodedKeySpec pubPKCS8 = new X509EncodedKeySpec(
                    filePublicKey);
            KeyFactory keyfpublic = KeyFactory.getInstance("RSA");

            PublicKey publickey = keyfpublic.generatePublic(pubPKCS8);

            Signature signaturepub = Signature
                    .getInstance(SIGN_ALGORITHMS);
            signaturepub.initVerify(publickey);
            signaturepub.update(data);
            ret = signaturepub.verify(signed);
        } catch (Exception e) {
            log.warn("Something went wrong: isValidByPublicKey inputs ", e);
        }
        return ret;
    }

}




