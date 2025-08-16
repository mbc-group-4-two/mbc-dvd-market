package org.group4.dvdshopbackend.core.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"isSuccess", "resCode", "resMessage", "data"})
public class ApiResult<T> {

	@JsonProperty("isSuccess")
	private boolean isSuccess = true;

	private int resCode = HttpStatus.OK.value();

	private String resMessage = HttpStatus.OK.name();

	private T result;

	public ApiResult(T data) {
		this.result = data;
	}

	// HTTPS 상태 로직 보강
	public ApiResult(int resCode, String resMessage, T data) {
		this.resCode = resCode;
		this.resMessage = resMessage;
		result = data;
	}

	// api request 성공 했을 때
	// 돌려줄 조회값이 있는 경우
	//      ok 호출한다.
	public static <T> ApiResult<T> ok(T data) {
		return new ApiResult<>(HttpStatus.OK.value(), "success", data);
	}

	// api request 성공 했을 때
	// POST 를 통해 데이터 생성 요청인 경우
	//      created 호출한다.
	public static <T> ApiResult<T> created(T data) {
		return new ApiResult<>(HttpStatus.CREATED.value(), "created", data);
	}

	// api request 성공 했을 때
	// 돌려줄 조회값이 없는 경우
	//      none 호출한다.
	public static ApiResult<Void> none() {
		return new ApiResult<>(HttpStatus.NO_CONTENT.value(), "no content", null);
	}
}