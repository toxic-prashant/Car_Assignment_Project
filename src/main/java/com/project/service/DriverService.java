package com.project.service;

import com.project.entity.Car;
import com.project.entity.Driver;
import com.project.exception.CarAlreadyInUseException;
import com.project.exception.CarNotFoundException;
import com.project.exception.DriverNotFoundException;
import com.project.repository.CarRepository;
import com.project.repository.DriverRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class DriverService {

  @Resource
  private DriverRepository driverRepository;

  @Resource
  private CarRepository carRepository;

  public Driver selectCar(Long driverId, Long carId) throws CarAlreadyInUseException {
    Driver driver = driverRepository.findById(driverId)
        .orElseThrow(() -> new DriverNotFoundException(driverId));
    Car car = carRepository.findById(carId).orElseThrow(() -> new CarNotFoundException(carId));

    if (car.getDriver() != null) {
      throw new CarAlreadyInUseException(carId);
    }

    car.setDriver(driver);
    driver.setSelectedCar(car);

    carRepository.save(car);
    return driverRepository.save(driver);
  }


  public Driver deselectcar(Long driverId) {
    Driver driver = driverRepository.findById(driverId)
        .orElseThrow(() -> new DriverNotFoundException(driverId));
    Car car = driver.getSelectedCar();

    if (car != null) {
      car.setDriver(null);
      carRepository.save(car);
    }

    driver.setSelectedCar(null);
    return driverRepository.save(driver);
  }
}
