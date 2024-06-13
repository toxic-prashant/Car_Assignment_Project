package com.project.controller;

import com.project.entity.Car;
import com.project.entity.Manufacturer;
import com.project.exception.CarNotFoundException;
import com.project.exception.ManufacturerNotFoundException;
import com.project.repository.CarRepository;
import com.project.repository.ManufacturerRepository;
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
@RequestMapping("/cars")
public class CarController {

  @Resource
  private CarRepository carRepository;

  @Resource
  private ManufacturerRepository manufacturerRepository;

  @GetMapping
  public List<Car> getAllCars() {
    return carRepository.findAll();
  }

  @GetMapping("/{id}")
  public Car getCarById(@PathVariable Long id) {
    return carRepository.findById(id).orElseThrow(() -> new CarNotFoundException(id));
  }

  @PostMapping
  public Car createCar(@RequestBody Car car) {
    if (car.getManufacturer() != null) {
      Manufacturer manufacturer = manufacturerRepository.findById(car.getManufacturer().getId())
          .orElseThrow(() -> {
            return new ManufacturerNotFoundException(car.getManufacturer().getId());
          });
      car.setManufacturer(manufacturer);
    }
    return carRepository.save(car);
  }

  @PutMapping("/{id}")
  public Car updateCar(@PathVariable Long id, @RequestBody Car carDetail) {
    Car car = carRepository.findById(id).orElseThrow(() -> new CarNotFoundException(id));
    car.setSeatCount(carDetail.getSeatCount());
    car.setLicensePlate(carDetail.getLicensePlate());
    car.setEngineType(carDetail.getEngineType());
    car.setConvertible(carDetail.isConvertible());
    car.setRating(carDetail.getRating());

    if (carDetail.getManufacturer() != null) {
      Manufacturer manufacturer = manufacturerRepository.findById(
          carDetail.getManufacturer().getId()).orElseThrow(
          () -> new ManufacturerNotFoundException(carDetail.getManufacturer().getId()));
      car.setManufacturer(manufacturer);
    }

    return carRepository.save(car);
  }

  @DeleteMapping("/{id}")
  public void deleteById(@PathVariable Long id) {
    Car car = carRepository.findById(id).orElseThrow(() -> new CarNotFoundException(id));
    carRepository.deleteById(id);
  }
}
