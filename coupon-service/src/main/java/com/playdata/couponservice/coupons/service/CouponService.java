package com.playdata.couponservice.coupons.service;

import com.playdata.couponservice.common.exception.InvalidCouponAccessException;
import com.playdata.couponservice.common.exception.InvalidCouponRegisterException;
import com.playdata.couponservice.coupons.dto.response.CouponCountResDto;
import com.playdata.couponservice.coupons.dto.response.CouponResDto;
import com.playdata.couponservice.coupons.dto.request.CouponReqDto;
import com.playdata.couponservice.coupons.dto.response.CouponSaveResDto;
import com.playdata.couponservice.coupons.dto.response.CouponValidateDto;
import com.playdata.couponservice.coupons.entity.Coupon;
import com.playdata.couponservice.coupons.repository.CouponRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;

    /**
     * 쿠폰 등록
     * @param dto serialNumber, expireDate, period, count, couponTitle
     * @return id, couponTitle
     * @throws InvalidCouponRegisterException 쿠폰 등록 실패
     */
    public CouponSaveResDto insert(CouponReqDto dto) throws InvalidCouponRegisterException {
        // 쿠폰의 유효기간이 있다면 현재 만료되지는 않았는지 확인
        LocalDateTime expireDate = dto.getExpireDate();
        if (expireDate != null && !expireDate.isBefore(LocalDateTime.now())) {
            throw new InvalidCouponRegisterException("Invalid Coupon Expire Date");
        }

        // 쿠폰의 사용 가능 기간이 정해져 있다면 0 이하는 아닌지 확인
        Integer period = dto.getPeriod();
        if (period != null && period <= 0) {
            throw new InvalidCouponRegisterException("Invalid Coupon Period");
        }

        // 쿠폰의 수량이 0개 이하로 설정되지는 않았는지 확인
        Integer count = dto.getCount();
        if (count != null && count <= 0) {
            throw new InvalidCouponRegisterException("Invalid Coupon Count");
        }

        // 쿠폰 객체 생성
        Coupon coupon = Coupon.builder()
                .serialNumber(dto.getSerialNumber())
                .expireDate(dto.getExpireDate())
                .period(dto.getPeriod())
                .count(dto.getCount())
                .couponTitle(dto.getCouponTitle())
                .active('Y')
                .build();

        // DB 저장
        couponRepository.save(coupon);

        // 응답 DTO 변환
        return CouponSaveResDto.builder()
                .id(coupon.getId())
                .couponTitle(coupon.getCouponTitle())
                .build();
    }


    /**
     * 쿠폰 유효성 검증 (쿠폰 서비스 내부 사용)
     * @param id id
     * @return id, valid
     */
    @Transactional
    public CouponValidateDto isValid(Long id) {
        CouponValidateDto resDto = CouponValidateDto.builder().id(id).valid(true).build();

        // id로 쿠폰 조회
        Coupon coupon = couponRepository.getCouponById(id).orElseThrow(
                () -> new EntityNotFoundException("Coupon Not Found")
        );

        // 활성화 상태 검증
        if (coupon.getActive().equals('N')) resDto.setValid(false);

        // 유효기간 검증
        if (coupon.getExpireDate() != null && !coupon.getExpireDate().isBefore(LocalDateTime.now())) {
            coupon.changeCouponActive();
            resDto.setValid(false);
        }

        // 수량 검증
        if (coupon.getCount() != null && coupon.getCount() <= 0) {
            coupon.changeCouponActive();
            resDto.setValid(false);
        }

        return resDto;
    }

    /**
     * 쿠폰 전체 조회
     * @return id, serialNumber, expireDate, period, count, couponTitle, registDate, updateDate
     */
    @Transactional
    public List<CouponResDto> findByAll() {
        List<Coupon> coupons = couponRepository.findAll();

        return coupons.stream()
                .filter(coupon -> isValid(coupon.getId()).isValid())
                .map(coupon ->
                        CouponResDto.builder()
                                .id(coupon.getId())
                                .couponTitle(coupon.getCouponTitle())
                                .count(coupon.getCount())
                                .period(coupon.getPeriod())
                                .expireDate(coupon.getExpireDate())
                                .serialNumber(coupon.getSerialNumber())
                                .registDate(coupon.getRegistDate())
                                .updateDate(coupon.getUpdateDate())
                                .build()
                )
                .toList();
    }

    /**
     * 쿠폰 남은 수량 확인
     * @param id id
     * @return id, couponTitle, count
     * @throws InvalidCouponAccessException 쿠폰에 대한 잘못된 접근
     */
    @Transactional
    public CouponCountResDto findCountById(Long id) throws InvalidCouponAccessException {
        Coupon coupon = couponRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Coupon Not Found")
        );

        if(coupon.getCount() == null) {
            throw new InvalidCouponAccessException("This coupon has no count");
        }

        if(!isValid(coupon.getId()).isValid()) {
            throw new InvalidCouponAccessException("Invalid Coupon Access");
        }

        return CouponCountResDto.builder()
                .id(coupon.getId())
                .count(coupon.getCount())
                .couponTitle(coupon.getCouponTitle())
                .build();
    }
}
