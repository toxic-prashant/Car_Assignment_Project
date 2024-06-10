package com.project.controller;

import com.project.entity.Driver;
import com.project.exception.CarAlreadyInUseException;
import com.project.exception.DriverNotFoundException;
import com.project.repository.DriverRepository;
import com.project.service.DriverService;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/driver")
public class DriverController {
  @Resource
  private DriverService driverService;

  @Resource
  private DriverRepository driverRepository;

  @GetMapping
  public List<Driver> getAllDrivers() {
    return driverRepository.findAll();
  }

  @GetMapping("/{id}")
  public Driver findDriverById(@PathVariable Long id) {
    return driverRepository.findById(id).orElseThrow(() -> {
      return new DriverNotFoundException(id);
    });
  }

  @PostMapping
  public Driver createDriver(@RequestBody Driver driver) {
    return driverRepository.save(driver);
  }

  @PutMapping("/{id}")
  public Driver updateDriver(@PathVariable Long id, @RequestBody Driver driverDetail) {
    Driver driver = driverRepository.findById(id).orElseThrow(() -> new DriverNotFoundException(id));
    driver.setName(driverDetail.getName());
    driver.setStatus(driverDetail.getStatus());
    driver.setSelectedCar(driverDetail.getSelectedCar());

    return driverRepository.save(driver);
  }

  @DeleteMapping("/{id}")
  public void removeDriver(@PathVariable Long id) {
    Driver driver = driverRepository.findById(id).orElseThrow(() -> new DriverNotFoundException(id));
    driverRepository.delete(driver);
  }

  @PostMapping("/{driverId}/selectCar/{carId}")
  public Driver selectCar(@PathVariable Long driverId, @PathVariable Long carId)
      throws CarAlreadyInUseException {
    return driverService.selectCar(driverId, carId);
  }

  @PostMapping("/{driverId}/deselectCar")
  public Driver deselectCar(@PathVariable Long driverId) {
    return driverService.deselectcar(driverId);
  }
}
