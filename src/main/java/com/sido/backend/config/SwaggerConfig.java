package com.sido.backend.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
	private final String jwtSchemeName = "jwtAuth";

	@Bean
	public OpenAPI openAPI() {
		Server devServer = new Server();
		Server prodServer = new Server();
		devServer.setUrl("/");
		prodServer.setUrl("/api");

		return new OpenAPI()
			.servers(List.of(devServer, prodServer))
			.info(getInfo())
			.addSecurityItem(getSecurityItem())
			.components(getComponents());
	}

	private Info getInfo() {
		return new Info()
			.version("0.1.0")
			.title("SIDO APIs")
			.description("SIDO API Documents");
	}

	private SecurityRequirement getSecurityItem() {
		return new SecurityRequirement().addList(jwtSchemeName);
	}

	private Components getComponents() {
		return new Components()
			.addSecuritySchemes(jwtSchemeName, new SecurityScheme()
				.name(jwtSchemeName)
				.type(SecurityScheme.Type.HTTP)
				.scheme("bearer")
				.bearerFormat("JWT"));
	}
}
