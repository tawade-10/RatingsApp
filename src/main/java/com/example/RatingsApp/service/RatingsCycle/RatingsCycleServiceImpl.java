package com.example.RatingsApp.service.RatingsCycle;

import com.example.RatingsApp.dto.EmployeesDto.EmployeesResponseDto;
import com.example.RatingsApp.dto.RatingsCycleDto.RatingsCycleRequestDto;
import com.example.RatingsApp.dto.RatingsCycleDto.RatingsCycleResponseDto;
import com.example.RatingsApp.dto.TeamsDto.TeamsResponseDto;
import com.example.RatingsApp.entity.Employees;
import com.example.RatingsApp.entity.RatingsCycle;
import com.example.RatingsApp.entity.Teams;
import com.example.RatingsApp.entity.enums.CycleStatus;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.RatingsCycleRepo;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RatingsCycleServiceImpl implements RatingsCycleService{

    private final RatingsCycleRepo ratingsCycleRepo;

    public RatingsCycleServiceImpl(RatingsCycleRepo ratingsCycleRepo) {
        this.ratingsCycleRepo = ratingsCycleRepo;
    }

    @Override
    public RatingsCycleResponseDto createRatingsCycle(RatingsCycleRequestDto ratingsCycleRequestDto) {

        if (ratingsCycleRequestDto.getCycleName() == null || Objects.equals(ratingsCycleRequestDto.getCycleName(), "")) {
            throw new APIException("Cycle name is required!");
        }

        String cycleName = ratingsCycleRequestDto.getCycleName();

        Optional<RatingsCycle> existingCycle = ratingsCycleRepo.findByCycleNameIgnoreCase(cycleName);
        if (existingCycle.isPresent()) {
            return new RatingsCycleResponseDto(existingCycle.get());
        }

        LocalDate[] dates = getCycleDates(cycleName);
        LocalDate startDate = dates[0];
        LocalDate endDate   = dates[1];

        LocalDate today = LocalDate.now();
        CycleStatus status;

        if (!today.isBefore(startDate) && !today.isAfter(endDate)) {
            status = CycleStatus.ACTIVE;
        } else if (today.isBefore(startDate)) {
            status = CycleStatus.UPCOMING;
        } else {
            status = CycleStatus.CLOSED;
        }

        RatingsCycle cycle = new RatingsCycle();
        cycle.setCycleName(cycleName);
        cycle.setStartDate(startDate);
        cycle.setEndDate(endDate);
        cycle.setStatus(status);

        RatingsCycle saved = ratingsCycleRepo.save(cycle);
        return new RatingsCycleResponseDto(saved);
    }

    @Override
    public List<RatingsCycleResponseDto> getAllCycles() {
        List<RatingsCycle> listRatings = ratingsCycleRepo.findAll(Sort.by(Sort.Direction.ASC, "cycleId"));
        return listRatings.stream().map(RatingsCycleResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public RatingsCycleResponseDto deleteRatingsCycle(Long ratingsCycleId) {
        RatingsCycle ratingsCycle = ratingsCycleRepo.findById(ratingsCycleId)
                .orElseThrow(() -> new ResourceNotFoundException("Ratings Cycle not found with ID: " + ratingsCycleId));
        ratingsCycleRepo.delete(ratingsCycle);
        return null;
    }

    @Override
    public RatingsCycleResponseDto getCycleById(Long ratingsCycleId) {
        RatingsCycle cycle = ratingsCycleRepo.findById(ratingsCycleId)
                .orElseThrow(() -> new ResourceNotFoundException("Ratings Cycle not found with ID: " + ratingsCycleId));
        return new RatingsCycleResponseDto(cycle);
    }

    @Override
    public RatingsCycleResponseDto updateRatings(Long ratingsCycleId) {

        RatingsCycle cycle = ratingsCycleRepo.findById(ratingsCycleId)
                .orElseThrow(() -> new ResourceNotFoundException("Ratings Cycle not found with ID: " + ratingsCycleId));

        LocalDate today = LocalDate.now();

        if (cycle.getStatus() == CycleStatus.ACTIVE && today.isAfter(cycle.getEndDate())) {
            cycle.setStatus(CycleStatus.CLOSED);
            ratingsCycleRepo.save(cycle);

            RatingsCycle nextCycle = ratingsCycleRepo.findFirstByStartDateAfterOrderByStartDateAsc(cycle.getEndDate());

            nextCycle.setStatus(CycleStatus.ACTIVE);
            ratingsCycleRepo.save(nextCycle);
            return new RatingsCycleResponseDto(cycle);
        }
        return new RatingsCycleResponseDto(cycle);
    }

//    public String getRatingsCycle(LocalDate startDate, LocalDate endDate){
//
//        int year = startDate.getYear();
//
//        LocalDate startQ1 = LocalDate.of(year , 1, 1);
//        LocalDate endQ1 = LocalDate.of(year,3,31);
//
//        LocalDate startQ2 = LocalDate.of(year,4,1);
//        LocalDate endQ2 = LocalDate.of(year, 6,30);
//
//        LocalDate startQ3 = LocalDate.of(year,7,1);
//        LocalDate endQ3 = LocalDate.of(year,9,30);
//
//        LocalDate startQ4 = LocalDate.of(year, 10,1);
//        LocalDate endQ4 = LocalDate.of(year, 12,31);
//
//        if (!startDate.isBefore(startQ1) && !endDate.isAfter(endQ1)){
//            return "Q1-" + year;
//        }
//        if (!startDate.isBefore(startQ2) && !endDate.isAfter(endQ2)){
//            return "Q2-" + year;
//        }
//        if (!startDate.isBefore(startQ3) && !endDate.isAfter(endQ3)){
//            return "Q3-" + year;
//        }
//        if (!startDate.isBefore(startQ4) && !endDate.isAfter(endQ4)){
//            return "Q4-" + year;
//        }
//        return "Invalid Cycle";
//    }

    private LocalDate[] getCycleDates(String cycleName) {
        String[] parts = cycleName.split("-");

        String quarters = parts[0].toUpperCase();
        int year;
        try {
            year = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new APIException("Invalid year in cycle name: " + parts[1]);
        }

        return switch (quarters) {
            case "Q1" -> new LocalDate[]{LocalDate.of(year, 1, 1), LocalDate.of(year, 3, 31)};
            case "Q2" -> new LocalDate[]{LocalDate.of(year, 4, 1), LocalDate.of(year, 6, 30)};
            case "Q3" -> new LocalDate[]{LocalDate.of(year, 7, 1), LocalDate.of(year, 9, 30)};
            case "Q4" -> new LocalDate[]{LocalDate.of(year, 10, 1), LocalDate.of(year, 12, 31)};
            default -> throw new APIException("Invalid quarter in cycle name: " + quarters);
        };
    }
}



