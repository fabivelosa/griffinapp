package com.callfailures.commontests.utils;

import static com.callfailures.commontests.utils.JsonTestUtils.*;

import java.net.URISyntaxException;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ResourceClient {
	private final URL urlBase;
	private String resourcePath;

	public ResourceClient(final URL urlBase) {
		this.urlBase = urlBase;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public ResourceClient resourcePath(final String resourcePath) {
		this.resourcePath = resourcePath;
		return this;
	}

	public Response postWithFile(final String fileName) {
		return postWithContent(getRequestFromFileOrEmptyIfNullFile(fileName));
	}
	

	public Response postWithContent(final String content) {
		System.out.println(content);
		return buildClient().post(Entity.entity(content, MediaType.APPLICATION_JSON));
	}
	
	public Response postWithFile(final String token, final String fileName) {
		return postWithContent(token, getRequestFromFileOrEmptyIfNullFile(fileName));
	}

	public Response postWithContent(final String token, final String content) {
		System.out.println(content);
		return buildClient().header(HttpHeaders.AUTHORIZATION, "Bearer " + token).post(Entity.entity(content, MediaType.APPLICATION_JSON));
	}
	
	public Response putWithFile(final String fileName) {
		return putWithContent(getRequestFromFileOrEmptyIfNullFile(fileName));
	}

	public Response putWithContent(final String content) {
		System.out.println(content);
		return buildClient().put(Entity.entity(content, MediaType.APPLICATION_JSON));
	}
	
	public Response putWithFile(final String token,final String fileName) {
		return putWithContent(getRequestFromFileOrEmptyIfNullFile(fileName));
	}

	public Response putWithContent(final String token, final String content) {
		System.out.println(content);
		return buildClient().put(Entity.entity(content, MediaType.APPLICATION_JSON));
	}

	public Response get(final String token) {
		return buildClient()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.get();
	}

	private Builder buildClient() {
		final Client resourceClient = ClientBuilder.newClient();
		System.out.println("THE URL IS " + getFullURL(resourcePath));
		
		return resourceClient.target(getFullURL(resourcePath)).request();
	}

	private String getFullURL(final String resourcePath) {
		try {
			return this.urlBase.toURI() + "api/" + resourcePath;
		} catch (final URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private String getRequestFromFileOrEmptyIfNullFile(final String fileName) {
		if (fileName == null) {
			return "";
		}
		return readJsonFile(fileName);
	}
	
	public void delete() {
		buildClient().delete();
	}
	
}