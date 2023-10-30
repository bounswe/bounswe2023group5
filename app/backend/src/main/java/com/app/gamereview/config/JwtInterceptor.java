package com.app.gamereview.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.app.gamereview.model.User;
import com.app.gamereview.util.AuthorizationRequired;
import com.app.gamereview.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import com.app.gamereview.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtInterceptor implements HandlerInterceptor {

	private final UserRepository userRepository;

	public JwtInterceptor(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String uri = request.getRequestURI();

		// Exclude Swagger URLs
		if (uri.startsWith("/v3/api-docs") || uri.contains("/swagger-ui/")) {
			return true;
		}

		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			AuthorizationRequired authRequired = handlerMethod.getMethodAnnotation(AuthorizationRequired.class);
			String token = request.getHeader("Authorization");
			if (authRequired != null) {
				if (token == null || !JwtUtil.validateToken(token)) {
					response.setStatus(HttpStatus.UNAUTHORIZED.value());
					Map<String, String> responseMessage = new HashMap<>();
					responseMessage.put("message", "Token is not valid");
					responseMessage.put("code", "401");

					String jsonResponse = objectMapper.writeValueAsString(responseMessage);

					response.getWriter().write(jsonResponse);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					return false;
				}
				String email = JwtUtil.extractSubject(token);
				Optional<User> user = userRepository.findByEmailAndIsDeletedFalse(email);
				if (user.isPresent()) {
					request.setAttribute("authenticatedUser", user.get());
				}
				else {
					response.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
					Map<String, String> responseMessage = new HashMap<>();
					responseMessage.put("message", "User doesn't exist or account is deleted");
					responseMessage.put("code", "405");

					String jsonResponse = objectMapper.writeValueAsString(responseMessage);

					response.getWriter().write(jsonResponse);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					return false;
				}
			}
		}

		return true;
	}

}