package com.amwaydemo.consumer.repository;

import com.amwaydemo.consumer.model.Shipment;
import com.amwaydemo.consumer.utils.ShippingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long>{
	
	
	Optional<Shipment> findByOrderId(long orderId);
	
	@Query("SELECT s FROM Shipment s WHERE s.shippingStatus = :#{#status}")
	List<Shipment> findByShippingStatus(ShippingStatus status);
	

}
