package com.example.RatingsApp.service.RatingsCycle;

import com.example.RatingsApp.dto.RatingsCycleDto.RatingsCycleRequestDto;
import com.example.RatingsApp.dto.RatingsCycleDto.RatingsCycleResponseDto;
import com.example.RatingsApp.dto.TeamsDto.TeamsResponseDto;
import com.example.RatingsApp.entity.RatingsCycle;
import com.example.RatingsApp.entity.Teams;
import com.example.RatingsApp.entity.enums.CycleStatus;
import com.example.RatingsApp.exception.APIException;
import com.example.RatingsApp.exception.ResourceNotFoundException;
import com.example.RatingsApp.repository.RatingsCycleRepo;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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

        RatingsCycle ratingsCycle = new RatingsCycle();

        LocalDate startDate = ratingsCycleRequestDto.getStartDate();
        LocalDate endDate = ratingsCycleRequestDto.getEndDate();

        if (startDate.isAfter(endDate)) {
            throw new APIException("Start date cannot be after end date!");
        }

        if(endDate.isBefore(startDate)){
            throw new APIException("End date cannot be before start date!");
        }

        if (startDate.getYear() != endDate.getYear()) {
            throw new APIException("Start date and end date must be in the same year!");
        }

        String cycle = getRatingsCycle(startDate, endDate);

        Optional<RatingsCycle> existingCycleStartDate = ratingsCycleRepo.findByStartDate(ratingsCycleRequestDto.getStartDate());
             if(existingCycleStartDate.isPresent()) {
                 throw new APIException("Cycle with start date '" + ratingsCycleRequestDto.getStartDate() + "' already exists!");
             };

             Optional<RatingsCycle> existingCycleEndDate = ratingsCycleRepo.findByEndDate(ratingsCycleRequestDto.getEndDate());
                  if(existingCycleEndDate.isPresent()) {
                      throw new APIException("Cycle with end date '" + ratingsCycleRequestDto.getEndDate() + "' already exists!");
                  };

        LocalDate currentDate = LocalDate.now();
        CycleStatus status;
        if ((currentDate.isEqual(startDate) || currentDate.isAfter(startDate))
                && (currentDate.isEqual(endDate) || currentDate.isBefore(endDate))) {
           status = CycleStatus.ACTIVE;
        } else if (currentDate.isBefore(startDate)) {
            status = CycleStatus.UPCOMING;
        } else {
            status = CycleStatus.CLOSED;
        }

//        Optional<RatingsCycle> existingCycle = ratingsCycleRepo.findByCycleNameIgnoreCase(ratingsCycleRequestDto.);
//        if (existingTeam.isPresent()) {
//            return new TeamsResponseDto(existingTeam.get());
//        }

//        ratingsCycle.setCycleId(ratingsCycleRequestDto.getCycleId());
        ratingsCycle.setCycleName(cycle);
        ratingsCycle.setStartDate(startDate);
        ratingsCycle.setEndDate(endDate);
        ratingsCycle.setStatus(status);

        RatingsCycle saved = ratingsCycleRepo.save(ratingsCycle);

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

    public String getRatingsCycle(LocalDate startDate, LocalDate endDate){

        int year = startDate.getYear();

        LocalDate startQ1 = LocalDate.of(year , 1, 1);
        LocalDate endQ1 = LocalDate.of(year,3,31);

        LocalDate startQ2 = LocalDate.of(year,4,1);
        LocalDate endQ2 = LocalDate.of(year, 6,30);

        LocalDate startQ3 = LocalDate.of(year,7,1);
        LocalDate endQ3 = LocalDate.of(year,9,30);

        LocalDate startQ4 = LocalDate.of(year, 10,1);
        LocalDate endQ4 = LocalDate.of(year, 12,31);

        if (!startDate.isBefore(startQ1) && !endDate.isAfter(endQ1)){
            return "Q1-" + year;
        }
        if (!startDate.isBefore(startQ2) && !endDate.isAfter(endQ2)){
            return "Q2-" + year;
        }
        if (!startDate.isBefore(startQ3) && !endDate.isAfter(endQ3)){
            return "Q3-" + year;
        }
        if (!startDate.isBefore(startQ4) && !endDate.isAfter(endQ4)){
            return "Q4-" + year;
        }
        return "Invalid Cycle";
    }
}



