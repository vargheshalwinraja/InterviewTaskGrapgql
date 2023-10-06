package com.allwin.graphql;

import com.allwin.graphql.exception.GraphQLErrorAdapter;
import com.allwin.graphql.repository.SellerRepositoryCustom;
import com.allwin.graphql.repository.SellersRepository;
import com.allwin.graphql.resolver.Query;
import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.kickstart.execution.error.GraphQLErrorHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class DemoGraphQlApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(DemoGraphQlApplication.class, args);
	}

	// Define a bean for the GraphQL instance
	@Bean
	public GraphQL graphQL(GraphQLSchema graphQLSchema) {
		return GraphQL.newGraphQL(graphQLSchema).build();
	}

	// Define a bean for handling GraphQL errors
	@Bean
	public GraphQLErrorHandler errorHandler() {
		return new GraphQLErrorHandler() {
			@Override
			public List<GraphQLError> processErrors(List<GraphQLError> errors) {
				// Separate errors into client errors and server errors
				List<GraphQLError> clientErrors = errors.stream()
						.filter(this::isClientError)
						.collect(Collectors.toList());

				List<GraphQLError> serverErrors = errors.stream()
						.filter(e -> !isClientError(e))
						.map(GraphQLErrorAdapter::new)
						.collect(Collectors.toList());

				List<GraphQLError> e = new ArrayList<>();
				e.addAll(clientErrors);
				e.addAll(serverErrors);
				return e;
			}

			// Check if an error is a client error
			protected boolean isClientError(GraphQLError error) {
				return !(error instanceof ExceptionWhileDataFetching || error instanceof Throwable);
			}
		};
	}

	// Define a bean for the GraphQL query resolver
	@Bean
	public Query query(SellersRepository sellersRepository, SellerRepositoryCustom sellerRepositoryCustom) {
		return new Query(sellersRepository, sellerRepositoryCustom);
	}
}
