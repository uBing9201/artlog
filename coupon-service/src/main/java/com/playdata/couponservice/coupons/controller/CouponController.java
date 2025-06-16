package com.playdata.couponservice.coupons.controller;

import com.playdata.couponservice.common.auth.TokenUserInfo;
import com.playdata.couponservice.common.dto.CommonResDto;
import com.playdata.couponservice.common.exception.InvalidCouponAccessException;
import com.playdata.couponservice.common.exception.InvalidCouponRegisterException;
import com.playdata.couponservice.coupons.dto.request.CouponReqDto;
import com.playdata.couponservice.coupons.dto.response.*;
import com.playdata.couponservice.coupons.service.CouponService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
@Slf4j
public class CouponController {
    private final CouponService couponService;

    /**
     * 쿠폰 등록
     * @param dto serialNumber, expireDate, period, count, couponTitle
     * @return id, couponTitle
     * @throws InvalidCouponRegisterException 쿠폰 등록 실패
     */
    @PostMapping("/insert")
    public ResponseEntity<?> insert(@RequestBody @Valid CouponReqDto dto) throws InvalidCouponRegisterException {
        CouponSaveResDto resDto = couponService.insert(dto);
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "쿠폰이 정상적으로 등록되었습니다.", resDto));
    }

    /**
     * 쿠폰 유효성 검증 (쿠폰 서비스 내부 사용)
     * @param id id
     * @return id, valid
     */
    @GetMapping("/isValid/{id}")
    public ResponseEntity<?> isValid(@PathVariable Long id) {
        CouponValidateDto resDto = couponService.isValid(id);
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, resDto.getId() +"번 쿠폰이 현재 유효한 상태입니다.", resDto));
    }

    /**
     * 쿠폰 전체 조회
     * @return id, serialNumber, expireDate, period, count, couponTitle, registDate, updateDate
     */
    @GetMapping("/findByAll")
    public ResponseEntity<?> findByAll() {
        List<CouponResDto> resDtoList = couponService.findByAll();
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "유효한 모든 쿠폰을 조회하였습니다.", resDtoList));

    }

    /**
     * 쿠폰 남은 수량 확인
     * @param id id
     * @return id, couponTitle, count
     * @throws InvalidCouponAccessException 쿠폰에 대한 잘못된 접근
     */
    @GetMapping("/findCountById/{id}")
    public ResponseEntity<?> findCountById(@PathVariable Long id) throws InvalidCouponAccessException {
        CouponCountResDto resDto= couponService.findCountById(id);
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, id + "번의 남은 수량은 " + resDto.getCount() + "입니다.", resDto));
    }

    /**
     * 사용자 쿠폰 조회
     * @param userKey userKey
     * @return id, userKey, userCoupon.registDate, userCouponKey, couponTitle, expiredDate
     */
    @GetMapping("/findByUserKey/{userKey}")
    public ResponseEntity<?> findByUserKey(@PathVariable Long userKey) {
        List<UserCouponInfoResDto> resDtoList = couponService.findByUserKey(userKey);
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "해당하는 유저의 보유 쿠폰 정보를 조회하였습니다.", resDtoList));
    }

    /**
     * userKey 없이 사용자 쿠폰 조회
     * @param userInfo
     * @return
     */
    @GetMapping("/findByUserKey")
    public ResponseEntity<?> findByUserKey(@AuthenticationPrincipal TokenUserInfo userInfo) {
        List<UserCouponInfoResDto> resDtoList = couponService.findByUserKey(userInfo.getId());
        return ResponseEntity.ok().body(new CommonResDto(HttpStatus.OK, "해당하는 유저의 보유 쿠폰 정보를 조회하였습니다.", resDtoList));
    }

    /**
     * 쿠폰 ID 조회
     * @param serialNumber serialNumber
     * @return id
     * @throws EntityNotFoundException 해당하는 쿠폰이 존재하지 않음
     */
    @GetMapping("/findBySerial/{serialNumber}")
    public ResponseEntity<Long> findBySerial(@PathVariable String serialNumber) throws EntityNotFoundException {
        Long id = couponService.findBySerial(serialNumber);
        return ResponseEntity.ok().body(id);
    }

    /**
     * 쿠폰 삭제
     * @param serialNumber
     * @return
     * @throws EntityNotFoundException
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{serialNumber}")
    public ResponseEntity<?> deleteBySerial(@PathVariable String serialNumber) throws EntityNotFoundException {
        Long id = couponService.delete(serialNumber);
        return ResponseEntity.ok().body(id);
    }
}
