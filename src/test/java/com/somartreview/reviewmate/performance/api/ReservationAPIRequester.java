package com.somartreview.reviewmate.performance.api;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.io.File;
import java.util.Map;

import static com.somartreview.reviewmate.performance.PerformanceTest.*;

public class ReservationAPIRequester {

    public static ExtractableResponse<Response> 예약을_생성한다() {
        Map<String, Object> singleTravelReservationCreateRequest = getSingleTravelReservationCreateReuqest();

        return RestAssured.given()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .pathParam("partnerDomain", PARTNER_DOMAIN)
                .multiPart("singleTravelReservationCreateRequest", singleTravelReservationCreateRequest, MediaType.APPLICATION_JSON_VALUE)
                .multiPart("singleTravelProductThumbnail", new File("src/test/resources/images/logo_b.jpg"), MediaType.MULTIPART_FORM_DATA_VALUE)
                .when().post("api/client/v1/{partnerDomain}/products/travel/single/reservations")
                .then()
                .extract();
    }

    private static Map<String, Object> getSingleTravelReservationCreateReuqest() {
        Map<String, Object> customerCreateRequest = Map.of(
                "partnerCustomId", "CUSTOMER_" + PARTNER_CUSTOM_ID_POSTFIX,
                "name", "권순찬",
                "phoneNumber", CUSTOMER_PHONE_NUMBER,
                "kakaoId", "sckwon770_" + PARTNER_CUSTOM_ID_POSTFIX
        );

        Map<String, Object> singleTravelProductCreateRequest = Map.of(
                "partnerCustomId", "PRODUCT_" + PARTNER_CUSTOM_ID_POSTFIX,
                "name", "신라더스테이 호텔",
                "singleTravelProductCategory", "ACCOMMODATION",
                "partnerSellerId", 1L
        );

        return Map.of(
                "partnerCustomId", "RESERVATION_" + PARTNER_CUSTOM_ID_POSTFIX,
                "startDateTime", "2023-10-18T13:00:00",
                "endDateTime", "2023-10-19T12:00:00",
                "customerCreateRequest", customerCreateRequest,
                "singleTravelProductCreateRequest", singleTravelProductCreateRequest
        );
    }

    public static ExtractableResponse<Response> 예약Id로_예약을_조회한다() {
        return RestAssured.given()
                .pathParam("reservationId", SAFE_COMMON_ID)
                .when().get("api/console/v1/products/travel/single/reservations/{reservationId}")
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 고객Id와_예약목록을_조회한다() {
        return RestAssured.given()
                .param("customerId", SAFE_COMMON_ID)
                .when().get("api/console/v1/products/travel/single/reservations")
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 상품Id와_예약목록을_조회한다() {
        return RestAssured.given()
                .param("singleTravelProductId", SAFE_COMMON_ID)
                .when().get("api/console/v1/products/travel/single/reservations")
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 고객Id와_상품Id로_예약목록을_조회한다() {
        return RestAssured.given()
                .param("customerId", SAFE_COMMON_ID)
                .param("singleTravelProductId", SAFE_COMMON_ID)
                .when().get("api/console/v1/products/travel/single/reservations")
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> 예약Id로_예약을_삭제한다(long createdReservationId) {
        return RestAssured.given()
                .pathParam("reservationId", createdReservationId)
                .when().delete("api/console/products/travel/single/reservations/{reservationId}")
                .then()
                .extract();
    }
}
