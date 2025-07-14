package com.codegym.mobilestore.repository;

import com.codegym.mobilestore.model.ShipInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IShipInfoRepository extends JpaRepository<ShipInfo, Long> {
}
