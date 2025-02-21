package com.minho.ownit.region;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionAuctionRepo extends CrudRepository<RegionAuction, Integer>{

}
