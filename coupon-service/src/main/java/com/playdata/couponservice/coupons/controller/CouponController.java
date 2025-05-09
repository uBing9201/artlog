package com.playdata.couponservice.coupons.controller;

import com.playdata.couponservice.common.dto.CommonResDto;
import com.playdata.couponservice.common.exception.InvalidCouponAccessException;
import com.playdata.couponservice.common.exception.InvalidCouponRegisterException;
import com.playdata.couponservice.coupons.dto.request.CouponReqDto;
import com.playdata.couponservice.coupons.dto.response.CouponCountResDto;
import com.playdata.couponservice.coupons.dto.response.CouponResDto;
import com.playdata.couponservice.coupons.dto.response.CouponSaveResDto;
import com.playdata.couponservice.coupons.dto.response.CouponValidateDto;
import com.playdata.couponservice.coupons.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
@Slf4j
public class CouponController {
    private final CouponService couponService;

    /**
     * @param dto CouponReqDto
     * @return 등록 완료되면 CouponSaveResDto 반환
     * @throws InvalidCouponRegisterException 등록 실패
     */
    @PostMapping("/insert")
    public ResponseEntity<?> insert(@RequestBody CouponReqDto dto) throws InvalidCouponRegisterException {
        CouponSaveResDto resDto = couponService.insert(dto);
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "쿠폰이 정상적으로 등록되었습니다.", resDto));
    }

    /**
     * @param id 찾을 쿠폰의 id
     * @return CouponValidateDto 유효한지에 대한 정보 반환
     */
    @GetMapping("/isValid/{id}")
    public ResponseEntity<?> isValid(@PathVariable Long id) {
        CouponValidateDto resDto = couponService.isValid(id);
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, resDto.getId() +"번 쿠폰이 현재 유효한 상태입니다.", resDto));
    }

    /**
     * @return 유효한 모든 쿠폰 조회 결과 반환
     */
    @GetMapping("/findByAll")
    public ResponseEntity<?> findByAll() {
        List<CouponResDto> resDtoList = couponService.findByAll();
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "유효한 모든 쿠폰을 조회하였습니다.", resDtoList));

    }

    /**
     * @param id 찾을 쿠폰의 id
     * @return CouponCountResDto 쿠폰의 이름과 수량 정보 반환
     * @throws InvalidCouponAccessException 잘못된 쿠폰에 대한 접근
     */
    @GetMapping("/findCountById/{id}")
    public ResponseEntity<?> findCountById(@PathVariable Long id) throws InvalidCouponAccessException {
        CouponCountResDto resDto= couponService.findCountById(id);
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, id + "번의 남은 수량은 " + resDto.getCount() + "입니다.", resDto));
    }

}
