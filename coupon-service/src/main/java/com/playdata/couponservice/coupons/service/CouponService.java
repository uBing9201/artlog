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
     * @param dto CouponReqDto - 쿠폰 등록에 필요한 정보
     * @return CouponSaveResDto - id, couponTitle
     * @throws InvalidCouponRegisterException 유효기간, 사용가능 기간, 수량 검증
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
     * @param id coupon 객체를 찾을 id 값
     * @return id와 validate로 유효성에 대한 true, false 값 반환
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
     * @return active가 true인 coupon만 CouponResDto List로 반환
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
     *
     * @param id coupon 객체를 찾을 id 값
     * @return coupon의 개수, id, 이름을 반환
     * @throws InvalidCouponAccessException 쿠폰 개수 항목이 없거나, 유효하지 않다면 예외 처리
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
