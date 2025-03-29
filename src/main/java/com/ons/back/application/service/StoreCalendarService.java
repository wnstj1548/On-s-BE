package com.ons.back.application.service;

import com.ons.back.commons.exception.ApplicationException;
import com.ons.back.commons.exception.payload.ErrorStatus;
import com.ons.back.persistence.domain.Store;
import com.ons.back.persistence.repository.StoreCalendarRepository;
import com.ons.back.persistence.repository.StoreRepository;
import com.ons.back.presentation.dto.request.CreateStoreCalendarRequest;
import com.ons.back.presentation.dto.response.ReadCalendarResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreCalendarService {

    private final StoreCalendarRepository storeCalendarRepository;
    private final StoreRepository storeRepository;

    @Transactional(readOnly = true)
    public List<ReadCalendarResponse> getStoreCalendarByYearAndMonth(Long storeId, Integer year, Integer month) {
        return storeCalendarRepository.findByStore_StoreIdAndYearAndMonth(storeId, year, month).stream().map(ReadCalendarResponse::fromEntity).toList();
    }

    public ReadCalendarResponse createStoreCalendar(CreateStoreCalendarRequest request) {

        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new ApplicationException(
                        ErrorStatus.toErrorStatus("해당하는 가게가 없습니다.", 404, LocalDateTime.now())
                ));

        return ReadCalendarResponse.fromEntity(storeCalendarRepository.save(request.toEntity(store)));
    }
}
