package org.group4.dvdshopbackend.core.api;

import org.springframework.http.ResponseEntity;

public final class ApiResponse {
	/**
	 * api request 성공 했을 때
	 * 돌려줄 조회값이 있는 경우
	 *      ok 호출한다.
	 * @param body
	 * @return
	 * @param <T>
	 */
	public static <T> ResponseEntity<ApiResult<T>> ok(T body) {
		return ResponseEntity.ok(ApiResult.ok(body));
	}

	/**
	 * api request 성공 했을 때
	 * POST 를 통해 데이터 생성 요청인 경우
	 *      created 호출한다.
	 * @param body
	 * @return
	 * @param <T>
	 */
	public static <T> ResponseEntity<ApiResult<T>> created(T body) {
		return ResponseEntity.status(201).body(ApiResult.created(body));
	}

	/**
	 * api request 성공 했을 때
	 * 돌려줄 조회값이 없는 경우
	 *      noContent 호출한다.
	 * @return
	 */
	public static ResponseEntity<ApiResult<?>> noContent() {
		return ResponseEntity.status(204).body(ApiResult.none());
	}
}
