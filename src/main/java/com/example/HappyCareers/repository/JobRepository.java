package com.example.HappyCareers.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.HappyCareers.entity.Job;

public interface JobRepository extends JpaRepository<Job,Long>{
	@Query("SELECT j FROM Job j WHERE " +
		       "(:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
		       " OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
		       "(:location IS NULL OR j.location = :location)")
	List<Job> findByKeywordAndLocation(@Param("keyword") String keyword,
		                                   @Param("location") String location);

	@Query("SELECT j FROM Job j WHERE LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) AND LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))")
	List<Job> searchByKeywordAndLocation(@Param("keyword") String keyword, @Param("location") String location);

	// Jobs posted today
    List<Job> findByPostedDate(LocalDate postedDate);

    // Jobs posted after a specific date (e.g., last 7 days)
    List<Job> findByPostedDateAfter(LocalDate date);

    // All jobs ordered by postedDate descending (newest first)
    List<Job> findAllByOrderByPostedDateDesc();

    // All jobs ordered by postedDate ascending (oldest first)
    List<Job> findAllByOrderByPostedDateAsc();
    
 // You can also use @Query if needed
    List<Job> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);

    // With location filtering and sorting
    @Query("SELECT j FROM Job j WHERE " +
           "(:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:location IS NULL OR j.location = :location) " +
           "ORDER BY j.salary DESC")
    List<Job> findByKeywordAndLocationOrderBySalaryDesc(@Param("keyword") String keyword, @Param("location") String location);

    @Query("SELECT j FROM Job j WHERE " +
           "(:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:location IS NULL OR j.location = :location) " +
           "ORDER BY j.salary ASC")
    List<Job> findByKeywordAndLocationOrderBySalaryAsc(@Param("keyword") String keyword, @Param("location") String location);

    @Query("SELECT j FROM Job j WHERE " +
           "(:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:location IS NULL OR j.location = :location) " +
           "ORDER BY j.postedDate DESC")
    List<Job> findByKeywordAndLocationOrderByPostedDateDesc(@Param("keyword") String keyword, @Param("location") String location);

    
    @Query("SELECT j FROM Job j WHERE " +
           "(:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:location IS NULL OR j.location = :location) " +
           "ORDER BY j.postedDate ASC")
    List<Job> findByKeywordAndLocationOrderByPostedDateAsc(@Param("keyword") String keyword, @Param("location") String location);

 
    @Query("SELECT j FROM Job j WHERE " +
            "(LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "ORDER BY j.salary DESC")
     List<Job> searchByKeywordOrderBySalaryDesc(@Param("keyword") String keyword);

     @Query("SELECT j FROM Job j WHERE " +
            "(LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "ORDER BY j.salary ASC")
     List<Job> searchByKeywordOrderBySalaryAsc(@Param("keyword") String keyword);
 
     Page<Job> findAll(Pageable pageable);
     
     // If you're using keyword+location search: myTRY
     @Query("SELECT j FROM Job j WHERE " +
    	       "(:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
    	       "OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
    	       "AND (:location IS NULL OR j.location = :location)")
    	Page<Job> searchJobs(@Param("keyword") String keyword,
    	                     @Param("location") String location,
    	                     Pageable pageable);
     
     
     
     List<Job> findTop5ByOrderByPostedDateDesc();

}



