package com.dinechain.repository;

import com.dinechain.constant.TableStatus;
import com.dinechain.entity.DiningTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiningTableRepository extends JpaRepository<DiningTableEntity, Integer> {

    @Query("select t from DiningTableEntity t join AreaEntity a on a.id = t.areaId where t.id = :tableId and a.branchId = :branchId")
    Optional<DiningTableEntity> findInBranch(@Param("tableId") Integer tableId, @Param("branchId") Integer branchId);

    @Query("select t from DiningTableEntity t join AreaEntity a on a.id = t.areaId where a.branchId = :branchId order by a.name, t.name")
    List<DiningTableEntity> findByBranchId(@Param("branchId") Integer branchId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update DiningTableEntity t set t.status = :status where t.id = :tableId")
    int updateStatus(@Param("tableId") Integer tableId, @Param("status") TableStatus status);
}
