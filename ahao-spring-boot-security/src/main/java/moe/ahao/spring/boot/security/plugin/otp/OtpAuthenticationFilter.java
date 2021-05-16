//package moe.ahao.spring.boot.security.plugin.otp;
//
//import moe.ahao.spring.boot.security.plugin.otp.store.Tokenstore;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.security.authentication.AuthenticationServiceException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class OtpAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
//
//	public static final String DEFAULT_OTP_PARAMETER_NAME = "otptoken";
//	public String otpParameterName = DEFAULT_OTP_PARAMETER_NAME;
//	private Tokenstore tokenstore;
//
//	public OtpAuthenticationFilter(Tokenstore tokenstore, String endpoint, String successUrl, String failureUrl) {
//        super(new AntPathRequestMatcher("/otp", "POST"));
//        this.tokenstore = tokenstore;
//	}
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
//        return null;
//    }
//
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        String username = request.getParameter(otpParameterName);
//        String principal = StringUtils.trimToNull(username == null ? "" : username);
//
//        PreOtpAuthenticationToken token = new PreOtpAuthenticationToken(principal);
//
//        // Allow subclasses to set the "details" property
//        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
//        setDetails(request, token);
//
//        return this.getAuthenticationManager().authenticate(token);
//
//		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
//			throw new IllegalArgumentException("Request and response must be over HTTP.");
//		}
//		HttpServletRequest req = (HttpServletRequest) request;
//		HttpServletResponse resp = (HttpServletResponse) response;
//
//		// Make sure validation endpoint was requested before continuing
//		String path = req.getRequestURI().substring(req.getContextPath().length());
//		if (!path.equals(endpoint)) {
//			chain.doFilter(request, response);
//			return;
//		}
//
//		// Get token from request
//		String token = request.getParameter(otpParameterName);
//		if (token == null) {
//			resp.sendRedirect(failureUrl);
//			return;
//		}
//
//		// Get username from security context
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		if (auth == null) {
//			resp.sendRedirect(failureUrl);
//			return;
//		}
//		if (!(auth instanceof PreOtpAuthenticationToken)) {
//			resp.sendRedirect(failureUrl);
//			return;
//		}
//		PreOtpAuthenticationToken authToken = (PreOtpAuthenticationToken) auth;
//		String username = authToken.getName();
//
//		// Validate token
//		if (tokenstore.isTokenValid(username, token)) {
//			SecurityContextHolder.getContext().setAuthentication(authToken.getEmbeddedToken());
//			resp.sendRedirect(successUrl);
//		} else {
//			SecurityContextHolder.getContext().setAuthentication(null);
//			resp.sendRedirect(failureUrl);
//		}
//	}
//
//
//}
