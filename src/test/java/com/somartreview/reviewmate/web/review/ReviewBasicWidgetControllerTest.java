package com.somartreview.reviewmate.web.review;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(ReviewBasicWidgetController.class)
class ReviewBasicWidgetControllerTest {

//    private final String PARTNER_DOMAIN = "goodchoice.kr";
//    private final String RESERVATION_PARTNER_CUSTOM_ID = "RESERVATION_0001";
//
//    @MockBean
//    private JpaMetamodelMappingContext jpaMetamodelMappingContext;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private ReviewService reviewService;
//
//    @MockBean
//    private ReviewDeleteService reviewDeleteService;
//
//    @MockBean
//    private ReservationService reservationService;
//
//    @Test
//    void 리뷰를_생성한다() throws Exception {
//        // given
//        given(reviewService.create(anyString(), anyString(), any(), any())).willReturn(1L);
//
//        MockMultipartFile image1 = new MockMultipartFile(
//                "reviewImages[0]",
//                "logo_b.jpg",
//                MediaType.IMAGE_JPEG_VALUE,
//                new FileInputStream("src/test/resources/images/logo_b.jpg")
//        );
//
//        MockMultipartFile reviewCreateRequest = new MockMultipartFile(
//                "reviewCreateRequest",
//                null,
//                MediaType.APPLICATION_JSON_VALUE,
//                objectMapper.writeValueAsString(new ReviewCreateRequest(5, "리뷰 제목", "리뷰 내용")).getBytes()
//        );
//
//        // when & then
//        mockMvc.perform(
//                        multipart("/api/widget/v1/" + PARTNER_DOMAIN + "/reservations/" + RESERVATION_PARTNER_CUSTOM_ID + "/reviews")
//                                .file(reviewCreateRequest)
//                                .file(image1)
//                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
//                )
//                .andExpect(status().isCreated())
//                .andExpect(header().string(HttpHeaders.LOCATION, "/api/widget/v1/reviews/1"));
//    }
//
//    @Test
//    void 한_개의_리뷰를_조회한댜() throws Exception {
//        // given
//        WidgetReviewResponse mockWidgetReviewResponse = new WidgetReviewResponse(1L, 5, "리뷰 제목", "리뷰 내용", "작성자", "2021.01.01", ReviewPolarity.POSITIVE, Collections.emptyList());
//        given(reviewService.getWidgetReviewResponseById(anyLong())).willReturn(mockWidgetReviewResponse);
//
//        // when & then
//        mockMvc.perform(
//                        get("/api/widget/v1/reviews/{reviewId}", 1)
//
//                )
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void 상품에_등록된_리뷰들을_조건없이_조회한다() throws Exception {
//        // given
//        given(reviewService.searchWidgetReviewResponsesWithPaging(anyString(), anyString(), any(), any(), any(), anyInt(), anyInt())).willReturn(Page.empty());
//
//        // when & then
//        mockMvc.perform(
//                        get("/api/widget/v1/{partnerDomain}/products/{travelProductPartnerCustomId}/reviews", PARTNER_DOMAIN, RESERVATION_PARTNER_CUSTOM_ID)
//                )
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void 상품에_등록된_리뷰들을_조건과_함께_조회한다() throws Exception {
//        // given
//        given(reviewService.searchWidgetReviewResponsesWithPaging(anyString(), anyString(), any(), any(), any(), anyInt(), anyInt())).willReturn(Page.empty());
//
//        // when & then
//        mockMvc.perform(
//                        get("/api/widget/v1/{partnerDomain}/products/{travelProductPartnerCustomId}/reviews", PARTNER_DOMAIN, RESERVATION_PARTNER_CUSTOM_ID)
//                                .param("property", String.valueOf(ReviewProperty.ROOM))
//                                .param("keyword", "편안")
//                                .param("orderBy", String.valueOf(ReviewOrderCriteria.RATING_DESC))
//                                .param("page", "0")
//                                .param("size", "10")
//                )
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void 리뷰를_수정한다() throws Exception {
//        // given
//        doNothing().when(reviewService).update(anyLong(), any(), any());
//
//        MockMultipartFile image2 = new MockMultipartFile(
//                "reviewImages[0]",
//                "logo_w.jpg",
//                MediaType.IMAGE_JPEG_VALUE,
//                new FileInputStream("src/test/resources/images/logo_w.jpg")
//        );
//
//        MockMultipartFile reviewUpdateRequest = new MockMultipartFile(
//                "reviewUpdateRequest",
//                null,
//                MediaType.APPLICATION_JSON_VALUE,
//                objectMapper.writeValueAsString(new ReviewUpdateRequest(5, "리뷰 제목", "리뷰 내용")).getBytes()
//        );
//
//        // when & then
//        mockMvc.perform(
//                        multipart(HttpMethod.PATCH, "/api/widget/v1/reviews/{reviewId}", 1)
//                                .file(reviewUpdateRequest)
//                                .file(image2)
//                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
//                )
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void 리뷰를_삭제한다() throws Exception {
//        // given
//        doNothing().when(reviewDeleteService).delete(anyLong());
//
//        // when & then
//        mockMvc.perform(
//                        delete("/api/widget/v1/reviews/{reviewId}", 1)
//                )
//                .andExpect(status().isNoContent());
//    }

}