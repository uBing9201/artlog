package com.playdata.couponservice.coupons.service;

import com.playdata.couponservice.common.exception.InvalidCouponAccessException;
import com.playdata.couponservice.common.exception.InvalidCouponRegisterException;
import com.playdata.couponservice.coupons.dto.response.*;
import com.playdata.couponservice.coupons.dto.request.CouponReqDto;
import com.playdata.couponservice.coupons.entity.Coupon;
import com.playdata.couponservice.coupons.feign.UserFeignClient;
import com.playdata.couponservice.coupons.repository.CouponRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final UserFeignClient userFeignClient;

    /**
     * 쿠폰 등록
     *
     * @param dto serialNumber, expireDate, period, count, couponTitle
     * @return id, couponTitle
     * @throws InvalidCouponRegisterException 쿠폰 등록 실패
     */
    public CouponSaveResDto insert(CouponReqDto dto) throws InvalidCouponRegisterException {
        // 쿠폰의 유효기간이 있다면 현재 만료되지는 않았는지 확인
        LocalDateTime expireDate = dto.getExpireDate();
        if (expireDate != null && expireDate.isBefore(LocalDateTime.now())) {
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
     *
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
        if (coupon.getExpireDate() != null && coupon.getExpireDate().isBefore(LocalDateTime.now())) {
            if(coupon.getActive().equals('Y')) {
                coupon.changeCouponActive();
            }
            resDto.setValid(false);
        }

        // 수량 검증
        if (coupon.getCount() != null && coupon.getCount() <= 0) {
            if(coupon.getActive().equals('Y')) {
                coupon.changeCouponActive();
            }
            resDto.setValid(false);
        }

        couponRepository.save(coupon);

        return resDto;
    }

    /**
     * 쿠폰 전체 조회
     *
     * @return id, serialNumber, expireDate, period, count, couponTitle, registDate, updateDate
     */
    @Transactional
    public List<CouponResDto> findByAll() {
        List<Coupon> coupons = couponRepository.findAll();

        coupons.forEach(coupon -> isValid(coupon.getId())); // 여기 로직 사실 중복임.. 나중에 시간되면 고칠게요.. ㅠㅠ

        return coupons.stream()
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
                                .active(coupon.getActive() == 'Y')
                                .build()
                )
                .toList();
    }

    /**
     * 쿠폰 남은 수량 확인
     *
     * @param id id
     * @return id, couponTitle, count
     * @throws InvalidCouponAccessException 쿠폰에 대한 잘못된 접근
     */
    @Transactional
    public CouponCountResDto findCountById(Long id) throws InvalidCouponAccessException {
        Coupon coupon = couponRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Coupon Not Found")
        );

        if (coupon.getCount() == null) {
            throw new InvalidCouponAccessException("This coupon has no count");
        }

        if (!isValid(coupon.getId()).isValid()) {
            throw new InvalidCouponAccessException("Invalid Coupon Access");
        }

        return CouponCountResDto.builder()
                .id(coupon.getId())
                .count(coupon.getCount())
                .couponTitle(coupon.getCouponTitle())
                .build();
    }

    /**
     * 사용자 쿠폰 조회
     * @param userKey userKey
     * @return id, userKey, userCoupon.registDate, userCouponKey, couponTitle, expiredDate
     */
    public List<UserCouponInfoResDto> findByUserKey(Long userKey) throws EntityNotFoundException {
        List<UserCouponResDto> userCouponList = userFeignClient.findCouponById(userKey).getBody();
        List<Coupon> coupons = userCouponList.stream()
                .map(dto -> couponRepository.getCouponById(dto.getCouponKey()).orElseThrow(
                        () -> new EntityNotFoundException("Coupon Not Found")
                )).toList();

        List<UserCouponInfoResDto> resDto = new ArrayList<>();
        for (int i = 0; i < userCouponList.size(); i++) {
            Coupon coupon = coupons.get(i);
            UserCouponResDto dto = userCouponList.get(i);

            if ((!coupon.getActive().equals('N')) && // 활성화 상태 확인
                (coupon.getPeriod() == null || coupon.getPeriod() >= ChronoUnit.DAYS.between(dto.getRegistDate(), LocalDateTime.now())) && // 기간 상태 확인
                (coupon.getExpireDate() == null || !coupon.getExpireDate().isBefore(LocalDateTime.now()))) { // 만료기간 상태 확인

                // 정상적인 쿠폰이 맞다면
                LocalDateTime expiredDate = null;
                if(coupon.getExpireDate() != null) expiredDate = coupon.getExpireDate();
                if(coupon.getPeriod() != null) {
                    LocalDateTime periodEndDate = LocalDateTime.now().plusDays(coupon.getPeriod());
                    if(expiredDate == null || expiredDate.isAfter(periodEndDate)) expiredDate = periodEndDate;
                }

                // 응답 DTO로 변환하여 전달
                resDto.add(
                        UserCouponInfoResDto.builder()
                                .id(coupon.getId())
                                .userKey(dto.getUserKey())
                                .userCouponKey(dto.getId())
                                .registDate(dto.getRegistDate())
                                .couponTitle(coupon.getCouponTitle())
                                .expiredDate(expiredDate)
                                .serialNumber(coupon.getSerialNumber())
                                .build()
                );
            }

        }

        return resDto;
    }

    @Transactional
    public Long findBySerial(String serialNumber) throws EntityNotFoundException {
        log.error(serialNumber);
        Coupon coupon = couponRepository.getCouponBySerialNumber(serialNumber).orElseThrow(
                () -> new EntityNotFoundException("쿠폰이 없습니다.")
        );

        if(!isValid(coupon.getId()).isValid()) {
            throw new EntityNotFoundException("유효한 쿠폰이 없습니다.");
        }

        if(coupon.getCount() != null && coupon.getCount() > 0){
            coupon.decreaseCount();
            if(coupon.getCount() == 0){
                coupon.changeCouponActive();
            }
            couponRepository.save(coupon);
        }

        return coupon.getId();
    }

    public Long delete(String serialNumber) {
        Coupon coupon = couponRepository.getCouponBySerialNumber(serialNumber).orElseThrow(
                () -> new EntityNotFoundException("쿠폰이 없습니다.")
        );

        if(coupon.getActive().equals('Y')) {
            coupon.changeCouponActive();
        }

        couponRepository.save(coupon);

        return coupon.getId();
    }
}
