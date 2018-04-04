package edu.sjsu.cmpe275.lab2.controller;

import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.Map;


@RestController
@RequestMapping(path = "/passenger")
public class PassengerController {

    @Autowired
    PassengerService passengerService;


    // API 3
    @PostMapping(path = "")
    public ResponseEntity createPassenger(@RequestParam Map<String,String> params){
        Passenger passenger = passengerService.createPassenger(params);
        return new ResponseEntity(params,HttpStatus.OK);
    }

    // API 1&2
    @GetMapping(path = "/{id}")
    public ResponseEntity displayPassenger(@PathVariable("id") String id, Map<String,String> map){
        return passengerService.findByPassengerId(id);
        //Return XML if map.get("xml") is true
    }

    //Update Passenger Details. API4
    @PutMapping(path = "/{id}")
    public ResponseEntity updatePassenger(@PathVariable("id") String id, Map<String,String> map){
        Passenger passenger = passengerService.updatePassenger(id,map);
        return new ResponseEntity(passenger,HttpStatus.OK);

    }

    //Delete passenger. API 5
    @DeleteMapping(path = "/{id}")
    public void deletePassenger(@PathVariable("id") String id){
        passengerService.deletePassenger(id);
    }
}