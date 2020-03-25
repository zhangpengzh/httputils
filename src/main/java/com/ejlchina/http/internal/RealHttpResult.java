package com.ejlchina.http.internal;

import java.util.List;

import com.ejlchina.http.HttpResult;

import okhttp3.Headers;
import okhttp3.Response;


public class RealHttpResult implements HttpResult {

	private State state;
	private Response response;
	private Exception error;
	private TaskExecutor taskExecutor;
	
	public RealHttpResult() {
	}
	
	public RealHttpResult(State state) {
		this.state = state;
	}
	
	public RealHttpResult(Response response, TaskExecutor taskExecutor) {
		this(taskExecutor);
		response(response);
	}
	
	public RealHttpResult(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	
	public RealHttpResult(State state, Exception error) {
		exception(state, error);
	}
	
	public void exception(State state, Exception error) {
		this.state = state;
		this.error = error;
	}
	
	public void response(Response response) {
		this.state = State.RESPONSED;
		this.response = response;
	}
	
	@Override
	public State getState() {
		return state;
	}

	@Override
	public int getStatus() {
		if (response != null) {
			return response.code();
		}
		return 0;
	}

	@Override
	public boolean isSuccessful() {
	    if (response != null) {
			return response.isSuccessful();
		}
		return false;
	}
	
	@Override
	public Headers getHeaders() {
		if (response != null) {
			return response.headers();
		}
		return null;
	}
	
	@Override
	public List<String> getHeaders(String name) {
	    return response.headers(name);
	}

	@Override
	public String getHeader(String name) {
	    return response.header(name);
	}

	@Override
	public Body getBody() {
		if (response != null) {
			return new ResultBody(response, taskExecutor);
		}
		return null;
	}
	
	@Override
	public Exception getError() {
		return error;
	}

	public Response getResponse() {
		return response;
	}
	

	@Override
	public String toString() {
		Body body = getBody();
		String str = "HttpResult [\n  state: " + state + ",\n  status: " + getStatus() 
				+ ",\n  headers: " + getHeaders();
		if (body != null) {
			str += ",\n  contentType: " + body.getContentType()
			+ ",\n  body: " + body.toString();
		}
		return str + ",\n  error: " + error + "\n]";
	}

	@Override
	public HttpResult close() {
		if (response != null) {
			response.close();
		}
		return this;
	}
	
}
