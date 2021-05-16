package moe.ahao.spring.boot.security.plugin.otp;

import moe.ahao.spring.boot.security.plugin.otp.generator.DefaultOtpGenerator;
import moe.ahao.spring.boot.security.plugin.otp.generator.OtpGenerator;
import moe.ahao.spring.boot.security.plugin.otp.lookup.LookupStrategy;
import moe.ahao.spring.boot.security.plugin.otp.send.SendStrategy;
import moe.ahao.spring.boot.security.plugin.otp.store.Tokenstore;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class OtpAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;
    private AuthenticationProvider provider;
    private Tokenstore tokenstore;
    private LookupStrategy lookupStrategy;
    private SendStrategy sendStrategy;
    private OtpGenerator gen;
    private static final int DEFAULT_OTP_LENGTH = 5;

    public OtpAuthenticationProvider(UserDetailsService userDetailsService, AuthenticationProvider provider,
                                     Tokenstore tokenstore, LookupStrategy lookupStrategy, SendStrategy sendStrategy) {
        if (provider == null) {
            throw new IllegalArgumentException("Embedded authentication provider must not be null.");
        }
        if (tokenstore == null) {
            throw new IllegalArgumentException("Tokenstore must not be null.");
        }
        if (lookupStrategy == null) {
            throw new IllegalArgumentException("LookupStrategy must not be null.");
        }
        if (sendStrategy == null) {
            throw new IllegalArgumentException("SendStrategy must not be null.");
        }
        this.userDetailsService = userDetailsService;
        this.provider = provider;
        this.tokenstore = tokenstore;
        this.lookupStrategy = lookupStrategy;
        this.sendStrategy = sendStrategy;
        this.gen = new DefaultOtpGenerator(DEFAULT_OTP_LENGTH);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String principal = (String) authentication.getPrincipal();
        UserDetails user = userDetailsService.loadUserByUsername(principal);




        Authentication auth = provider.authenticate(authentication);
        if (auth.isAuthenticated()) {
            // Generate OTP token
            String contact = lookupStrategy.lookup(auth.getName());
            if (contact != null) {
                String otp = gen.generateToken();
                tokenstore.putToken(auth.getName(), otp);
                sendStrategy.send(otp, contact);
            }
        }
        return new PreOtpAuthenticationToken(auth);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return provider.supports(authentication);
    }

    public void setOtpGenerator(OtpGenerator generator) {
        if (generator == null) {
            throw new IllegalArgumentException("OTP generator instance cannot be null.");
        }
        gen = generator;
    }
}
